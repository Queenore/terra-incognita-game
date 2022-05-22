package algorithms

import core.Maze
import core.Operations
import core.TwoCoords
import kotlin.random.Random

class AntAlg(private val maze: Maze, twoCoords: TwoCoords) : ShortestPath {

    private val start = twoCoords.firstCoord
    private val finish = twoCoords.secondCoord
    private val mazeGraph = maze.graphFromMaze(0.5)
    private val trace = mutableListOf<Pair<Int, Int>>()
    private var totalExecuteTime: Long = 0
    private val vert = mazeGraph.vertices
    private val antsNumber = vert.size
    private val attempts = 100
    private var solveFlag = false

    override fun getTraceLength(): Int {
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
            println("number of ants: $antsNumber")
            println("attempts: $attempts")
            println("execute time: $totalExecuteTime milliseconds")
            println("trace length: ${trace.size}")
            maze.printTrace(trace)
            maze.printMaze()
        } else throw IllegalStateException()
    }

    private data class Prob(val probPair: Pair<Double, Double>, val coord: Pair<Int, Int>)

    private fun chooseNextCoord(curr: Pair<Int, Int>, visited: MutableMap<Pair<Int, Int>, Boolean>): Pair<Int, Int> {
        var allowedProb = 0.0
        var prevProb = 0.0

        vert[curr]!!.neighbors.forEach {
            if (!visited[it.key.coord]!!)
                allowedProb += it.value
        }

        if (allowedProb != 0.0) {
            val listOfProb = mutableListOf<Prob>()
            vert[curr]!!.neighbors.forEach {
                if (!visited[it.key.coord]!!) {
                    val newProb = it.value / allowedProb
                    listOfProb.add(Prob(Pair(prevProb, prevProb + newProb), it.key.coord))
                    prevProb += newProb
                }
            }
            val rnd = Random.nextDouble(0.0, listOfProb.last().probPair.second)
            listOfProb.forEach {
                if (rnd in it.probPair.first..it.probPair.second)
                    return it.coord
            }
        }
        return Pair(-1, -1)
    }

    override fun solve() {
        if (!solveFlag) {
            val startTime = System.currentTimeMillis()

            for (attempt in 1..attempts) {
                val traces = List(antsNumber) { mutableListOf<TwoCoords>() }
                for (ant in 0 until antsNumber) {
                    var dist = 0
                    var curr = start
                    var prev: Pair<Int, Int>
                    val visited = mutableMapOf<Pair<Int, Int>, Boolean>()
                    vert.forEach { visited[it.value.coord] = false }
                    while (curr != finish) {
                        prev = curr
                        curr = chooseNextCoord(curr, visited)
                        if (curr == Pair(-1, -1))
                            break // kill ant
                        visited[curr] = true
                        traces[ant].add(TwoCoords(prev, curr))
                        dist++
                    }
                }

                var shortestPath = Int.MAX_VALUE
                for (trace in traces) {
                    if (trace.last().secondCoord == finish && trace.size <= shortestPath) {
                        for (coords in trace)
                            mazeGraph.changeWeight(coords.firstCoord, coords.secondCoord,
                                1.0 / trace.size, Operations.PLus)
                        if (shortestPath > trace.size)
                            shortestPath = trace.size
                    }
                }
                mazeGraph.changeAllWeights(0.5, Operations.Times)
            }

            // restore the trace
            var curr = start
            val visited = mutableListOf<Pair<Int, Int>>()
            trace.add(curr)
            visited.add(curr)
            while (curr != finish) {
                var maxProb = 0.0
                vert[curr]!!.neighbors.forEach {
                    if (it.value > maxProb && !visited.contains(it.key.coord)) {
                        curr = it.key.coord
                        maxProb = it.value
                    }
                }
                trace.add(curr)
                visited.add(curr)
            }

            totalExecuteTime = System.currentTimeMillis() - startTime
        }
        solveFlag = true
    }

}