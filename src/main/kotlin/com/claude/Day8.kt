fun main() {
    Day8Solver.solve()
}

object Day8Solver {
    data class Point(val x: Long, val y: Long, val z: Long)

    data class Edge(val i: Int, val j: Int, val distSq: Long)

    class UnionFind(size: Int) {
        private val parent = IntArray(size) { it }
        private val rank = IntArray(size) { 1 }
        var componentCount = size
            private set

        fun find(x: Int): Int {
            if (parent[x] != x) parent[x] = find(parent[x])
            return parent[x]
        }

        fun union(x: Int, y: Int): Boolean {
            val px = find(x)
            val py = find(y)
            if (px == py) return false

            if (rank[px] < rank[py]) {
                parent[px] = py
                rank[py] += rank[px]
            } else {
                parent[py] = px
                rank[px] += rank[py]
            }
            componentCount--
            return true
        }

        fun getSize(x: Int) = rank[find(x)]

        fun allSizes(): List<Int> =
            parent.indices.filter { parent[it] == it }.map { rank[it] }
    }

    fun solve() {
        val points = javaClass.getResourceAsStream("/day8/input.txt")!!
            .bufferedReader()
            .readLines()
            .filter { it.isNotBlank() }
            .map { line ->
                val (x, y, z) = line.split(",").map { it.toLong() }
                Point(x, y, z)
            }

        val edges = buildSortedEdges(points)

        println("Part 1: ${solvePart1(points.size, edges)}")
        println("Part 2: ${solvePart2(points, edges)}")
    }

    private fun buildSortedEdges(points: List<Point>): List<Edge> {
        val edges = mutableListOf<Edge>()
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                val dx = points[i].x - points[j].x
                val dy = points[i].y - points[j].y
                val dz = points[i].z - points[j].z
                edges.add(Edge(i, j, dx * dx + dy * dy + dz * dz))
            }
        }
        return edges.sortedBy { it.distSq }
    }

    private fun solvePart1(pointCount: Int, edges: List<Edge>): Long {
        val uf = UnionFind(pointCount)
        var connections = 0

        for (edge in edges) {
            if (uf.union(edge.i, edge.j)) {
                connections++
                if (connections == 1000) break
            }
        }

        return uf.allSizes()
            .sortedDescending()
            .take(3)
            .fold(1L) { acc, size -> acc * size }
    }

    private fun solvePart2(points: List<Point>, edges: List<Edge>): Long {
        val uf = UnionFind(points.size)

        for (edge in edges) {
            if (uf.union(edge.i, edge.j)) {
                if (uf.componentCount == 1) {
                    return points[edge.i].x * points[edge.j].x
                }
            }
        }

        error("All points should eventually connect")
    }
}
