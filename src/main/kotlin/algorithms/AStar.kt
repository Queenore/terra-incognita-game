package algorithms

import core.Graph
import core.Maze
import core.TwoCoords
import kotlin.math.abs

class AStar(private val maze: Maze, twoCoords: TwoCoords, edgeWeightForGraph: Double) : ShortestPath {

    private val start = twoCoords.firstCoord
    private val finish = twoCoords.secondCoord
    private val trace = mutableListOf<Pair<Int, Int>>()
    private val allTrace = mutableListOf<Pair<Int, Int>>()
    private var totalExecuteTime: Long = 0
    private var mazeGraph = maze.graphFromMaze(edgeWeightForGraph)
    private var solveFlag = false

    override fun getPathLength(): Int {
        if (solveFlag)
            return trace.size
        else throw IllegalStateException()
    }

    override fun getExecutionTime(): Long {
        if (solveFlag)
            return totalExecuteTime
        else throw IllegalStateException()
    }

    override fun printSolution() {
        if (solveFlag) {
            val aStarMaze = maze.deepCopy()
            aStarMaze.printTrace(trace)
            println("A*: trace length = ${trace.size}")
            println("execute time: $totalExecuteTime milliseconds")
            aStarMaze.printMaze()
            println()
            aStarMaze.printTrace(allTrace)
            println("A*: all trace length = ${allTrace.size}")
            aStarMaze.printMaze()
            println()
        } else throw IllegalStateException()
    }

    private data class AStarHelp(
        var curr: Graph.Vertex?,
        var distToStart: Int = 10000000,
        var distToFinish: Int = 10000000,
        var prev: Graph.Vertex? = null,
    )

    private fun manhattDist(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        abs(a.first - b.first) + abs(a.second - b.second)

    override fun solve() {
        if (!solveFlag) {
            val startTime = System.currentTimeMillis()

            val info = mutableMapOf<Graph.Vertex, AStarHelp>()
            val compareByDist: Comparator<AStarHelp> = compareBy { it.distToStart + it.distToFinish }
            val prQueue = java.util.PriorityQueue(compareByDist)
            val vert = mazeGraph.vertices

            // A* trace search, using PriorityQueue
            vert.forEach { info[it.value] = AStarHelp(null, prev = null) }
            info[vert[start]!!] = AStarHelp(vert[start]!!, 0, manhattDist(start, finish))
            info[vert[start]!!]?.let { prQueue.add(it) }
            while (prQueue.isNotEmpty()) {
                val u = prQueue.poll()
                allTrace.add(u.curr!!.coord)
                for (v in u.curr!!.neighbors) {
                    val newStartDist = u.distToStart + 1
                    val newFinishDist = manhattDist(v.key.coord, finish)
                    if (newStartDist + newFinishDist < info[v.key]!!.distToStart + info[v.key]!!.distToFinish) {
                        info[v.key] = AStarHelp(v.key, newStartDist, newFinishDist, u.curr!!)
                        prQueue.add(info[v.key])
                    }
                }
                if (u.curr!!.coord == finish)
                    break
            }

            // восстанавливаем самый короткий путь
            var curr = info[vert[finish]!!]
            while (curr!!.prev!!.coord != start) {
                trace.add(curr.prev!!.coord)
                curr = info[vert[curr.prev!!.coord]!!]
            }
            trace.add(finish)
            trace.add(start)

            totalExecuteTime = System.currentTimeMillis() - startTime
        }
        solveFlag = true
    }

}