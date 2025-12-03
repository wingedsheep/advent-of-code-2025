package com.wingedsheep.day1

data class Rotation(
    val direction: Direction,
    val ticks: Int)

enum class Direction {
    Left, Right
}