package com.claude

fun main() {
    val input = object {}.javaClass.getResourceAsStream("/day5/input.txt")!!
        .bufferedReader()
        .readText()

    val (rangesSection, idsSection) = input.split("\n\n")
    val ranges = parseRangesDay5(rangesSection)
    val availableIds = idsSection.lines().filter { it.isNotBlank() }.map { it.toLong() }

    println("Part 1: ${solvePart1(ranges, availableIds)}")
    println("Part 2: ${solvePart2(ranges)}")
}

data class Range(val start: Long, val end: Long) {
    operator fun contains(id: Long) = id in start..end
    val size get() = end - start + 1
}

fun parseRangesDay5(input: String): List<Range> =
    input.lines()
        .filter { it.isNotBlank() }
        .map { line ->
            val (start, end) = line.split("-").map { it.toLong() }
            Range(start, end)
        }

fun solvePart1(ranges: List<Range>, availableIds: List<Long>): Int =
    availableIds.count { id -> ranges.any { id in it } }

fun mergeRanges(ranges: List<Range>): List<Range> {
    if (ranges.isEmpty()) return emptyList()

    val sorted = ranges.sortedBy { it.start }
    val merged = mutableListOf(sorted.first())

    for (range in sorted.drop(1)) {
        val last = merged.last()
        if (range.start <= last.end + 1) {
            merged[merged.lastIndex] = Range(last.start, maxOf(last.end, range.end))
        } else {
            merged.add(range)
        }
    }
    return merged
}

fun solvePart2(ranges: List<Range>): Long =
    mergeRanges(ranges).sumOf { it.size }