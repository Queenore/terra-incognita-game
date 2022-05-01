import kotlin.math.abs

fun main() {
    // generate maze, dispel it, getting a graph based on a maze
    val maze = Maze(25, 25)
    maze.newRandomMaze()
    maze.dispelMaze(45.0)
    val twoCoords = maze.getTwoRandCoords()
    println("maze size: ${maze.height} ${maze.width}\n" +
            "start: ${twoCoords.firstCoord}\nfinish:  ${twoCoords.secondCoord}\n")
    val mazeGraph = maze.graphFromMaze()

    val bfsMazePath: Maze = maze.copy()
    val bfsMazeAllTrace: Maze = maze.copy()
    val aStarMazePath: Maze = maze.copy()
    val aStarMazeAllTrace: Maze = maze.copy()

    val bfs = bfs(mazeGraph, twoCoords.firstCoord, twoCoords.secondCoord)
    bfsMazePath.printPath(bfs.first)
    println("BFS: path length = ${bfs.first.size}")
    bfsMazePath.printMaze()
    println()
    bfsMazeAllTrace.printPath(bfs.second)
    println("BFS: all trace length = ${bfs.second.size}")
    bfsMazeAllTrace.printMaze()
    println()

    val aStar = aStar(mazeGraph, twoCoords.firstCoord, twoCoords.secondCoord)
    aStarMazePath.printPath(aStar.first)
    println("A*: path length = ${aStar.first.size}")
    aStarMazePath.printMaze()
    println()
    aStarMazeAllTrace.printPath(aStar.second)
    println("A*: all trace length = ${aStar.second.size}")
    aStarMazeAllTrace.printMaze()
    println()
}

fun bfs(
    mazeGraph: Graph,
    start: Pair<Int, Int>,
    finish: Pair<Int, Int>,
): Pair<MutableList<Pair<Int, Int>>, MutableList<Pair<Int, Int>>> {
    val info = mutableMapOf<Graph.Vertex, BfsHelp>()
    val deque = java.util.ArrayDeque<Graph.Vertex?>()
    val vert = mazeGraph.vertices
    val allTrace = mutableListOf<Pair<Int, Int>>()

    // bfs path search
    vert.forEach { info[it.value] = BfsHelp(visit = false, prev = null) }
    info[vert[start]!!] = BfsHelp(visit = true, dist = 0, prev = null)
    vert[start]?.let { deque.add(it) }
    while (deque.isNotEmpty()) {
        val u = deque.pollFirst()
        allTrace.add(u!!.coord)
        for (v in u.neighbors)
            if (!info[v.first]!!.visit) {
                info[v.first] = BfsHelp(true, info[u]!!.dist + 1, u)
                deque.add(v.first)
            }
        if (u.coord == finish)
            break
    }

    // restoring the path
    val path = mutableListOf<Pair<Int, Int>>()
    var curr = info[vert[finish]!!]
    while (curr!!.prev!!.coord != start) {
        path.add(curr.prev!!.coord)
        curr = info[vert[curr.prev!!.coord]!!]
    }
    path.add(finish)
    path.add(start)

    return Pair(path, allTrace)
}

fun manhattDist(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

fun aStar(
    mazeGraph: Graph,
    start: Pair<Int, Int>,
    finish: Pair<Int, Int>,
): Pair<MutableList<Pair<Int, Int>>, MutableList<Pair<Int, Int>>> {
    val info = mutableMapOf<Graph.Vertex, AStarHelp>()
    val compareByDist: Comparator<AStarHelp> = compareBy { it.distToStart + it.distToFinish }
    val prQueue = java.util.PriorityQueue(compareByDist)
    val vert = mazeGraph.vertices
    val allTrace = mutableListOf<Pair<Int, Int>>()

    // A* path search, using PriorityQueue
    vert.forEach { info[it.value] = AStarHelp(curr = null, visited = false, prev = null) }
    info[vert[start]!!] = AStarHelp(
        curr = vert[start]!!, visited = false, distToStart = 0, distToFinish = manhattDist(start, finish), prev = null
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

    // restoring the path
    val path = mutableListOf<Pair<Int, Int>>()
    var curr = info[vert[finish]!!]
    while (curr!!.prev!!.coord != start) {
        path.add(curr.prev!!.coord)
        curr = info[vert[curr.prev!!.coord]!!]
    }
    path.add(finish)
    path.add(start)

    return Pair(path, allTrace)
}
