package com.tsystems.javaschool.tasks;

import java.text.ParseException;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by Kolia on 17.01.2015.
 *
 * CalculatorImpl uses implementation of Interpreter design pattern
 * and Tokenizer class to get next token from the input string.
 *
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
        //translate to Reverse Polish Notation
        try
        {
            statement = toReversePolishNotation(statement);
        }
        catch (ParseException e)
        {
            return null;
        }

        //interpreting tokens we got
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

        //rounding and formatting result
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


    private static String toReversePolishNotation(String statement) throws ParseException
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
                    //error in the token sequence
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
                //unsupported token
                throw new ParseException("Can't parse " + statement, statement.indexOf(str));
            }
        }

        while (!stack.empty())
        {
            rnp.append(stack.pop()).append(" ");
        }

        return rnp.toString();
    }
}
