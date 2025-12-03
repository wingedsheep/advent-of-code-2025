package com.wingedsheep.day1

import kotlin.math.absoluteValue
import kotlin.math.min

class Day1Algorithm {

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