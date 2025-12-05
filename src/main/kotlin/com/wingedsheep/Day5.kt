package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader
import kotlin.math.max

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day5Solver(resourceFileReader)
    solver.solve1()
    solver.solve2()
}

class Day5Solver(private val resourceFileReader: ResourceFileReader) {

    fun solve1() {
        val (freshIngredientRanges, availableIngredients) = parseData()

        val availableFreshIngredients = ArrayList<Long>()
        for (availableIngredient in availableIngredients) {
            val contains = freshIngredientRanges.any { it.contains(availableIngredient) }
            if (contains) availableFreshIngredients.add(availableIngredient)
        }

        println("Part 1 solution: ${availableFreshIngredients.size}")
    }

    fun solve2() {
        val (freshIngredientRanges, _) = parseData()
        freshIngredientRanges.sortBy { it.first }
        val mergedRanges = mergeRanges(freshIngredientRanges)
        val totalFreshIngredients = mergedRanges.fold(0L) { acc, range -> acc + (range.second - range.first + 1) }

        println(mergedRanges)

        println("Part 2 solution: $totalFreshIngredients")
    }

    private fun mergeRanges(freshIngredientRangesSortedByStart: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
        var mergedRanges = true
        var result = freshIngredientRangesSortedByStart
        while (mergedRanges) {
            val sizeBeforeMerge = result.size
            result = mergeFirst(result)
            result.sortedBy { it.first }
            mergedRanges = sizeBeforeMerge > result.size
        }
        return result
    }

    private fun mergeFirst(freshIngredientRangesSortedByStart: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
        val newList = ArrayList<Pair<Long, Long>>()
        for (i in 0 .. freshIngredientRangesSortedByStart.size - 2) {
            val merged = freshIngredientRangesSortedByStart[i].merge(freshIngredientRangesSortedByStart[i+1])
            if (merged.size == 1) {
                if (i > 0) {
                    newList.addAll(freshIngredientRangesSortedByStart.subList(0, i))
                }
                newList.add(merged[0])
                if (i < freshIngredientRangesSortedByStart.size - 2) {
                    newList.addAll(freshIngredientRangesSortedByStart.subList(i+2, freshIngredientRangesSortedByStart.size))
                }
                return newList
            }
        }
        return freshIngredientRangesSortedByStart
    }

    fun Pair<Long, Long>.merge(other: Pair<Long, Long>) : List<Pair<Long, Long>> {
        return if (this.second >= other.first) {
            listOf(Pair(this.first, max(this.second, other.second)))
        } else {
            listOf(this, other)
        }
    }

    fun Pair<Long, Long>.contains(ingredientId: Long): Boolean {
        return (this.first <= ingredientId) && (ingredientId <= this.second)
    }

    fun parseData() : Pair<ArrayList<Pair<Long, Long>>, ArrayList<Long>> {
        val lines = resourceFileReader.readLinesFromResource("/day5/input.txt")

        val freshIngredientRanges = ArrayList<Pair<Long, Long>>()
        val availableIngredients = ArrayList<Long>()

        for (line in lines) {
            if (line == "") {
                continue
            } else if (line.contains("-")) {
                val values = line.split("-")
                freshIngredientRanges.add(Pair(values[0].toLong(), values[1].toLong()))
            } else {
                availableIngredients.add(line.toLong())
            }
        }

        return Pair(freshIngredientRanges, availableIngredients)
    }
}