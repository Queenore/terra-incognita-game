fun main() {
    // generate maze, dispel it, getting a graph based on a maze
        val maze = Maze(25, 25)
        maze.newRandomMaze()
        maze.dispelMaze(45.0)
        val twoCoords = maze.getTwoRandCoords()
        println("maze size: ${maze.height} ${maze.width}\n" +
                "start: ${twoCoords.firstCoord}\nfinish:  ${twoCoords.secondCoord}\n")

        val mazeGraph = maze.graphFromMaze()
        val bfs = BFS(maze, mazeGraph, twoCoords)
        val aStar = AStar(maze, mazeGraph, twoCoords)

        bfs.print()
        aStar.print()
}



