fun main() {
    val grid = object {}.javaClass.getResourceAsStream("/day4/input.txt")!!
        .bufferedReader()
        .readLines()
        .filter { it.isNotBlank() }

    val rolls = parseRolls(grid)

    println("Part 1: ${solvePart1(rolls)}")
    println("Part 2: ${solvePart2(rolls)}")
}

data class Pos(val row: Int, val col: Int)

val DIRECTIONS = listOf(
    Pos(-1, -1), Pos(-1, 0), Pos(-1, 1),
    Pos(0, -1), Pos(0, 1),
    Pos(1, -1), Pos(1, 0), Pos(1, 1)
)

fun parseRolls(grid: List<String>): Set<Pos> =
    grid.flatMapIndexed { row, line ->
        line.mapIndexedNotNull { col, c ->
            if (c == '@') Pos(row, col) else null
        }
    }.toSet()

fun Pos.neighbors() = DIRECTIONS.map { Pos(row + it.row, col + it.col) }

fun countAdjacentRolls(pos: Pos, rolls: Set<Pos>) =
    pos.neighbors().count { it in rolls }

fun isAccessible(pos: Pos, rolls: Set<Pos>) =
    countAdjacentRolls(pos, rolls) < 4

fun solvePart1(rolls: Set<Pos>) =
    rolls.count { isAccessible(it, rolls) }

fun solvePart2(initialRolls: Set<Pos>): Int {
    val rolls = initialRolls.toMutableSet()
    val accessible = rolls.filter { isAccessible(it, rolls) }.toMutableSet()
    var totalRemoved = 0

    while (accessible.isNotEmpty()) {
        val toRemove = accessible.toSet()
        totalRemoved += toRemove.size

        rolls.removeAll(toRemove)
        accessible.clear()

        val candidates = toRemove.flatMap { it.neighbors() }.filter { it in rolls }.toSet()
        accessible.addAll(candidates.filter { isAccessible(it, rolls) })
    }

    return totalRemoved
}
