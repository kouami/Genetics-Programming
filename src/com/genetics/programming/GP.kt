/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genetics.programming

import java.util.Random
import com.genetics.programming.utilities.*

/*

  @author u329022
  Created on Oct 24, 2017
*/

open class GP {

    /**
     * The population involved in the tournament
     */
    private val POPULATION_SIZE = 100

    /**
     * This is the chance that 2 chromosomes will mate to create 2 new children
     */
    private val CROSSED_OVER = POPULATION_SIZE * 10 / 100

    /**
     * This is the chance that a chromosome in the candidate will be subject of
     * a mutation
     */
    private val MUTATED = POPULATION_SIZE * 10 / 100

    /**
     * Percentage of population to drop at each iteration
     */
    private val DROPPED = POPULATION_SIZE * 40 / 100

    /**
     * Percentage of population that are randomly generated
     */
    private val NEW = POPULATION_SIZE * 10 / 100

    //var population = ArrayList<Individual>()
    //var solutions = ArrayList<Individual>()


    private var rand: Random
    private var firstCand = 0
    private var secondCand = 0
    private var thirdCand = 0
    private var fourthCand = 0

    constructor() {
        rand = Random()
        for (i: Int in 0 until POPULATION_SIZE) {
            var c = Individual()
            population.add(c)
        }
        population.sortedWith(compareBy({ it.getNetFitness() }))
    }

    public fun initPopulation() {
        population.clear()
        rand = Random()
        for (i: Int in 0 until POPULATION_SIZE) {
            var c = Individual()
            population.add(c)
        }

        population.sortedWith(compareBy({ it.getNetFitness() }))

    }

     open fun mutate(candidate: Individual): Individual {
        var levelOfMutation = Random().nextInt(3)
        var peek: Int
        if (levelOfMutation == 0) {
            // terminal set level
            peek = Random().nextInt(8)
            var leaves = candidate.leafNodes
            leaves[peek]?.data = TERMINAL_SET[terminalRand.nextInt(TERMINAL_SET.size)]
        } else if (levelOfMutation == 1) {
            // first level of op functions
            peek = Random().nextInt(4)
            var firstLevOpNodes = candidate.firstLevelOpNodes
            firstLevOpNodes[peek]?.data = FUNCTION_SET[functionRand.nextInt(FUNCTION_SET.size)]

        } else {
            // second level of op functions
            peek = Random().nextInt(2)
            var secondLevOpNodes = candidate.secondLevelOpNodes
            secondLevOpNodes[peek]?.data = FUNCTION_SET[functionRand.nextInt(FUNCTION_SET.size)]

        }
        return candidate
    }

    // One point crossover
     fun newBornChild(father: Individual, mother: Individual): ArrayList<Individual> {

        var candidates = ArrayList<Individual>()

        var fatherSubtrees = father.secondLevelOpNodes
        var motherSubtrees = mother.secondLevelOpNodes

        var fatherSubtree = fatherSubtrees[1]
        var motherSubtree = motherSubtrees[0]

        father.rootNode.right = motherSubtree
        motherSubtree?.parent = father.rootNode

        mother.rootNode.left = fatherSubtree
        fatherSubtree?.parent = mother.rootNode

        var child1 = father
        var child2 = mother

        candidates.add(child1)
        candidates.add(child2)
        return candidates
    }

    fun playGod() {

        var count = 0
        var begin = System.currentTimeMillis()
        var duration = 0L

        var noSolutionFound = true
        while ((duration <= 600000) && noSolutionFound) {
            // Stop simulation if a solution was found or we reached 10 minute
            // of simulation
            nextGeneration()

            count++

            //println("Generation #  $count")
            for (i: Int in 0 until population.size) {
                if (population[i].getNetFitness() <= 0.01) {
                    solutions.add(population[i])
                    noSolutionFound = false
                }
            }
            duration = System.currentTimeMillis() - begin
        }
        printSolutions(count)
        println("Size of Population :: " + population.size)
    }

    /**
     * For the selection strategy, we will randomly select 4 distinct candidates
     * in the population; pair them up and let them fight. The winners (2
     * winners in each fight) will mate to give birth to 2 new children.
     *
     */
    open fun nextGeneration() {

        unfit()
        addMutants()
        addCrossedOver()
        addNew()
        population.sortedWith(compareBy({ it.getNetFitness() }))

    }

    open fun unfit() {
        for (i: Int in population.size - 1 downTo (DROPPED + 1) step 1) {
            population.removeAt(i)
        }
    }

    open fun addMutants() {
        for (i: Int in 0 until MUTATED) {
            var mut = rand.nextInt(population.size - DROPPED)
            var c = mutate(population[mut])
            population.add(c)
        }
    }

    open fun addCrossedOver() {
        var size = population.size

        for (i in 0 until (size - DROPPED) step 2) {
            while (secondCand == firstCand) {
                secondCand = rand.nextInt(size)
            }
            while (thirdCand == firstCand || thirdCand == secondCand) {
                thirdCand = rand.nextInt(size)
            }
            while (fourthCand == firstCand || fourthCand == secondCand || fourthCand == thirdCand) {
                fourthCand = rand.nextInt(size)
            }

            var c1 = population[firstCand]
            var c2 = population[secondCand]
            var c3 = population[thirdCand]
            var c4 = population[fourthCand]


            var firstCandidateFitness = c1.getNetFitness()
            var secondCandidateFitness = c2.getNetFitness()
            var thirdCandidateFitness = c3.getNetFitness()
            var fourthCandidateFitness = c4.getNetFitness()

            var winner1 = if (firstCandidateFitness > secondCandidateFitness) c1 else c2
            var winner2 = if (thirdCandidateFitness > fourthCandidateFitness) c3 else c4

            var newBorns = newBornChild(winner1, winner2)
            population.addAll(newBorns)
        }
    }

    open fun addNew() {

        for (i in 0 until DROPPED) {
            population.add(Individual())
        }
    }

    open fun printSolutions(generation: Int) {


        //println("The Individual is :: " + solutions[0].toString())
        for (c: Individual in solutions) {

            printInorder(c.rootNode)
            print("\tFitness :: " + c.getNetFitness())
            println()
        }
        if (solutions.size > 0) {

            println("The Solution was found in generation :: " + generation)
        } else {
            println("No Solution was found in the time allocated... ")
        }
    }

}

fun main(args: Array<String>) {

    var start = System.currentTimeMillis()
    var ga = GP()
    ga.initPopulation()
    ga.playGod()
    println("The Duration of the run is :: " + (System.currentTimeMillis() - start) / 1000 / 60 + " minutes")

}