/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

@file:JvmName("GPUtils")
package com.genetics.programming.utilities

import com.genetics.programming.Individual
import com.genetics.programming.Node
import com.genetics.programming.Operation
import java.util.Stack
import java.util.Random

/*

  @author Emmanuel Akolly
  Created on Oct 24, 2017
*/

val TERMINAL_SET = arrayOf("0", "1", "2", "3", "4", "X", "6", "7", "8", "9", "5")
val FUNCTION_SET = arrayOf("+", "-", "*", "/")

val terminalRand: Random = Random()
val functionRand: Random = Random()

var population = ArrayList<Individual>()
var solutions = ArrayList<Individual>()

var evaluator: Stack<String> = Stack()

/**
 * This method returns the height of a given tree provided that the root
 * node of the tree is supplied to it as an argument.
 *
 * @param node -the root node of the tree
 * @return the height of the tree whose root node is node.
 */
fun getHeight(node: Node? = null): Int {

    val heightLeft = getHeight(node?.left)
    val heightRight = getHeight(node?.right)

    when (heightLeft > heightRight) {
        true -> {
            return heightLeft + 1
        }
        false -> return heightRight + 1
    }
}

/**
 * This is the integral of the function (x * x - 1)/2 This will be used to
 * compute the fitness of each candidate which is the area between the
 * candidate function and the above solution function. The less the area
 * will be, the better the candidate is. So, the termination condition is:
 * Math.abs(integral of candidate - solutionIntegral) < 0.01 NB: The
 * integral will be performed in an interval of [-100,100] along the x-axis
 * Returns an approximation of the integral of f(x) from start to stop,
 * using the midpoint rule. The function f is defined by the evaluate method
 * of the Evaluatable object.
 */
fun evaluateIntegral(root: Node, start: Double, stop: Double, numSteps: Int): Double {
    var startInterval: Double = start
    val stepSize = (stop - startInterval) / numSteps
    startInterval += stepSize / 2.0
    return (stepSize * sum(root, startInterval, stop, stepSize))
}

/**
 * Returns the sum of f(x) from x=start to x=stop, where the function f is
 * defined by a tree that can be evaluated.
 */
fun sum(root: Node, start: Double, stop: Double, stepSize: Double): Double {
    var sum = 0.0
    var current: Double = start
    while (current <= stop) {
        sum += evaluate(root, current) - subEvalStr(java.lang.Double.toString(subEvalStr(java.lang.Double.toString(current), Operation.MUL, java.lang.Double.toString(current))), Operation.SUB, "1") / 2
        current += stepSize
    }
    return (sum)
}

/**
 * Evaluates a tree that is pushed on the stack
 *
 * @param root -the root node of the tree
 * @param x -the variable that will replace a terminal set value should it
 * be X.
 * @return the value represented by the tree
 *
 */
