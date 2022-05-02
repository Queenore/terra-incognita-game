import kotlin.math.abs

class AStar(private val maze: Maze, private val mazeGraph: Graph, twoCoords: TwoCoords) {

    private val trace = mutableListOf<Pair<Int, Int>>()
    private val allTrace = mutableListOf<Pair<Int, Int>>()
    private val start = twoCoords.firstCoord
    private val finish = twoCoords.secondCoord

    init {
        aStarSolve()
    }

    fun print() {
        val aStarMaze = maze.deepCopy()
        aStarMaze.printTrace(trace)
        println("A*: trace length = ${trace.size}")
        aStarMaze.printMaze()
        println()
        aStarMaze.printTrace(allTrace)
        println("A*: all trace length = ${allTrace.size}")
        aStarMaze.printMaze()
        println()
    }

    private data class AStarHelp(
        var curr: Graph.Vertex?,
        var visited: Boolean,
        var distToStart: Int = Int.MAX_VALUE,
        var distToFinish: Int = Int.MAX_VALUE,
        var prev: Graph.Vertex?,
    )

    private fun manhattDist(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        abs(a.first - b.first) + abs(a.second - b.second)

    private fun aStarSolve() {
        val info = mutableMapOf<Graph.Vertex, AStarHelp>()
        val compareByDist: Comparator<AStarHelp> = compareBy { it.distToStart + it.distToFinish }
        val prQueue = java.util.PriorityQueue(compareByDist)
        val vert = mazeGraph.vertices

        // A* trace search, using PriorityQueue
        vert.forEach { info[it.value] = AStarHelp(curr = null, visited = false, prev = null) }
        info[vert[start]!!] = AStarHelp(
            curr = vert[start]!!,
            visited = false,
            distToStart = 0,
            distToFinish = manhattDist(start, finish),
            prev = null
        )
        info[vert[start]!!]?.let { prQueue.add(it) }
        while (prQueue.isNotEmpty()) {
            val u = prQueue.poll()
            allTrace.add(u.curr!!.coord)
            for (v in u.curr!!.neighbors)
                if (!info[v.first]!!.visited) {
                    info[v.first] = AStarHelp(
                        curr = v.first, visited = true, distToStart = u.distToStart + 1,
                        distToFinish = manhattDist(v.first.coord, finish), prev = u.curr!!
                    )
                    prQueue.add(info[v.first])
                }
            if (u.curr!!.coord == finish)
                break
        }

        // restoring the trace
        var curr = info[vert[finish]!!]
        while (curr!!.prev!!.coord != start){
            trace.add(curr.prev!!.coord)
            curr = info[vert[curr.prev!!.coord]!!]
        }
        trace.add(finish)
        trace.add(start)
    }

}