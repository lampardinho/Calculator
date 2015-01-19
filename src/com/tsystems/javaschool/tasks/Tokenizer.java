package com.tsystems.javaschool.tasks;

/**
 * Created by Kolia on 19.01.2015.
 */
public class Tokenizer
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
