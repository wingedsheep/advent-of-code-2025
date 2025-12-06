fun main() {
    val lines = object {}.javaClass.getResourceAsStream("/day6/input.txt")!!
        .bufferedReader()
        .readLines()

    val problems = parseProblems(lines)

    println("Part 1: ${solvePart1(problems)}")
    println("Part 2: ${solvePart2(problems)}")
}

data class Problem(val columns: List<String>, val operator: Char)

fun parseProblems(lines: List<String>): List<Problem> {
    val maxWidth = lines.maxOf { it.length }
    val padded = lines.map { it.padEnd(maxWidth) }
    val operatorRow = padded.last()
    val numberRows = padded.dropLast(1)

    val problems = mutableListOf<Problem>()
    var currentColumns = mutableListOf<String>()
    var currentOperator = ' '

    for (col in 0 until maxWidth) {
        val column = numberRows.map { it[col] }.joinToString("")
        val opChar = operatorRow[col]

        if (column.all { it == ' ' } && opChar == ' ') {
            if (currentColumns.isNotEmpty()) {
                problems.add(Problem(currentColumns, currentOperator))
                currentColumns = mutableListOf()
                currentOperator = ' '
            }
        } else {
            currentColumns.add(column)
            if (opChar != ' ') currentOperator = opChar
        }
    }

    if (currentColumns.isNotEmpty()) {
        problems.add(Problem(currentColumns, currentOperator))
    }

    return problems
}

fun applyOperator(numbers: List<Long>, operator: Char): Long = when (operator) {
    '+' -> numbers.sum()
    '*' -> numbers.fold(1L) { acc, n -> acc * n }
    else -> error("Unknown operator: $operator")
}

fun solvePart1(problems: List<Problem>): Long =
    problems.sumOf { problem ->
        val numRows = problem.columns.first().length
        val numbers = (0 until numRows).mapNotNull { rowIdx ->
            val rowDigits = problem.columns.map { it[rowIdx] }.joinToString("").filter { it.isDigit() }
            if (rowDigits.isNotBlank()) rowDigits.toLong() else null
        }
        applyOperator(numbers, problem.operator)
    }

fun solvePart2(problems: List<Problem>): Long =
    problems.sumOf { problem ->
        val numbers = problem.columns
            .reversed()
            .map { col -> col.filter { it.isDigit() } }
            .filter { it.isNotBlank() }
            .map { it.toLong() }
        applyOperator(numbers, problem.operator)
    }