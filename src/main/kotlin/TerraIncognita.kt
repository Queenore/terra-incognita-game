import algorithms.AStar
import algorithms.AntAlg
import algorithms.BFS
import core.Maze

fun main() {
    example()
    performTest()
}

fun example(){
    // generate maze, dispel it, getting a graph based on a maze
    val maze = Maze(25, 25)
    maze.newRandomMaze()
    maze.dispelMaze(45.0)
    val twoCoords = maze.getTwoRandCoords()
    println("maze size: ${maze.height} ${maze.width}\n" +
            "start: ${twoCoords.firstCoord}\nfinish:  ${twoCoords.secondCoord}\n")

    val bfs = BFS(maze, twoCoords, 0.5)
    bfs.solve()
    bfs.printSolution()
    val aStar = AStar(maze, twoCoords, 0.5)
    aStar.solve()
    aStar.printSolution()
    val antAlg = AntAlg(maze, twoCoords)
    antAlg.solve()
    antAlg.printSolution()
}

fun performTest() {
    val bfsAStarRange = (11..151)
    val antAlgRange = (7..41)
    val iterNumber = 50
    println("\nInformation about the execution time\n" +
            "($iterNumber iterations,\n" +
            "maze size for bfs and A* in range $bfsAStarRange,\n" +
            "maze size for ant algorithm in range $antAlgRange)\n" +
            "############################################################")
    var bfsTime: Long = 0
    var aStarTime: Long = 0
    for (i in 1..iterNumber) {
        var rnd = bfsAStarRange.random()
        if (rnd % 2 == 0)
            rnd -= 1
        val maze = Maze(rnd, rnd)
        maze.newRandomMaze()
        maze.dispelMaze(45.0)
        val twoCoords = maze.getTwoRandCoords()
        val bfs = BFS(maze, twoCoords, 0.5)
        bfs.solve()
        bfsTime += bfs.getExecutionTime()
        val aStar = AStar(maze, twoCoords, 0.5)
        aStar.solve()
        aStarTime += aStar.getExecutionTime()
    }
    println("average bfs execution time = ${bfsTime.toDouble() / iterNumber} milliseconds\n")
    println("average A* execution time = ${aStarTime.toDouble() / iterNumber} milliseconds")

    var antAlgTime: Long = 0
    var minAntExecTime = Long.MAX_VALUE
    var maxAntExecTime: Long = 0
    for (i in 1..iterNumber) {
        var rnd = antAlgRange.random()
        if (rnd % 2 == 0)
            rnd -= 1
        val maze = Maze(rnd, rnd)
        maze.newRandomMaze()
        maze.dispelMaze(45.0)
        val twoCoords = maze.getTwoRandCoords()
        val antAlg = AntAlg(maze, twoCoords)
        antAlg.solve()
        val execTime = antAlg.getExecutionTime()
        antAlgTime += execTime
        if (execTime < minAntExecTime)
            minAntExecTime = execTime
        if (execTime > maxAntExecTime)
            maxAntExecTime = execTime
    }
    println("\naverage ant algorithm execution time: ${antAlgTime.toDouble() / iterNumber} milliseconds\n" +
            "\tmin ant algorithm execution time: $minAntExecTime milliseconds\n" +
            "\tmax ant algorithm execution time: $maxAntExecTime milliseconds\n" +
            "############################################################\n")
}