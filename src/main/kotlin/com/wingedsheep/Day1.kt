package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader
import kotlin.math.absoluteValue
import kotlin.math.min

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
        val state = Problem1State()
        rotations.forEach { state.doRotation(it) }
        return state.exactlyZeroCount
    }

    fun crackCode2(rotations: List<Rotation>): Int {
        val state = Problem2State()
        rotations.forEach { state.doRotation(it) }
        if (state.currentPosition == 0) state.movePastZeroCount += 1
        return state.movePastZeroCount
    }

    private data class Problem1State(var currentPosition: Int = 50, var exactlyZeroCount: Int = 0) {

        companion object {
            const val MAX_VALUE = 99
        }

        fun doRotation(rotation: Rotation) {
            updateCurrentPosition(
                when (rotation.direction) {
                    Direction.Right -> rotation.ticks
                    else -> -rotation.ticks
                }
            )
            if (currentPosition == 0) exactlyZeroCount++
        }

        private fun updateCurrentPosition(ticks: Int) {
            currentPosition += ticks
            while (currentPosition < 0) currentPosition += (MAX_VALUE + 1)
            while (currentPosition > MAX_VALUE) currentPosition -= (MAX_VALUE + 1)
        }
    }

    private data class Problem2State(var currentPosition: Int = 50, var movePastZeroCount: Int = 0) {

        companion object {
            const val MAX_VALUE = 99
        }

        fun doRotation(rotation: Rotation) {
            updateCurrentPositionAndCountZeroPasses(
                when (rotation.direction) {
                    Direction.Right -> rotation.ticks
                    else -> -rotation.ticks
                }
            )
        }

        private fun updateCurrentPositionAndCountZeroPasses(ticks: Int) {
            var ticksRemaining = ticks
            while (ticksRemaining < 0) {
                if (currentPosition == 0) {
                    movePastZeroCount++
                    currentPosition = 100
                }
                val absoluteTicksToNextZero = currentPosition
                val absoluteTicksToTake = min(absoluteTicksToNextZero, ticksRemaining.absoluteValue)
                currentPosition -= absoluteTicksToTake
                if (currentPosition == -100) currentPosition = 0
                ticksRemaining += absoluteTicksToTake
            }
            while (ticksRemaining > 0) {
                if (currentPosition == 0) movePastZeroCount++
                val absoluteTicksToNextZero = (MAX_VALUE + 1) - currentPosition
                val absoluteTicksToTake = min(absoluteTicksToNextZero, ticksRemaining.absoluteValue)
                currentPosition += absoluteTicksToTake
                if (currentPosition == 100) currentPosition = 0
                ticksRemaining -= absoluteTicksToTake
            }
        }
    }
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