package com.claude

fun main() {
    val input = object {}.javaClass.getResourceAsStream("/day2/input.txt")!!
        .bufferedReader()
        .readText()
        .trim()

    val ranges = parseRanges(input)

    println("Part 1: ${solvePart1(ranges)}")
    println("Part 2: ${solvePart2(ranges)}")
}

data class IdRange(val start: Long, val end: Long)

fun parseRanges(input: String): List<IdRange> =
    input.split(",")
        .filter { it.isNotBlank() }
        .map { range ->
            val (start, end) = range.split("-").map { it.toLong() }
            IdRange(start, end)
        }

fun pow10(n: Int): Long {
    var result = 1L
    repeat(n) { result *= 10 }
    return result
}

fun findInvalidIdsInRange(range: IdRange, exactlyTwice: Boolean): Set<Long> {
    val result = mutableSetOf<Long>()
    val minDigits = range.start.toString().length
    val maxDigits = range.end.toString().length

    for (totalLen in minDigits..maxDigits) {
        val unitLengths = if (exactlyTwice) {
            if (totalLen % 2 == 0) listOf(totalLen / 2) else emptyList()
        } else {
            (1..totalLen / 2).filter { totalLen % it == 0 }
        }

        for (unitLen in unitLengths) {
            val multiplier = (pow10(totalLen) - 1) / (pow10(unitLen) - 1)
            val unitMin = maxOf(
                if (unitLen == 1) 1L else pow10(unitLen - 1),
                (range.start + multiplier - 1) / multiplier
            )
            val unitMax = minOf(
                pow10(unitLen) - 1,
                range.end / multiplier
            )

            for (unit in unitMin..unitMax) {
                result.add(unit * multiplier)
            }
        }
    }
    return result
}

fun solvePart1(ranges: List<IdRange>): Long =
    ranges.flatMap { findInvalidIdsInRange(it, exactlyTwice = true) }.sum()

fun solvePart2(ranges: List<IdRange>): Long =
    ranges.flatMap { findInvalidIdsInRange(it, exactlyTwice = false) }.sum()