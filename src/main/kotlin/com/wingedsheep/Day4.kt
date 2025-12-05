package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day4Solver(resourceFileReader)
    solver.solvePart1()
    solver.solvePart2()
}

class Day4Solver(private val resourceFileReader: ResourceFileReader) {

    fun solvePart1() {
        val matrix = parseAsMatrix()
        println("Cells with less than 4 adjacent tiles: ${matrix.countLessThanXAdjacent(4)}")
    }

    fun solvePart2() {
        val matrix = parseAsMatrix()
        var totalRemoved = 0
        var lastRemoved: Int? = null
        while (lastRemoved != 0) {
            lastRemoved = matrix.removeLessThanXAdjacent(4)
            totalRemoved += lastRemoved
        }
        println("Total removed rolls: $totalRemoved")
    }

    fun parseAsMatrix() : PaperRollMatrix {
        val rows = resourceFileReader.readLinesFromResource("/day4/input.txt")
        val columnCount = rows[0].length

        val matrixValues: Array<BooleanArray> = Array(rows.size) {
            BooleanArray(columnCount) { false }
        }
        for (r in 0 .. rows.size - 1) {
            val rowValues = rows[r]
            for (c in 0 .. rowValues.length - 1) {
                matrixValues[r][c] = rowValues[c] == '@'
            }
        }

        return PaperRollMatrix(matrixValues)
    }

    class PaperRollMatrix(val values: Array<BooleanArray>) {

        val rowCount: Int = values.size
        val columnCount: Int = values[0].size

        fun countRollsInAdjacentTiles(row: Int, col: Int): Int {
            var count = 0
            for (r in row - 1..row + 1) {
                for (c in col - 1 .. col + 1) {
                    if (r == row && c == col) continue
                    if (containsPaperRoll(r, c)) {
                        count ++
                    }
                }
            }
            return count
        }

        fun containsPaperRoll(row: Int, column: Int): Boolean {
            if (row < 0) return false
            if (column < 0) return false
            if (row >= rowCount) return false
            if (column >= columnCount) return false
            return values[row][column]
        }

        fun countLessThanXAdjacent(x: Int): Int {
            var count = 0
            for (row in 0..rowCount - 1) {
                for (col in 0..columnCount - 1) {
                    if (values[row][col]) {
                        if (countRollsInAdjacentTiles(row, col) < 4) count++
                    }
                }
            }
            return count
        }

        fun removeLessThanXAdjacent(x: Int): Int {
            val toBeRemoved = ArrayList<Pair<Int, Int>>()
            var removed = 0
            for (row in 0..rowCount - 1) {
                for (col in 0..columnCount - 1) {
                    if (values[row][col]) {
                        if (countRollsInAdjacentTiles(row, col) < x) {
                            toBeRemoved.add(Pair(row, col))
                            removed ++
                        }
                    }
                }
            }
            toBeRemoved.forEach { values[it.first][it.second] = false }
            return removed
        }
    }
}