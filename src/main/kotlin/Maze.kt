class Maze(height: Int, width: Int) {

    private val matrix = Matrix(height, width)
    private val possibleSteps = mutableSetOf<Step>()

    fun newRandomMaze() {
        generateMaze(matrix)
    }

    fun printMaze() {
        matrix.printMatrix()
    }

    private fun generateMaze(matrix: Matrix) {
        val startCoord = Pair(1, 1)

        possibleSteps.add(Step(Pair(0, 0), startCoord))
        updatePossibleSteps(Step(Pair(0, 0), startCoord))
        matrix.set(startCoord.first, startCoord.second, '!')

        while (possibleSteps.isNotEmpty()) {
            val currStep = getRandNextCell()
            matrix.set(currStep.nextCoord.first, currStep.nextCoord.second, ' ')
            establishConnect(currStep)
            updatePossibleSteps(currStep)
        }
    }

    private fun getRandNextCell(): Step {
        val rndIndex = (0 until possibleSteps.size).random()
        for ((index, element) in possibleSteps.withIndex())
            if (rndIndex == index)
                return element
        return Step(Pair(-1, -1), Pair(-1, -1))
    }

    private fun establishConnect(step: Step) {
        val prev = step.prevCoord
        val next = step.nextCoord
        if (prev.first - next.first != 0) {
            matrix.set(if (prev.first > next.first) next.first + 1 else prev.first + 1, prev.second, ' ')
        } else if (prev.second - next.second != 0) {
            matrix.set(prev.first, if (prev.second > next.second) next.second + 1 else prev.second + 1, ' ')
        }
    }

    private fun updatePossibleSteps(step: Step) {
        val nextCoord = step.nextCoord
        for (dir in 1..4) {
            val firstIncDec = when (dir) {
                1 -> 2
                2 -> 0
                3 -> -2
                else -> 0
            }
            val secondIncDec = when (dir) {
                1 -> 0
                2 -> 2
                3 -> 0
                else -> -2
            }
            if (matrix.get(nextCoord.first + firstIncDec, nextCoord.second + secondIncDec) == '#'
                && matrix.get(nextCoord.first + firstIncDec, nextCoord.second + secondIncDec) != ' '
            )
                possibleSteps.add(Step(nextCoord, Pair(nextCoord.first + firstIncDec, nextCoord.second + secondIncDec)))
        }
        val iterator = possibleSteps.iterator()
        while (iterator.hasNext())
           if (iterator.next().nextCoord == nextCoord)
               iterator.remove()
    }

}