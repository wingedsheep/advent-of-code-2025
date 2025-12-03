fun main() {
    val banks = object {}.javaClass.getResourceAsStream("/day3/input.txt")!!
        .bufferedReader()
        .readLines()
        .filter { it.isNotBlank() }

    println("Part 1: ${solve(banks, batteriesToSelect = 2)}")
    println("Part 2: ${solve(banks, batteriesToSelect = 12)}")
}

fun solve(banks: List<String>, batteriesToSelect: Int): Long =
    banks.sumOf { findMaxJoltage(it, batteriesToSelect) }

fun findMaxJoltage(bank: String, k: Int): Long {
    val n = bank.length
    val result = StringBuilder()
    var start = 0

    repeat(k) { i ->
        val end = n - k + i
        val maxIdx = (start..end).maxBy { bank[it] }
        result.append(bank[maxIdx])
        start = maxIdx + 1
    }

    return result.toString().toLong()
}
