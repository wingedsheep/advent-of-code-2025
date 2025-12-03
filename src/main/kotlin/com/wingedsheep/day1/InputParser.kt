package com.wingedsheep.day1

import com.wingedsheep.util.ResourceFileReader

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