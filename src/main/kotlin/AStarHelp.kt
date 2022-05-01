data class AStarHelp(
    var curr: Graph.Vertex?,
    var visited: Boolean,
    var distToStart: Int = Int.MAX_VALUE,
    var distToFinish: Int = Int.MAX_VALUE,
    var prev: Graph.Vertex?,
)
