package com.tsystems.javaschool.tasks;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

public class CalculatorImpl implements Calculator {

    public static String input = "1/3";
    private CalculatorImpl.Tokenizer tokenizer = new Tokenizer(input);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Calculator c = new CalculatorImpl();        
        System.out.println(c.evaluate(input));

    }

    

    @Override
    public String evaluate(String str) {

        /**
         * First we translate to reverse-polish notation
         */
        String n = tokenizer.next();

        LinkedList<String> pn = new LinkedList<String>();
        Stack<String> st = new Stack<String>();

        while (n != null) {
            if (CalculatorImpl.Tokenizer.isNumber(n)) {
                pn.add(n);
            } else if (CalculatorImpl.Tokenizer.isLP(n)) {
                st.push(n);
            } else if (CalculatorImpl.Tokenizer.isRP(n)) {
                String lp = st.pop();
                while (!CalculatorImpl.Tokenizer.isLP(lp)) {
                    pn.add(lp);
                    lp = st.pop();
                }
            } else if (CalculatorImpl.Tokenizer.isOp(n)) {
                try {
                    String op = st.peek();
                    while (st.size() > 0 && CalculatorImpl.Tokenizer.isOp(op) && CalculatorImpl.Tokenizer.getPriority(n) <= CalculatorImpl.Tokenizer.getPriority(op)) {
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

        /**
         * And then - compute
         */
        try {
        while (pn.size() > 0) {
            String next = pn.removeFirst();
            if (CalculatorImpl.Tokenizer.isOp(next)) {
                st.push(CalculatorImpl.Tokenizer.compute(st.pop(), st.pop(), next));
            } else {
                st.push(next);
            }
        }

        return st.pop();
        } catch (EmptyStackException e) {return null;}
    }

    /**
     * Includes tokenizer implementation, operations logic
     */
    private static class Tokenizer {
        String content;
        int position = 0;

        static final String lparentheses = "(";
        static final String rparentheses = ")";
        static final String priority2Ops = "*/";
        static final String priority1Ops = "+-";
        static final String ops = priority1Ops + priority2Ops;
        static final String signs = ops + lparentheses + rparentheses;
        static final String digits = "0123456789";

        Tokenizer(String content) {
            this.content = content;
        }

        /**
         * Check if token is operation
         * @param s token to check
         * @return true if token is operation
         */
        static boolean isOp(String s) {
            return s.length() == 1 && ops.indexOf(s) > -1;
        }

        /**
         * Check if token is number
         * @param s token to check
         * @return true if token is number
         */
        static boolean isNumber(String s) {
            return signs.indexOf(s) == -1;
        }

        /**
         * Return priority of operation
         * @param op operation token
         * @return priority of operation of it is supported
         */
        static int getPriority(String op) {
            if (!isOp(op)) throw new IllegalArgumentException();

            if (priority1Ops.indexOf(op) > -1) return 1;
            if (priority2Ops.indexOf(op) > -1) return 2;

            return 0;
        }

        /**
         * Check if token is left parentheses
         * @param s token
         * @return true if token is left parentheses
         */
        static boolean isLP(String s) {
            return s.length() == 1 && lparentheses.indexOf(s) > -1;
        }

        /**
         * Check if token is right parentheses
         * @param s token
         * @return true if token is right parentheses
         */
        static boolean isRP(String s) {
            return s.length() == 1 && rparentheses.indexOf(s) > -1;
        }

        /**
         * Know how to operate with operations. Only binary operations are supported
         * @param s1 first operand
         * @param s2 second operand
         * @param op operation
         * @return result of applied operation
         */
        static String compute(String s1, String s2, String op) {
            double i1 = Double.parseDouble(s1);
            double i2 = Double.parseDouble(s2);
            
            if ("*".equals(op)) {
                return String.format("%.4f",(i2 * i1));
            } else if ("/".equals(op)) {
                return String.format("%.4f", i2 / i1);
            } else if ("+".equals(op)) {
                return String.format("%.4f",(i2 + i1));
            } else if ("-".equals(op)) {
                return String.format("%.4f",(i2 - i1));
            }

            throw new IllegalArgumentException();

        }

        /**
         * @return next token or null
         */
        public String next() {
            StringBuilder sb = new StringBuilder();

            while (position < content.length()
                    && signs.indexOf(content.charAt(position)) == -1
                    && digits.indexOf(content.charAt(position)) == -1) {
                position++;
            }
            if (position == content.length()) return null;

            if (signs.indexOf(content.charAt(position)) > -1) return Character.toString(content.charAt(position++));

            while (position < content.length() &&
                    signs.indexOf(content.charAt(position)) == -1) {
                if (digits.indexOf(content.charAt(position)) > -1) sb.append(content.charAt(position));
                position++;
            }

            return sb.toString();
        }
    }
}

