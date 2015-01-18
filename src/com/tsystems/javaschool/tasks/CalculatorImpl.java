package com.tsystems.javaschool.tasks;

import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by Kolia on 17.01.2015.
 */
public class CalculatorImpl implements Calculator
{
    public static void main(String[] args)
    {
        Calculator c = new CalculatorImpl();
        System.out.println(c.evaluate("(1+38)*4-5"));
    }

    @Override
    public String evaluate(String statement)
    {
        try
        {
            statement = toReversePolishNotation(statement);
        }
        catch (ParseException e)
        {
            return null;
        }

        Stack<IExpression> stack = new Stack<IExpression>();

        String[] tokenList = statement.split(" ");
        for (String s : tokenList)
        {
            if (Tokenizer.isOperation(s))
            {
                IExpression rightExpression = stack.pop();
                IExpression leftExpression = stack.isEmpty() ? null : stack.pop(); //support of unary + and -
                IExpression operator = getOperatorInstance(s, leftExpression, rightExpression);
                double result = operator.interpret();
                stack.push(new NumberExpression(result));
            }
            else
            {
                IExpression i = new NumberExpression(s);
                stack.push(i);
            }
        }

        double value = stack.pop().interpret();
        double roundedValue = (double)Math.round(value * 10000) / 10000;
        return String.format("%.4f", roundedValue);
    }


    private static IExpression getOperatorInstance(String s, IExpression left, IExpression right)
    {
        char c = s.charAt(0);
        switch (c)
        {
            case '+':
                return new PlusExpression(left, right);
            case '-':
                return new MinusExpression(left, right);
            case '*':
                return new MultiplyExpression(left, right);
            case '/':
                return new DivideExpression(left, right);
        }
        return null;
    }


    private String toReversePolishNotation(String statement) throws ParseException
    {
        Tokenizer tokenizer = new Tokenizer(statement);

        StringBuilder rnp = new StringBuilder();
        Stack<String> stack = new Stack<String>();

        String str;
        while ((str = tokenizer.getNextToken()) != null)
        {
            if (Tokenizer.isNumber(str))
            {
                rnp.append(str).append(" ");
            }
            else if (Tokenizer.isLeftParentheses(str))
            {
                stack.push(str);
            }
            else if (Tokenizer.isRightParentheses(str))
            {
                try
                {
                    String lp = stack.pop();
                    while (!Tokenizer.isLeftParentheses(lp))
                    {
                        rnp.append(lp).append(" ");
                        lp = stack.pop();
                    }
                }
                catch (EmptyStackException e)
                {
                    throw new ParseException("Can't parse " + statement, statement.indexOf(str));
                }
            }
            else if (Tokenizer.isOperation(str))
            {
                while (!stack.isEmpty())
                {
                    String op = stack.peek();
                    if (Tokenizer.isOperation(op) && Tokenizer.getPriority(str) <= Tokenizer.getPriority(op))
                        rnp.append(stack.pop()).append(" ");
                    else
                        break;
                }

                stack.push(str);
            }
            else
            {
                throw new ParseException("Can't parse " + statement, statement.indexOf(str));
            }
        }

        while (!stack.empty())
        {
            rnp.append(stack.pop()).append(" ");
        }

        return rnp.toString();
    }


    private static class Tokenizer
    {
        static final char decimalDelimiter = '.';
        static final String leftParentheses = "(";
        static final String rightParentheses = ")";
        static final String priority2_Ops = "*/";
        static final String priority1_Ops = "+-";
        static final String operations = priority1_Ops + priority2_Ops;
        static final String signs = operations + leftParentheses + rightParentheses;

        String content;
        int position = 0;

        Tokenizer(String content)
        {
            this.content = content;
        }

        public String getNextToken()
        {
            StringBuilder sb = new StringBuilder();

            if (position == content.length())
                return null;

            char currentChar = content.charAt(position);
            if (isSign(currentChar))
            {
                position++;
                return Character.toString(currentChar);
            }
            else if (isDigitOrDelimiter(currentChar))
            {
                while (position < content.length() && isDigitOrDelimiter(currentChar))
                {
                    sb.append(currentChar);
                    position++;
                    if (position < content.length())
                        currentChar = content.charAt(position);
                }
            }
            else
            {
                sb.append(currentChar);
                position++;
            }

            return sb.toString();
        }

        static boolean isSign(char c)
        {
            return signs.indexOf(c) != -1;
        }

        static boolean isDigitOrDelimiter(char c)
        {
            return Character.isDigit(c) || c == decimalDelimiter;
        }

        static boolean isOperation(String s)
        {
            return s.length() == 1 && operations.contains(s);
        }

        static boolean isNumber(String s)
        {
            try
            {
                Double.parseDouble(s);
                return true;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }

        static boolean isLeftParentheses(String s)
        {
            return s.length() == 1 && leftParentheses.contains(s);
        }

        static boolean isRightParentheses(String s)
        {
            return s.length() == 1 && rightParentheses.contains(s);
        }

        static int getPriority(String op)
        {
            if (!isOperation(op)) throw new IllegalArgumentException();

            if (priority1_Ops.contains(op)) return 1;
            if (priority2_Ops.contains(op)) return 2;

            return 0;
        }
    }
}
