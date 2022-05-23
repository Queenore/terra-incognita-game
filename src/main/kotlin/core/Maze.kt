package core

import kotlin.random.Random

data class Maze(val height: Int, val width: Int, private val matrix: Matrix = Matrix(height, width)) {

    private val possibleSteps = mutableSetOf<TwoCoords>()

    /**
     * the height and width must be an odd number;
     * also, they should not be equal to 1 at the same time
     */
    fun newRandomMaze() {
        if ((height == 1 && width == 1) || height % 2 == 0 || width % 2 == 0)
            throw IllegalArgumentException()
        generateMaze(matrix)
    }

    fun printMaze() {
        print(matrix.toString())
    }

    fun printTrace(path: MutableList<Pair<Int, Int>>) {
        path.forEach {
            matrix.set(it.first, it.second, Cell.Trace)
        }
    }

    /**
     * creating a copy of a Core.Maze with properties that do not refer to any other
     */
    fun deepCopy(): Maze {
        val maze = Maze(height, width)
        for (x in 1..height)
            for (y in 1..width)
                maze.matrix.set(x, y, this.matrix.get(x, y))
        return maze
    }

    fun getTwoRandCoords(): TwoCoords {
        return matrix.twoRandCoords()
    }

    private fun generateMaze(matrix: Matrix) {
        val startCoord = rndOddStartCoord()

        possibleSteps.add(TwoCoords(Pair(0, 0), startCoord))
        updatePossibleSteps(TwoCoords(Pair(0, 0), startCoord))

        while (possibleSteps.isNotEmpty()) {
            val currStep = getNextRandCoord()
            matrix.set(currStep.secondCoord.first, currStep.secondCoord.second, Cell.Empty)
            establishConnect(currStep)
            updatePossibleSteps(currStep)
        }
    }

    private fun rndOddStartCoord(): Pair<Int, Int> {
        var x = 0
        var y = 0
        while (x % 2 == 0)
            x = (1..height).random()
        while (y % 2 == 0)
            y = (1..width).random()
        return Pair(x, y)
    }

    private fun getNextRandCoord(): TwoCoords {
        val rndIndex = (0 until possibleSteps.size).random()
        for ((index, element) in possibleSteps.withIndex())
            if (rndIndex == index)
                return element
        return TwoCoords(Pair(-1, -1), Pair(-1, -1))
    }

    private fun establishConnect(step: TwoCoords) {
        val prev = step.firstCoord
        val next = step.secondCoord
        if (prev.first - next.first != 0)
            matrix.set(if (prev.first > next.first) next.first + 1 else prev.first + 1, prev.second, Cell.Empty)
        else if (prev.second - next.second != 0)
            matrix.set(prev.first, if (prev.second > next.second) next.second + 1 else prev.second + 1, Cell.Empty)
    }

    private fun updatePossibleSteps(step: TwoCoords) {
        val nextCoord = step.secondCoord

        for (dir in 1..4) {
            val (newX, newY) = getNewCoords(nextCoord, dir, 2)
            if (newX in 1..height && newY in 1..width && matrix.get(newX, newY) == Cell.Wall)
                possibleSteps.add(TwoCoords(nextCoord, Pair(newX, newY)))
        }

        val iterator = possibleSteps.iterator()
        while (iterator.hasNext())
            if (iterator.next().secondCoord == nextCoord)
                iterator.remove()
    }

    /**
     * a function that generates new coordinates in the up, down, left, right directions
     */
    private fun getNewCoords(currCoord: Pair<Int, Int>, dir: Int, dist: Int): Pair<Int, Int> {
        val first = currCoord.first + when (dir) {
            1 -> dist
            2 -> 0
            3 -> -dist
            else -> 0
        }
        val second = currCoord.second + when (dir) {
            1 -> 0
            2 -> dist
            3 -> 0
            else -> -dist
        }
        return Pair(first, second)
    }

    /**
     * prob must be in the range 0..100
     */
    fun dispelMaze(prob: Double) {
        if (prob !in 0.0..100.0)
            throw IllegalArgumentException("prob must be in the range 0..100")

        for (x in 1..height)
            for (y in 1..width) {
                var count = 0
                for (dir in 1..4) {
                    val (newX, newY) = getNewCoords(Pair(x, y), dir, 1)
                    if (newX in 1..height && newY in 1..width && matrix.get(newX, newY) == Cell.Wall)
                        count++
                }
                if (Random.nextDouble(0.0, 100.0) in 0.0..prob && count < 4)
                    matrix.set(x, y, Cell.Empty)
            }
    }

    fun graphFromMaze(edgeWeight: Double): Graph {
        val graph = Graph()

        for (x in 1..height)
            for (y in 1..width) {
                val firstIsEmpty = matrix.get(x, y) == Cell.Empty
                for (dir in 1..4) {
                    val (newX, newY) = getNewCoords(Pair(x, y), dir, 1)
                    if (!graph.vertices.containsKey(Pair(x, y)) && firstIsEmpty)
                        graph.addVertex(Pair(x, y))
                    if (newX in 1..height && newY in 1..width) {
                        val secondIsEmpty = matrix.get(newX, newY) == Cell.Empty
                        if (!graph.vertices.containsKey(Pair(newX, newY)) && secondIsEmpty)
                            graph.addVertex(Pair(newX, newY))
                        if (firstIsEmpty && secondIsEmpty)
                            graph.connect(Pair(x, y), Pair(newX, newY), edgeWeight)
                    }
                }
            }

        return graph
    }

}