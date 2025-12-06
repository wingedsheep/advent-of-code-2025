package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader
import java.util.stream.IntStream.range
import kotlin.math.max

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day6Solver(resourceFileReader)
    solver.solve1()
    solver.solve2()
}

class Day6Solver(private val resourceFileReader: ResourceFileReader) {

    fun solve1() {
        val result = parseProblems(ParsingType.PART1).fold(0L) { acc, problem ->
            acc + problem.solve()
        }
        println("Result for part 1: $result")
    }

    fun solve2() {
        val result = parseProblems(ParsingType.PART2).fold(0L) { acc, problem ->
            acc + problem.solve()
        }
        println("Result for part 2: $result")
    }

    private fun parseProblems(parsingType: ParsingType): List<Problem> {
        val rows = resourceFileReader.readLinesFromResource("/day6/input.txt")

        val numberRows: MutableList<List<Long>> = mutableListOf()
        val numberColumns: MutableList<MutableList<Long>> = mutableListOf()

        var operatorRow: List<String> = listOf()

        for (row in rows) {
            if (row.contains("+") || row.contains("*")) {
                operatorRow = parseOperators(row)
            } else {
                if (parsingType == ParsingType.PART1) {
                    val numbers = parseNumbers(row)
                    if (numbers.isEmpty()) continue
                    numberRows.add(numbers)
                }
            }
        }

        if (parsingType == ParsingType.PART2) {
            // Flip the input around
            val r = rows.size
            val c = rows[0].length
            val results = (0..c - 1).map { column ->
                (0.. r - 1).fold("") { acc, row ->
                    acc + rows[row][column]
                }
            }.map { it.replace(Regex("\\D"), "") }

            numberColumns.add(ArrayList())
            for (result in results) {
                if (result.isBlank()) numberColumns.add(ArrayList())
                else numberColumns[numberColumns.size - 1].add(result.toLong())
            }
        }

        return (0..operatorRow.size - 1).map { column ->
            val numbers = if(parsingType == ParsingType.PART1) numberRows.map { numbersRow -> numbersRow[column] }
                          else numberColumns[column]

            val operator = operatorRow[column]
            Problem(numbers, operator)
        }
    }

    private fun parseNumbers(row: String): List<Long> =
        row.split(Regex("\\s+")).map { it.toLong() }

    private fun parseOperators(row: String): List<String> =
        row.split(Regex("\\s+")).map { it }.filter { !it.isBlank() }

    data class Problem(val numbers: List<Long>, val operator: String) {

        fun solve(): Long {
            return when (operator) {
                "*" -> numbers.fold(1L) { total, number -> total * number }
                "+" -> numbers.fold(0L) { total, number -> total + number }
                else -> error("Unsupported operator")
            }
        }
    }

    enum class ParsingType {
        PART1, PART2
    }
}
