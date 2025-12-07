fun main() {
    val grid = object {}.javaClass.getResourceAsStream("/day7/input.txt")!!
        .bufferedReader()
        .readLines()
        .filter { it.isNotBlank() }

    val startCol = grid[0].indexOf('S')

    println("Part 1: ${solvePart1(grid, startCol)}")
    println("Part 2: ${solvePart2(grid, startCol)}")
}

fun solvePart1(grid: List<String>, startCol: Int): Int {
    val height = grid.size
    val width = grid[0].length

    var beamColumns = setOf(startCol)
    var currentRow = 0
    var splits = 0

    while (beamColumns.isNotEmpty() && currentRow < height - 1) {
        currentRow++
        val nextColumns = mutableSetOf<Int>()

        for (col in beamColumns) {
            if (grid[currentRow][col] == '^') {
                splits++
                if (col - 1 >= 0) nextColumns.add(col - 1)
                if (col + 1 < width) nextColumns.add(col + 1)
            } else {
                nextColumns.add(col)
            }
        }

        beamColumns = nextColumns
    }

    return splits
}

fun solvePart2(grid: List<String>, startCol: Int): Long {
    val height = grid.size
    val width = grid[0].length
    val memo = mutableMapOf<Pair<Int, Int>, Long>()

    fun countTimelines(startRow: Int, col: Int): Long {
        if (col < 0 || col >= width) return 0

        val key = startRow to col
        memo[key]?.let { return it }

        var row = startRow
        while (true) {
            row++
            if (row >= height) {
                memo[key] = 1L
                return 1L
            }
            if (grid[row][col] == '^') {
                val result = countTimelines(row, col - 1) + countTimelines(row, col + 1)
                memo[key] = result
                return result
            }
        }
    }

    return countTimelines(0, startCol)
}
