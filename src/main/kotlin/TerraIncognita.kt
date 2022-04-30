fun main() {
    val maze = Maze(45, 45)
    maze.newRandomMaze()
    maze.dispelMaze(50.0)

    val twoCoords = maze.getTwoRandCoords()
    println("maze size: ${maze.height} ${maze.width}\n" +
            "start: ${twoCoords.firstCoord}\nfinish:  ${twoCoords.secondCoord}")

//    maze.printMaze()

    val mazeGraph = maze.graphFromMaze()
    val bfsPath = bfs(mazeGraph, twoCoords.firstCoord, twoCoords.secondCoord)
    maze.printPath(bfsPath)
    maze.printMaze()
}

fun bfs(mazeGraph: Graph, start: Pair<Int, Int>, finish: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
    val info = mutableMapOf<Graph.Vertex, BFS>()
    val deque = java.util.ArrayDeque<Graph.Vertex?>()
    val vert = mazeGraph.vertices

    for (v in vert)
        info[v.value] = BFS(visit = false, prev = null)

    info[vert[start]!!] = BFS(visit = true, dist = 0, prev = null)
    vert[start]?.let { deque.addFirst(it) }

    while (deque.isNotEmpty()) {
        val u = deque.pollFirst()
        for (v in u!!.neighbors) {
            if (!info[v.first]!!.visit) {
                info[v.first] = BFS(true, info[u]!!.dist + 1, u)
                deque.add(v.first)
            }
        }
    }

    val path = mutableListOf<Pair<Int, Int>>()
    var curr = info[vert[finish]!!]
    do {
        path.add(curr!!.prev!!.coord)
        curr = info[vert[curr.prev!!.coord]!!]
    }
    while (curr!!.prev!!.coord != start)
    path.add(finish)
    path.add(start)

    return path
}

