import kotlin.random.Random

class Maze(val height: Int, val width: Int) {

    private val matrix = Matrix(height, width)
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
        matrix.printMatrix()
    }

    fun printPath(path: MutableList<Pair<Int, Int>>) {
        path.forEach {
            matrix.set(it.first, it.second, '.')
        }
    }

    fun copy(): Maze {
        val maze = Maze(height, width)
        for (x in 1..height)
            for (y in 1..width)
                maze.matrix.set(x, y, this.matrix.get(x, y))
        return maze
    }

    /**
     * prob must be in the range 0..100
     */
    fun dispelMaze(prob: Double) {
        if (prob !in 0.0..100.0)
            throw IllegalArgumentException()
        for (x in 1..height)
            for (y in 1..width)
                if (Random.nextDouble(0.0, 100.0) in 0.0..prob)
                    matrix.set(x, y, ' ')
    }

    fun getTwoRandCoords(): TwoCoords {
        var coord1: Pair<Int, Int>
        var coord2: Pair<Int, Int>

        do coord1 = Pair((1..height).random(), (1..width / 2).random())
        while (matrix.get(coord1.first, coord1.second) != ' ')
        do coord2 = Pair((1..height).random(), (width / 2 + 1..width).random())
        while (matrix.get(coord2.first, coord2.second) != ' ')

        return TwoCoords(coord1, coord2)
    }

    private fun generateMaze(matrix: Matrix) {
        val startCoord = rndOddStartCoord()

        possibleSteps.add(TwoCoords(Pair(0, 0), startCoord))
        updatePossibleSteps(TwoCoords(Pair(0, 0), startCoord))

        while (possibleSteps.isNotEmpty()) {
            val currStep = getNextRandCoord()
            matrix.set(currStep.secondCoord.first, currStep.secondCoord.second, ' ')
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
            matrix.set(if (prev.first > next.first) next.first + 1 else prev.first + 1, prev.second, ' ')
        else if (prev.second - next.second != 0)
            matrix.set(prev.first, if (prev.second > next.second) next.second + 1 else prev.second + 1, ' ')
    }

    private fun updatePossibleSteps(step: TwoCoords) {
        val nextCoord = step.secondCoord

        for (dir in 1..4) {
            val newX = nextCoord.first + when (dir) {
                1 -> 2
                2 -> 0
                3 -> -2
                else -> 0
            }
            val newY = nextCoord.second + when (dir) {
                1 -> 0
                2 -> 2
                3 -> 0
                else -> -2
            }
            if (newX in 1..height && newY in 1..width && matrix.get(newX, newY) == '@')
                possibleSteps.add(TwoCoords(nextCoord, Pair(newX, newY)))
        }

        val iterator = possibleSteps.iterator()
        while (iterator.hasNext())
            if (iterator.next().secondCoord == nextCoord)
                iterator.remove()
    }

    fun graphFromMaze(): Graph {
        val graph = Graph()

        for (x in 1..height)
            for (y in 1..width)
                for (dir in 1..4) {
                    val newX = x + when (dir) {
                        1 -> 1
                        2 -> 0
                        3 -> -1
                        else -> 0
                    }
                    val newY = y + when (dir) {
                        1 -> 0
                        2 -> 1
                        3 -> 0
                        else -> -1
                    }
                    if (newX in 1..height && newY in 1..width &&
                        matrix.get(x, y) == ' ' && matrix.get(newX, newY) == ' '
                    ) {
                        if (!graph.vertices.containsKey(Pair(x, y)))
                            graph.addVertex(Pair(x, y))
                        if (!graph.vertices.containsKey(Pair(newX, newY)))
                            graph.addVertex(Pair(newX, newY))

                        graph.connect(Pair(x, y), Pair(newX, newY), 1)

                    }
                }

        return graph
    }

}