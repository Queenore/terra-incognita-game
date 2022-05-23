package algorithms

import core.Graph
import core.Maze
import core.TwoCoords

class BFS(private val maze: Maze, twoCoords: TwoCoords, edgeWeightForGraph: Double): ShortestPath {

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
            val bfsMaze = maze.deepCopy()
            bfsMaze.printTrace(trace)
            println("Algorithms.BFS: trace length = ${trace.size}")
            println("execute time: $totalExecuteTime milliseconds")
            bfsMaze.printMaze()
            println()
            bfsMaze.printTrace(allTrace)
            println("Algorithms.BFS: all trace length = ${allTrace.size}")
            bfsMaze.printMaze()
            println()
        } else throw IllegalStateException()
    }

    private data class BfsHelp(var visit: Boolean, var dist: Int = Int.MAX_VALUE, var prev: Graph.Vertex?)

    override fun solve() {
        if (!solveFlag) {
            val startTime = System.currentTimeMillis()

            val info = mutableMapOf<Graph.Vertex, BfsHelp>()
            val deque = java.util.ArrayDeque<Graph.Vertex?>()
            val vert = mazeGraph.vertices

            // bfs trace search
            vert.forEach { info[it.value] = BfsHelp(visit = false, prev = null) }
            info[vert[start]!!] = BfsHelp(visit = true, dist = 0, prev = null)
            vert[start]?.let { deque.add(it) }
            while (deque.isNotEmpty()) {
                val u = deque.pollFirst()
                allTrace.add(u!!.coord)
                for (v in u.neighbors)
                    if (!info[v.key]!!.visit) {
                        info[v.key] = BfsHelp(true, info[u]!!.dist + 1, u)
                        deque.add(v.key)
                    }
                if (u.coord == finish)
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