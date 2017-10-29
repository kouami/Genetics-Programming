package com.genetics.programming.gpgraphics;

/**
 *
 * @author Emmanuel
 */
public class TargetFunction implements Function {

    @Override
    public double apply(double x) {
        return (x * x - 1) / 2;
    }

}
