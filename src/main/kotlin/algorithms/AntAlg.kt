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
    private val attempts = 70
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
            println("number of ants: $antsNumber")
            println("attempts: $attempts")
            println("execute time: $totalExecuteTime milliseconds")
            println("trace length: ${trace.size}")
            maze.printTrace(trace)
            maze.printMaze()
        } else throw IllegalStateException()
    }

    private data class Prob(val probPair: Pair<Double, Double>, val coord: Pair<Int, Int>)

    /**
     * Функция, которая с учетом весов ребер возвращает новую координату.
     * Чем больше вес, тем больше вероятность, что муравей пойдет именно по этому ребру.
     */
    private fun chooseNextCoord(curr: Pair<Int, Int>, visited: MutableMap<Pair<Int, Int>, Boolean>): Pair<Int, Int> {
        var allowedProb = 0.0
        vert[curr]!!.neighbors.forEach {
            if (!visited[it.key.coord]!!)
                allowedProb += it.value
        } // вычисление суммы весов ребер, по которым может пойти муравей

        var prevProb = 0.0
        if (allowedProb != 0.0) { // если муравей не попал в тупик, то выбираем новую координату с учетом весов ребер
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
                    var curr = start
                    var prev: Pair<Int, Int>
                    val visited = mutableMapOf<Pair<Int, Int>, Boolean>()
                    vert.forEach { visited[it.value.coord] = false } // map посещенных или нет вершин
                    while (curr != finish) { // выстраивание маршрута n-го муравья
                        prev = curr
                        curr = chooseNextCoord(curr, visited) // выбираем следующую координату
                        if (curr == Pair(-1, -1))
                            break // если муравей попал в тупик, то прекращаем поиск
                        visited[curr] = true
                        traces[ant].add(TwoCoords(prev, curr)) // запоминаем путь пройденный конкретным муравьем
                    }
                }

                // увеличиваем веса ребер, соответствующих маршрутам муравьев, которые достигли финишной точки
                // (добавляем обратно пропорциональное длине пройденного пути количество феромона)
                var shortestPath = Int.MAX_VALUE
                for (trace in traces) {
                    if (trace.isNotEmpty() && trace.last().secondCoord == finish && trace.size <= shortestPath) {
                        for (coords in trace)
                            mazeGraph.changeWeight(coords.firstCoord, coords.secondCoord,
                                1.0 / trace.size, Operations.PLus)
                        if (shortestPath > trace.size)
                            shortestPath = trace.size
                    }
                }
                mazeGraph.changeAllWeights(0.5, Operations.Times) // испаряем часть феромонов со всех ребер
            }

            // восстанавливаем самый короткий путь
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