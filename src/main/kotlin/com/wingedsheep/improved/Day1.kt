package com.wingedsheep.improved

import com.wingedsheep.util.ResourceFileReader
import javax.swing.text.Position
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

fun main() {
    val resourceFileReader = ResourceFileReader()
    val inputParser = InputParser(resourceFileReader)
    val solver = Day1Solver()

    val rotations = inputParser.parseInput("/day1/input.txt")

    val code1 = solver.crackCode1(rotations)
    println("Code 1 is: $code1")

    val code2 = solver.crackCode2(rotations)
    println("Code 2 is: $code2")
}

class Day1Solver {

    fun crackCode1(rotations: List<Rotation>): Int {
        return rotations.fold(State(50, 0)) {
            state, rotation -> state.rotate(rotation, false)
        }.zeroCount
    }

    fun crackCode2(rotations: List<Rotation>): Int {
        return rotations.fold(State(50, 0)) {
                state, rotation -> state.rotate(rotation, true)
        }.zeroCount
    }

    fun State.rotate(rotation: Rotation, countMovePastZero: Boolean): State {
        val ticksToTake = if (rotation.direction == Direction.Left) -rotation.ticks else rotation.ticks
        val endPosition = (this.position + ticksToTake)
        val realEndPosition = ((endPosition % 100) + 100) % 100
        val zeroPasses = countZeroPasses(this.position, endPosition, countMovePastZero)
        return State(realEndPosition, this.zeroCount + zeroPasses)
    }

    fun countZeroPasses(startValue: Int, endValue: Int, countMovePastZero: Boolean): Int {
        return if (!countMovePastZero) {
            if (endValue % 100 == 0) 1 else 0
        } else {
            val start = ceil(startValue / 100.0).toInt()
            val end = ceil(endValue / 100.0).toInt()
            var count = (start - end).absoluteValue
            if (endValue > 0 && endValue % 100 == 0) count ++
            if (startValue == 0 && endValue > 0) count --
            return count
        }
    }

    data class State(val position: Int, val zeroCount: Int)

}

class InputParser (val resourceFileReader: ResourceFileReader) {

    fun parseInput(filename: String): List<Rotation> {
        return resourceFileReader.readLinesFromResource(filename)
            .map(this::parseLine)
    }

    private fun parseLine(line: String): Rotation {
        val directionChar = line[0]
        val ticks = line.substring(1).toInt()
        val direction = when (directionChar) {
            'L' -> Direction.Left
            'R' -> Direction.Right
            else -> error("Unexpected character $directionChar")
        }
        return Rotation(
            direction = direction,
            ticks = ticks
        )
    }
}

data class Rotation(
    val direction: Direction,
    val ticks: Int)

enum class Direction {
    Left, Right
}