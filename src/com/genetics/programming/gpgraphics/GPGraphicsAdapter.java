/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genetics.programming.gpgraphics;
import com.genetics.programming.*;

import java.util.List;
import com.genetics.programming.utilities.GPUtils;
/**
 *
 * @author ekakoll
 */
public class GPGraphicsAdapter extends GP {

    private String resultText = "";


    public GPGraphicsAdapter() {
        super();
    }

    public void initGPGraphicsAdapter() {
        initPopulation();
    }

    public Individual mutate(Individual candidate) {
        return super.mutate(candidate);
    }


    public List<Individual> newBornChilds(Individual father, Individual mother) {
        return newBornChild(father, mother);
    }


    public void nextGeneration() {
        super.nextGeneration();
    }

    public void unfit() {
        super.unfit();
    }


    public void addMutants() {
        super.addMutants();
    }

    public void addCrossedOver() {
        super.addCrossedOver();
    }


    public void addNew() {
        super.addNew();
    }

    public void printSolutions(int generations) {
        super.printSolutions(generations);
    }

    public String printSolution(int generation) {


        if (getSolutions().size() > 0) {
            Individual ind = getSolutions().get(0);
            StringBuilder builder = new StringBuilder();
            GPUtils.printInorderToString(ind.getRootNode(), builder);
            resultText = "Solution :: " + builder.toString();

            System.out.println(resultText);
        } else {
            resultText = "No Solution was found in the time allocated... ";
        }
        return resultText;
        //return gp.printSolutions(generation);
    }

    public static List<Individual> getSolutions() {
        return GPUtils.getSolutions();
    }

    public List<Individual> getPopulation() {
        return GPUtils.getPopulation();
    }

    public static Individual extractSolution() {
        return getSolutions().get(0);
    }
}
