class BFS(private val maze: Maze, private val mazeGraph: Graph, twoCoords: TwoCoords) {

    private val trace = mutableListOf<Pair<Int, Int>>()
    private val allTrace = mutableListOf<Pair<Int, Int>>()
    private val start = twoCoords.firstCoord
    private val finish = twoCoords.secondCoord

    init {
        bfsSolve()
    }

    fun print() {
        val bfsMaze = maze.deepCopy()
        bfsMaze.printTrace(trace)
        println("BFS: trace length = ${trace.size}")
        bfsMaze.printMaze()
        println()
        bfsMaze.printTrace(allTrace)
        println("BFS: all trace length = ${allTrace.size}")
        bfsMaze.printMaze()
        println()
    }

    private data class BfsHelp(var visit: Boolean, var dist: Int = Int.MAX_VALUE, var prev: Graph.Vertex?)

    private fun bfsSolve() {
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
                if (!info[v.first]!!.visit) {
                    info[v.first] = BfsHelp(true, info[u]!!.dist + 1, u)
                    deque.add(v.first)
                }
            if (u.coord == finish)
                break
        }

        // restoring the trace
        var curr = info[vert[finish]!!]
        while (curr!!.prev!!.coord != start){
            trace.add(curr.prev!!.coord)
            curr = info[vert[curr.prev!!.coord]!!]
        }
        trace.add(finish)
        trace.add(start)
    }

}