/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.genetics.programming
import com.genetics.programming.utilities.*
/*

  @author u329022
  Created on Oct 25, 2017
*/


fun main(args: Array<String>) {

    var start = System.currentTimeMillis()
    var ga = GP()
    ga.initPopulation()
    ga.playGod()
    println("The Duration of the run is :: " + (System.currentTimeMillis() - start) / 1000 / 60 + " minutes")


}

