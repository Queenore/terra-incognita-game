class Graph {

    data class Vertex(val coord: Pair<Int, Int>) {
        val neighbors = mutableSetOf<Pair<Vertex, Int>>()
    }

    val vertices = mutableMapOf<Pair<Int, Int>, Vertex>()

    private operator fun get(coord: Pair<Int, Int>) = vertices[coord] ?: throw IllegalArgumentException()

    fun addVertex(coord: Pair<Int, Int>) {
        vertices[coord] = Vertex(coord)
    }

    private fun connect(first: Vertex, second: Vertex, weight: Int) {
        first.neighbors.add(Pair(second, weight))
        second.neighbors.add(Pair(first, weight))
    }

    fun connect(first: Pair<Int, Int>, second: Pair<Int, Int>, weight: Int) = connect(this[first], this[second], weight)

}