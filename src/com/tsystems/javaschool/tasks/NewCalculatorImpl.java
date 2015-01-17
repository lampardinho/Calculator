package com.tsystems.javaschool.tasks;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Kolia on 17.01.2015.
 */
public class NewCalculatorImpl implements Calculator
{
    public static String input = "12.541+4";
    private Tokenizer tokenizer = new Tokenizer(input);

    public static void main(String[] args)
    {
        Calculator c = new NewCalculatorImpl();
        System.out.println(c.evaluate(input));
    }

    @Override
    public String evaluate(String statement)
    {
        //translate to reverse polish notation
        statement = toRPN(statement);


        Stack<IExpression> stack = new Stack<IExpression>();

        String[] tokenList = statement.split(" ");
        for (String s : tokenList) {
            if (isOperator(s)) {
                IExpression rightExpression = stack.pop();
                IExpression leftExpression = stack.pop();
                IExpression operator = getOperatorInstance(s, leftExpression,
                        rightExpression);
                double result = operator.interpret();
                stack.push(new NumberExpression(result));
            } else {
                IExpression i = new NumberExpression(s);
                stack.push(i);
            }
        }
        return String.valueOf(stack.pop().interpret());
    }

    public static boolean isOperator(String s) {
        if (s.equals("+") || s.equals("-") || s.equals("*"))
            return true;
        else
            return false;
    }

    public static IExpression getOperatorInstance(String s, IExpression left, IExpression right)
    {
        char c = s.charAt(0);
        switch (c) {
            case '+':
                return new PlusExpression(left, right);
            case '-':
                return new MinusExpression(left, right);
            case '*':
                return new MultiplyExpression(left, right);
        }
        return null;
    }


    private int curPosition = 0;
    private String toRPN(String statement)
    {
        /**
         * First we translate to reverse-polish notation
         */
        String n = tokenizer.next();

        LinkedList<String> pn = new LinkedList<String>();
        Stack<String> st = new Stack<String>();

        while (n != null) {
            if (NewCalculatorImpl.Tokenizer.isNumber(n)) {
                pn.add(n);
            } else if (NewCalculatorImpl.Tokenizer.isLP(n)) {
                st.push(n);
            } else if (NewCalculatorImpl.Tokenizer.isRP(n)) {
                String lp = st.pop();
                while (!NewCalculatorImpl.Tokenizer.isLP(lp)) {
                    pn.add(lp);
                    lp = st.pop();
                }
            } else if (NewCalculatorImpl.Tokenizer.isOp(n)) {
                try {
                    String op = st.peek();
                    while (st.size() > 0 && NewCalculatorImpl.Tokenizer.isOp(op) && NewCalculatorImpl.Tokenizer.getPriority(n) <= NewCalculatorImpl.Tokenizer.getPriority(op)) {
                        pn.add(st.pop());
                        op = st.peek();
                    }
                } catch (EmptyStackException e) { }
                st.push(n);
            }

            n = tokenizer.next();
        }

        while (!st.empty()) {
            pn.add(st.pop());
        }

        return null;
    }

    private String extractNumber(String statement)
    {
        int start = curPosition;
        for (int i = start; i < statement.length(); i++)
        {
            if (!Character.isDigit(statement.charAt(i)) && statement.charAt(i) != '.')
            {
                curPosition = i;
                break;
            }
        }
        return statement.substring(start, curPosition);
    }




    private static class Tokenizer
    {
        String content;
        int position = 0;

        static final String lparentheses = "(";
        static final String rparentheses = ")";
        static final String priority2Ops = "*/";
        static final String priority1Ops = "+-";
        static final String ops = priority1Ops + priority2Ops;
        static final String signs = ops + lparentheses + rparentheses;
        static final String digits = "0123456789";

        Tokenizer(String content)
        {
            this.content = content;
        }

        /**
         * Check if token is operation
         *
         * @param s token to check
         * @return true if token is operation
         */
        static boolean isOp(String s)
        {
            return s.length() == 1 && ops.indexOf(s) > -1;
        }

        /**
         * Check if token is number
         *
         * @param s token to check
         * @return true if token is number
         */
        static boolean isNumber(String s)
        {
            return signs.indexOf(s) == -1;
        }

        /**
         * Return priority of operation
         *
         * @param op operation token
         * @return priority of operation of it is supported
         */
        static int getPriority(String op)
        {
            if (!isOp(op)) throw new IllegalArgumentException();

            if (priority1Ops.indexOf(op) > -1) return 1;
            if (priority2Ops.indexOf(op) > -1) return 2;

            return 0;
        }

        /**
         * Check if token is left parentheses
         *
         * @param s token
         * @return true if token is left parentheses
         */
        static boolean isLP(String s)
        {
            return s.length() == 1 && lparentheses.indexOf(s) > -1;
        }

        /**
         * Check if token is right parentheses
         *
         * @param s token
         * @return true if token is right parentheses
         */
        static boolean isRP(String s)
        {
            return s.length() == 1 && rparentheses.indexOf(s) > -1;
        }

        /**
         * Know how to operate with operations. Only binary operations are supported
         *
         * @param s1 first operand
         * @param s2 second operand
         * @param op operation
         * @return result of applied operation
         */
        static String compute(String s1, String s2, String op)
        {
            double i1 = Double.parseDouble(s1);
            double i2 = Double.parseDouble(s2);

            if ("*".equals(op))
            {
                return String.format("%.4f", (i2 * i1));
            } else if ("/".equals(op))
            {
                return String.format("%.4f", i2 / i1);
            } else if ("+".equals(op))
            {
                return String.format("%.4f", (i2 + i1));
            } else if ("-".equals(op))
            {
                return String.format("%.4f", (i2 - i1));
            }

            throw new IllegalArgumentException();

        }

        /**
         * @return next token or null
         */
        public String next()
        {
            StringBuilder sb = new StringBuilder();

            while (position < content.length()
                    && signs.indexOf(content.charAt(position)) == -1
                    && digits.indexOf(content.charAt(position)) == -1)
            {
                position++;
            }
            if (position == content.length()) return null;

            if (signs.indexOf(content.charAt(position)) > -1) return Character.toString(content.charAt(position++));

            while (position < content.length() &&
                    signs.indexOf(content.charAt(position)) == -1)
            {
                if (digits.indexOf(content.charAt(position)) > -1) sb.append(content.charAt(position));
                position++;
            }

            return sb.toString();
        }
    }


}
