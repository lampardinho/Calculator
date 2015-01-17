package com.tsystems.javaschool.tasks;

/**
 * Created by Kolia on 17.01.2015.
 */
public class NumberExpression implements IExpression
{
    double number;

    public NumberExpression(double i) {
        number = i;
    }

    public NumberExpression(String s) {
        number = Double.parseDouble(s);
    }

    @Override
    public double interpret() {
        return number;
    }

}