fun evaluate(root: Node, x: Double): Double {

    var ret = 0.0
    var operand1: Double?
    var operand2: Double?

    evaluator.clear()
    evaluator.push(root.data)
    evaluator.push(root.left?.data)
    evaluator.push(root.left?.left?.data)

    if (root.left?.left?.left?.data.equals("X")) {
        operand1 = x
    } else {
        operand1 = root.left?.left?.left?.data?.toDoubleOrNull()
    }

    if (root.left?.left?.right?.data.equals("X")) {
        operand2 = x
    } else {
        operand2 = root.left?.left?.right?.data?.toDoubleOrNull()
    }

    var op = evaluator.pop()
    ret += subEval(operand1, op, operand2)
    evaluator.push(root.left?.right?.data)

    when {
        (root.left?.right?.left?.data.equals("X")) -> operand1 = x
        else -> operand1 = root.left?.right?.left?.data?.toDoubleOrNull()
    }

    when (root.left?.right?.right?.data.equals("X")) {
        true -> operand2 = x
        false -> operand2 = root.left?.right?.right?.data?.toDoubleOrNull()
    }

    op = evaluator.pop()
    var temp: Double = subEval(operand1, op, operand2)
    op = evaluator.pop()
    ret = subEval(ret, op, temp) // left subtree evaluated
    val result1: Double = ret


    evaluator.push(root.right?.data)
    evaluator.push(root.right?.left?.data)

    when (root.right?.left?.left?.data.equals("X")) {
        true -> operand1 = x
        false -> operand1 = root.right?.left?.left?.data?.toDoubleOrNull()
    }

    if (root.right?.left?.right?.data.equals("X")) {
        operand2 = x
    } else {
        operand2 = root.right?.left?.right?.data?.toDoubleOrNull()
    }

    op = evaluator.pop()
    ret = subEval(operand1, op, operand2)

    evaluator.push(root.right?.right?.data)

    if (root.right?.right?.left?.data.equals("X")) {
        operand1 = x
    } else {
        operand1 = root.right?.right?.left?.data?.toDoubleOrNull()
    }

    if (root.right?.right?.right?.data.equals("X")) {
        operand2 = x
    } else {
        operand2 = root.right?.right?.right?.data?.toDoubleOrNull()
    }

    op = evaluator.pop()
    temp = subEval(operand1, op, operand2)
    op = evaluator.pop()
    ret = subEval(ret, op, temp) // right subtree evaluate;
    val result2: Double = ret
    op = evaluator.pop()  // pop the root node finally

    ret = subEval(result1, op, result2)

    return ret
}

/**
 *
 *
 * @param op1 -the left operand
 * @param op -the operator
 * @param op2 -the right operand
 * @return the result of the evaluation of this expression
 */
fun subEvalStr(op1: String, op: Operation, op2: String): Double {
    var retVal = 0.0

    when (op) {

        Operation.ADD -> retVal = (java.lang.Double.parseDouble(op1) + java.lang.Double.parseDouble(op2))
        Operation.SUB -> retVal = (java.lang.Double.parseDouble(op1) - java.lang.Double.parseDouble(op2))
        Operation.MUL -> retVal = (java.lang.Double.parseDouble(op1) * java.lang.Double.parseDouble(op2))

        Operation.DIV -> {
            var temp: Double = java.lang.Double.parseDouble(op2)
            if (java.lang.Double.parseDouble(op2) <= 0.001) {
                temp = 1.0
            } else {
                temp = temp
            }
            retVal = (java.lang.Double.parseDouble(op1) / temp)
        }
    }
    return retVal
}

/**
 *
 * @param op1 -the left operand
 * @param op -the operator
 * @param op2 -the right operand
 * @return the result of the evaluation of this expression
 */
fun subEval(op1: Double?, op: String, op2: Double?): Double {
    var retVal = 0.0
    var operation1: Double = if (op1 == null) 0.0 else op1
    var operation2 = if (op2 == null) 0.0 else op2


    when (op.elementAt(0)) {

        '+' -> retVal = operation1 + operation2
        '-' -> retVal = operation1 - operation2
        '*' -> retVal = operation1 * operation2
        '/' -> {
            if (operation2 <= 0.001) {
                operation2 = 1.0
            } else {
                operation2 = operation2
            }
            retVal = operation1 / operation2
        }
    }
    return retVal
}

/**
 * Prints a tree in a format that is readable to the human eye
 *
 * @param node -the root node of the tree to be printed
 */
fun printInorder(node: Node? ) {

    if(node != null) {
        print(" (")
        printInorder(node?.left)
        print(node?.data)
        printInorder(node?.right)
        print(") ")
    }

}

/**
 * This is an altered version of the printInorder method
 *
 * @param node -the root node of the tree to be printed
 * @return a String representation of a tree
 */
fun printInorderToString(node: Node? = null, builder: StringBuilder) {

    if(node != null) {
        builder.append(" (")
        printInorderToString(node?.left, builder)
        builder.append(node?.data)
        printInorderToString(node?.right, builder)
        builder.append(") ")
    }
}
