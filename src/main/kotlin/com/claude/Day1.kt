package com.claude

fun main() {
    val rotations = object {}.javaClass.getResourceAsStream("/day1/input.txt")!!
        .bufferedReader()
        .readLines()
        .map(::parseRotation)

    println("Part 1: ${solvePart1(rotations)}")
    println("Part 2: ${solvePart2(rotations)}")
}

data class Rotation(val direction: Char, val distance: Int)

fun parseRotation(line: String) = Rotation(line[0], line.drop(1).toInt())

fun Rotation.applyTo(position: Int): Int = when (direction) {
    'L' -> (position - distance).mod(100)
    'R' -> (position + distance).mod(100)
    else -> error("Unknown direction: $direction")
}

fun Rotation.countZerosCrossedFrom(start: Int): Int {
    val stepsToFirstZero = when (direction) {
        'L' -> if (start == 0) 100 else start
        'R' -> if (start == 0) 100 else 100 - start
        else -> return 0
    }
    return if (distance >= stepsToFirstZero) {
        (distance - stepsToFirstZero) / 100 + 1
    } else {
        0
    }
}

fun solvePart1(rotations: List<Rotation>): Int {
    var position = 50
    return rotations.count { rotation ->
        position = rotation.applyTo(position)
        position == 0
    }
}

fun solvePart2(rotations: List<Rotation>): Int {
    var position = 50
    return rotations.sumOf { rotation ->
        val zeros = rotation.countZerosCrossedFrom(position)
        position = rotation.applyTo(position)
        zeros
    }
}
