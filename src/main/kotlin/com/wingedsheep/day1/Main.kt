package com.wingedsheep.day1

import com.wingedsheep.util.ResourceFileReader

fun main() {
    val resourceFileReader = ResourceFileReader()
    val inputParser = InputParser(resourceFileReader)
    val algorithm = Day1Algorithm()

    val rotations1 = inputParser.parseInput("/day1/input.txt")
    val code1 = algorithm.crackCode1(rotations1)
    println("Code 1 is: $code1")

    val rotations2 = inputParser.parseInput("/day1/input2.txt")
    val code2 = algorithm.crackCode2(rotations2)
    println("Code 2 is: $code2")
}
