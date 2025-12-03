package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader
import kotlin.math.absoluteValue

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day2Solver(resourceFileReader)
    solver.solvePart1()
    solver.solvePart2()
}

class Day2Solver(private val resourceFileReader: ResourceFileReader) {

    fun solvePart1() {
        val pairs = parseInput(resourceFileReader)
        val invalidIds = pairs
            .flatMap { pair -> pair.first..pair.second }
            .filter(this::isInvalidForPart1)

        val code = invalidIds.sum()
        println("The code is: $code")
    }

    fun solvePart2() {
        val pairs = parseInput(resourceFileReader)
        val invalidIds = pairs
            .flatMap { pair -> pair.first..pair.second }
            .filter(this::isInvalidForPart2)

        val code = invalidIds.sum()
        println("The code is: $code")
    }

    fun parseInput(resourceFileReader: ResourceFileReader): List<Pair<Long, Long>> {
        val line = resourceFileReader.readLinesFromResource("/day2/input.txt")[0]
        return line.split(",").map { parsePair(it) }
    }

    fun isInvalidForPart1(int: Long): Boolean {
        val stringValue = int.toString()
        val length = stringValue.length
        val firstHalf = stringValue.substring(0, length / 2)
        val secondHalf = stringValue.substring(length / 2, length)
        return firstHalf == secondHalf
    }

    fun isInvalidForPart2(int: Long): Boolean {
        val possibleSequenceLengths = getPossibleRepeatedSequenceLengths(int)
        for (i in possibleSequenceLengths) {
            val parts = splitInParts(int.toString(), i)
            val firstPart = parts[0]
            var allPartsMatch = true
            for (part in parts) {
                if (part != firstPart) {
                    allPartsMatch = false
                    break
                }
            }
            if (allPartsMatch) return true
        }
        return false
    }

    private fun splitInParts(string: String, partSize: Int) : List<String> {
        val subStrings = ArrayList<String>()
        var currentIndex = 0
        while (currentIndex < string.length) {
            subStrings.add(string.substring(currentIndex, currentIndex + partSize))
            currentIndex += partSize
        }
        return subStrings
    }

    fun getPossibleRepeatedSequenceLengths(int: Long): ArrayList<Int> {
        val stringValue = int.toString()
        val length = stringValue.length
        val possibleSequenceLengths = ArrayList<Int>()
        for (i in 1..length / 2) {
            val divided = length / i.toFloat()
            val dividedInt = divided.toInt()
            if ((divided - dividedInt).absoluteValue < 0.001) {
                possibleSequenceLengths.add(i)
            }
        }
        return possibleSequenceLengths
    }

    fun parsePair(string: String): Pair<Long, Long> {
        val split = string.split("-")
        return Pair(split[0].toLong(), split[1].toLong())
    }
}