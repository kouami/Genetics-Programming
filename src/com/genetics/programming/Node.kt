/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genetics.programming

/*

  @author Emmanuel Akolly
  Created on Oct 24, 2017
*/

data class Node(var data: String, var left: Node?=null, var right: Node?=null, var parent: Node?=null) {
    
    /**
     * @return
     * <code>true</code> if the node is a root node and
     * <code>false</code> otherwise
     *
     */
    fun isRootNode() : Boolean = this.parent == null
    
    /**
     * @return
     * <code>true</code> if the node is a leaf node and
     * <code>false</code> otherwise
     *
     */
    fun isLeafNode() : Boolean = (this.left == null) && (this.right == null) 
    
    /**
     * @return
     * <code>true</code> if the node carries an operator in the function set and
     * <code>false</code> otherwise
     *
     */   
    fun isFunctionNode() : Boolean = !isTerminalNode()
    
    /**
     * @return
     * <code>true</code> if the node carries a constant in the terminal set and
     * <code>false</code> otherwise
     *
     */    
    fun isTerminalNode() : Boolean = (Character.isDigit(this.data.elementAt(0)) || data.equals("X"))
    
}