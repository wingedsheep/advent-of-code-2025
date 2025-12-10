package com.wingedsheep

import com.wingedsheep.util.ResourceFileReader
import java.util.stream.IntStream.range
import kotlin.math.max

fun main() {
    val resourceFileReader = ResourceFileReader()
    val solver = Day7Solver(resourceFileReader)
    solver.solve()
}

class Day7Solver(private val resourceFileReader: ResourceFileReader) {

    fun solve() {
        val splits = parse().fireTachyonBeam().splits
        println("Number of splits: $splits")

        val graph = parse().fireQuantumTachyonBeam().graph
        graph.processGraph()
        println("Number of quantum splits: ${graph.startNode.totalPathsFromHere!! - 1}")
    }

    fun parse(): TeleportationMachine {
        val lines = resourceFileReader.readLinesFromResource("/day7/input.txt")

        val start = lines[0].indexOf('S')
        val splitterIndicesPerRow = (2..lines.size -1 step 2).map { rowIndex ->
            val line = lines[rowIndex]

            val splittersOnLine = HashSet<Int>()
            line.forEachIndexed {
                index, char -> if (char == '^') splittersOnLine.add(index)
            }
            return@map splittersOnLine
        }

        return TeleportationMachine(start, splitterIndicesPerRow)
    }

    class TeleportationMachine(
        val start: Int,
        val splitterIndicesPerRow: List<Set<Int>>
    ) {
        fun fireTachyonBeam(): BeamState {
            val initialState = BeamState(0, setOf(start), 0)
            return splitterIndicesPerRow.fold(initialState) {
                acc, splitterIndices -> acc.splitBeam(splitterIndices)
            }
        }

        fun fireQuantumTachyonBeam(): QuantumBeamState {
            val startNode = Node(-1, start, mutableSetOf())
            val initialGraph = Graph(startNode)
            val initialState = QuantumBeamState(0, setOf(Pair(start, startNode)), initialGraph)
            return splitterIndicesPerRow.fold(initialState) {
                    acc, splitterIndices -> acc.splitBeam(splitterIndices)
            }
        }

        data class QuantumBeamState(var row: Int, var beamsFromNodes: Set<Pair<Int, Node>>, val graph: Graph) {

            fun splitBeam(splitterIndices: Set<Int>): QuantumBeamState {
                val beamsFromNodesHittingSplitters = beamsFromNodes.filter {
                    splitterIndices.contains(it.first)
                }
                val beamsFromNodesNotHittingSplitters = beamsFromNodes.minus(beamsFromNodesHittingSplitters)

                val nodesOnThisRow: MutableMap<Int, Node> = mutableMapOf()

                beamsFromNodesHittingSplitters.map {
                    it.first
                }.forEach { nodesOnThisRow[it] = Node(row, it, mutableSetOf()) }

                graph.nodes.addAll(nodesOnThisRow.values)

                val newBeamsFromNodes = mutableSetOf<Pair<Int, Node>>()
                beamsFromNodesHittingSplitters.forEach { pair ->
                    val node = nodesOnThisRow[pair.first]!!
                    pair.second.children.add(node)
                    newBeamsFromNodes.add(Pair(pair.first - 1, node))
                    newBeamsFromNodes.add(Pair(pair.first + 1, node))
                }
                newBeamsFromNodes.addAll(beamsFromNodesNotHittingSplitters)

                return QuantumBeamState(this.row + 1, newBeamsFromNodes, graph)
            }
        }

        class Graph(val startNode: Node, val nodes: MutableSet<Node> = mutableSetOf()) {

            init {
                nodes.add(startNode)
            }

            override fun toString(): String {
                return "Graph"
            }

            fun processGraph() {
                while (processNextLayer()) {}
            }

            fun processNextLayer(): Boolean {
                val processableNodes = getProcessableNodes()
                processableNodes.forEach { it.process() }
                return processableNodes.isNotEmpty()
            }

            fun getProcessableNodes() = nodes.filter { it.isProcessable() }
        }

        class Node(val row: Int, val index: Int, val children: MutableSet<Node>, var totalPathsFromHere: Long? = null) {

            fun process() {
                val beams = 2 - children.size
                val sumOfChildren = children.fold(0L) {acc, node -> acc + node.totalPathsFromHere!!}
                totalPathsFromHere = beams + sumOfChildren
            }

            fun isProcessed() = totalPathsFromHere != null
            fun isProcessable() = !isProcessed() && children.fold(true) { acc, node -> acc && node.isProcessed() }
        }

        data class BeamState(var row: Int, var beams: Set<Int>, var splits: Long) {

            fun splitBeam(splitterIndices: Set<Int>): BeamState {
                val beamsHittingSplitters = beams.intersect(splitterIndices)
                val beamsNotHittingSplitters = beams.minus(splitterIndices)

                val newBeams = mutableSetOf<Int>()
                beamsHittingSplitters.toSet().forEach { beamIndex ->
                    newBeams.add(beamIndex - 1)
                    newBeams.add(beamIndex + 1)
                }
                newBeams.addAll(beamsNotHittingSplitters)

                return BeamState(this.row + 1, newBeams, this.splits + beamsHittingSplitters.size)
            }
        }

    }
}
