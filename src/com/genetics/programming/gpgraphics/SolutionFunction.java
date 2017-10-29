/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genetics.programming.gpgraphics;

import com.genetics.programming.utilities.GPUtils;

/**
 *
 * @author Emmanuel
 */
public class SolutionFunction implements Function {
    @Override
    public double apply(double x) {
        return GPUtils.evaluate(GPGraphicsAdapter.extractSolution().getRootNode(), x);
    }
}
