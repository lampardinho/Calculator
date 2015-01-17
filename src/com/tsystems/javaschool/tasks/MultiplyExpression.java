package com.tsystems.javaschool.tasks;

/**
 * Created by Kolia on 17.01.2015.
 */
public class MultiplyExpression implements IExpression
{

    IExpression leftExpression;
    IExpression rightExpresion;

    public MultiplyExpression(IExpression leftExpression, IExpression rightExpression)
    {
        this.leftExpression = leftExpression;
        this.rightExpresion = rightExpression;
    }

    @Override
    public double interpret()
    {
        return leftExpression.interpret() * rightExpresion.interpret();
    }

}

