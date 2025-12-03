package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day3Solver(resourceFileReader)
    solver.solvePart1()
}

class Day3Solver(val resourceFileReader: ResourceFileReader) {

    fun solvePart1() {
        val batteryBanks = parseFile()

        val totalOutputJoltage = batteryBanks.sumOf { toMaxJoltage(it, 2) }
        println("Total output Joltage: $totalOutputJoltage")

        val totalOutputJoltageFor12 = batteryBanks.sumOf { toMaxJoltage(it, 12) }
        println("Total output Joltage for 12: $totalOutputJoltageFor12")
    }

    private fun parseFile(): List<List<Int>> =
        resourceFileReader.readLinesFromResource("/day3/input.txt")
            .map(this::toIntList)


    private fun toIntList(inputLine: String): List<Int> =
        inputLine.map { char -> Integer.parseInt(char.toString()) }

    private fun toMaxJoltage(numbers: List<Int>, numberOfBatteriesToUse: Int): Long {
        val pickedNumbers = ArrayList<Int>()
        var indexOfLastNumberPicked = -1

        (1..numberOfBatteriesToUse).forEach { _ ->
            val numbersRemaining = numberOfBatteriesToUse - pickedNumbers.size
            val bestNumberWithIndex = findBestNumberWithIndex(
                numbers = numbers,
                numbersRemaining = numbersRemaining,
                indexOfLastNumberPicked = indexOfLastNumberPicked)
            pickedNumbers.add(bestNumberWithIndex.number)
            indexOfLastNumberPicked = bestNumberWithIndex.index
        }
        return pickedNumbers.fold("", { acc, number -> acc + number }).toLong()
    }

    private fun findBestNumberWithIndex(numbers: List<Int>, numbersRemaining: Int, indexOfLastNumberPicked: Int): NumberWithIndex {
        val indexToExclusiveInReversedArray = numbers.size - indexOfLastNumberPicked - 1
        val bestNumber = numbers
            .reversed()
            .subList(numbersRemaining - 1, indexToExclusiveInReversedArray)
            .max()

        val searchBestNumberIndexFrom = indexOfLastNumberPicked + 1
        val bestNumberIndex = numbers.subList(searchBestNumberIndexFrom, numbers.size).indexOf(bestNumber) + searchBestNumberIndexFrom

        return NumberWithIndex(
            number = bestNumber,
            index = bestNumberIndex
        )
    }

    data class NumberWithIndex(val number: Int, val index: Int)
}
