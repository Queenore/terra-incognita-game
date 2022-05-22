package algorithms

interface ShortestPath {

    /**
     * Returns the length of the shortest path.
     * @throws IllegalStateException if it was called before solve() call
     */
    fun getTraceLength(): Int

    /**
     * Returns the execution time of solve() function
     * @throws IllegalStateException if it was called before solve() call
     */
    fun getExecutionTime(): Long

    /**
     * Outputs to the console the results of solve() in graphical form:
     * the shortest path, the full route.
     * @throws IllegalStateException if it was called before solve() call
     */
    fun printSolution()

    /**
     * Must implement finding the shortest path in the maze,
     * which is an instance of the Core.Maze.kt class.
     */
    fun solve()

}