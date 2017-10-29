/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genetics.programming

import java.util.Random
import com.genetics.programming.utilities.*

/*

  @author Emmanuel Akolly
  Created on Oct 24, 2017
*/

open class Individual : Comparable<Individual> {

    var rootNode =  Node("")
    var firstLevelOpNodes = arrayOfNulls<Node>(4)
    var secondLevelOpNodes = arrayOfNulls<Node>(2)
    var leafNodes = arrayOfNulls<Node>(8)
   


    /**
     * This method generates a tree.
     * To build this tree, we are making sure that we are building a tree
     * of height 4 all the time.
     * From the bottom of the tree up:
     *                --we have constants as leaves
     *                --One level up (firstLevelOpNodes) we have 4 operators
     *                --One level up (secondLevelOpNodes) we have 2 operators
     *                --Finally we have the root node
     */
    constructor() {
        this.rootNode = Node(FUNCTION_SET[functionRand.nextInt(FUNCTION_SET.size)])

        for (i: Int in 0 until leafNodes.size )
            leafNodes[i] = Node(TERMINAL_SET[terminalRand.nextInt(TERMINAL_SET.size)])

        for (i: Int in 0 until firstLevelOpNodes.size ) {
            firstLevelOpNodes[i] = Node(FUNCTION_SET[functionRand.nextInt(FUNCTION_SET.size)])
            firstLevelOpNodes[i]?.left = leafNodes[2 * i]
            leafNodes[2 * i]?.parent = firstLevelOpNodes[i]
            firstLevelOpNodes[i]?.right = leafNodes[(2 * i) + 1]
            leafNodes[(2 * i) + 1]?.parent = firstLevelOpNodes[i]
        }

        for (i: Int in 0 until secondLevelOpNodes.size ) {
            secondLevelOpNodes[i] = Node(FUNCTION_SET[functionRand.nextInt(FUNCTION_SET.size)])
            secondLevelOpNodes[i]?.left = firstLevelOpNodes[2 * i]
            firstLevelOpNodes[2 * i]?.parent = secondLevelOpNodes[i]
            secondLevelOpNodes[i]?.right = firstLevelOpNodes[(2 * i) + 1]
            firstLevelOpNodes[(2 * i) + 1]?.parent = secondLevelOpNodes[i]
        }

        rootNode.left = secondLevelOpNodes[0]
        secondLevelOpNodes[0]?.parent = rootNode
        rootNode.right = secondLevelOpNodes[1]
        secondLevelOpNodes[1]?.parent = rootNode
    }

    /**
     * @return -the area between this candidate and the solution (X^2 - 1)/2
     *          over an interval of [-50,50] on the x-axis.
     * The lower the area, the better the fitness and the stronger the candidate
     * To evaluate this area we are computing the integral of each individual
     * The integral of this individual is computed using the midpoint rule as
     * java lacks any built-in function to do so.
     */
    fun getNetFitness(): Double {
        val integral: Double = evaluateIntegral(rootNode, -100.0, 100.0, 5)        
        return Math.abs(integral)
    }

    /**
     * This method needs to be implemented in order for each individual to be
     * sorted in a collection.
     *
     */
    override fun compareTo(other: Individual): Int {
        val f1: Double = this.getNetFitness()
        val f2: Double = this.getNetFitness()
        if (f1 > f2) {
            return 1
        } else if (f1 < f2) {
            return -1
        } else {
            return 0
        }
    }

    override fun toString(): String {
        var builder = StringBuilder()
        builder.append("Data :: " + this.rootNode.data)
        var flOp: String = ""
        for(fln:Node? in firstLevelOpNodes) {
            flOp += fln?.data + " "
        }
        builder.append("\nFirstLevelOpNodes :: " + flOp)

        var slOp:String = ""
        for(sln:Node? in secondLevelOpNodes) {
            slOp += sln?.data + " "
        }
        builder.append("\nSecondLevelOpNodes :: " + slOp)

        var lnd:String = ""
        for(ln:Node? in leafNodes) {
            lnd += ln?.data + " "
        }
        builder.append("\nLeafNodes :: " + lnd)
        return builder.toString()
    }
    
}