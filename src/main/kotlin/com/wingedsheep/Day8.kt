package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader
import java.util.TreeSet
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day8Solver(resourceFileReader)
    solver.solve()
}

class Day8Solver(private val resourceFileReader: ResourceFileReader) {

    fun solve() {
        val junctionBoxes = parse()
        val pairsWithDistance = getDistances(junctionBoxes)

        // Solve part one
        val circuitResult = createCircuits(junctionBoxes, pairsWithDistance.toList().subList(0, 1000))
        val threeLargestCircuitSizesMultiplied = circuitResult.circuits
            .map { it.value.size }
            .sortedDescending()
            .subList(0, 3)
            .reduce { acc, i -> acc * i }

        println("Answer: $threeLargestCircuitSizesMultiplied")

        // Solve part two
        val circuitResult2 = createCircuits(junctionBoxes, pairsWithDistance.toList())
        requireNotNull(circuitResult2.lastConnected)
        val xCoordinatesOfLastConnectionMultiplied = circuitResult2.lastConnected.first.x.times(circuitResult2.lastConnected.second.x)

        println("Answer 2: $xCoordinatesOfLastConnectionMultiplied")

    }

    private fun parse(): List<JunctionBox> {
        return resourceFileReader.readLinesFromResource("/day8/input.txt").map { line ->
            val (x, y, z) = line.split(",").map { it.toLong() }
            JunctionBox(x, y, z)
        }
    }

    data class CircuitResult(val circuits: MutableMap<Int, MutableSet<JunctionBox>>, val lastConnected: Pair<JunctionBox, JunctionBox>?)

    private fun createCircuits(junctionBoxes: List<JunctionBox>, pairsWithDistance: List<Connection>): CircuitResult {
        var currentCircuitId = 0
        val circuitIdToJunctionBoxes = mutableMapOf<Int, MutableSet<JunctionBox>>()
        val junctionBoxToCircuitId = mutableMapOf<JunctionBox, Int>()
        var lastConnected: Pair<JunctionBox, JunctionBox>? = null

        junctionBoxes.forEach { junctionBox ->
                circuitIdToJunctionBoxes[currentCircuitId] = mutableSetOf(junctionBox)
                junctionBoxToCircuitId[junctionBox] = currentCircuitId
                currentCircuitId++
        }

        for (shortestDistance in pairsWithDistance) {
            val circuitIdI = requireNotNull(junctionBoxToCircuitId[shortestDistance.from])
            val circuitIdJ = requireNotNull(junctionBoxToCircuitId[shortestDistance.to])
            if (circuitIdI != circuitIdJ) {
                lastConnected = Pair(shortestDistance.from, shortestDistance.to)
                for (junctionBox in requireNotNull(circuitIdToJunctionBoxes[circuitIdJ])) {
                    junctionBoxToCircuitId[junctionBox] = circuitIdI
                    circuitIdToJunctionBoxes[circuitIdI]?.add(junctionBox)
                }
                circuitIdToJunctionBoxes.remove(circuitIdJ)
                if (circuitIdToJunctionBoxes.size == 1) {
                    println("All connected")
                    break
                }
            }
        }

        return CircuitResult(circuitIdToJunctionBoxes, lastConnected)
    }

    private fun getDistances(junctionBoxes: List<JunctionBox>): TreeSet<Connection> {
        val shortestConnections = sortedSetOf(compareBy(Connection::distance).thenBy { it.from.x }.thenBy { it.from.y }.thenBy { it.from.z }.thenBy { it.to.x }.thenBy { it.to.y }.thenBy {it.to.z} )

        for (i in 1..junctionBoxes.size - 1) {
            for (j in 0..i - 1) {
                val distance = junctionBoxes[i].distanceTo(junctionBoxes[j])
                shortestConnections.add(Connection(junctionBoxes[i], junctionBoxes[j], distance))
            }
        }

        return shortestConnections
    }

    data class Connection(val from: JunctionBox, val to: JunctionBox, val distance: Double)

    data class JunctionBox(val x: Long, val y: Long, val z: Long) {

        fun distanceTo(other: JunctionBox): Double {
            val xDistance = this.x - other.x
            val yDistance = this.y - other.y
            val distanceInXYPlane = sqrt(xDistance.toDouble().pow(2.0) + yDistance.toDouble().pow(2.0))

            val zDistance = this.z - other.z
            return sqrt(distanceInXYPlane.pow(2) + zDistance.toDouble().pow(2))
        }
    }

}
