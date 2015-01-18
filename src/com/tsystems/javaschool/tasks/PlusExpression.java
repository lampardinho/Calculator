package com.tsystems.javaschool.tasks;

/**
 * Created by Kolia on 17.01.2015.
 */
public class PlusExpression implements IExpression
{
    IExpression leftExpression;
    IExpression rightExpression;

    public PlusExpression(IExpression leftExpression, IExpression rightExpression)
    {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public double interpret()
    {
        if (leftExpression == null)
            return rightExpression.interpret();
        
        return leftExpression.interpret() + rightExpression.interpret();
    }

}

