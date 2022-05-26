package core

class Graph {

    var vertexNumb = 0

    data class Vertex(val coord: Pair<Int, Int>) {
        val neighbors = mutableMapOf<Vertex, Double>()
    }

    val vertices = hashMapOf<Pair<Int, Int>, Vertex>()

    private operator fun get(coord: Pair<Int, Int>) = vertices[coord] ?: throw IllegalArgumentException()

    fun addVertex(coord: Pair<Int, Int>) {
        vertices[coord] = Vertex(coord)
        vertexNumb = vertices.size
    }

    fun changeWeight(first: Pair<Int, Int>, second: Pair<Int, Int>, number: Double, oper: Operations) =
        changeWeight(this[first], this[second], number, oper)

    private fun changeWeight(first: Vertex, second: Vertex, number: Double, oper: Operations) {
        val prevWeight = first.neighbors[second]
        val newWeight = when (oper) {
            Operations.Plus -> prevWeight?.plus(number)
            Operations.Minus -> prevWeight?.minus(number)
            Operations.Times -> prevWeight?.times(number)
            else -> prevWeight?.div(number)
        }
        first.neighbors[second] = newWeight!!
        second.neighbors[first] = newWeight
    }

    fun changeAllWeights(number: Double, oper: Operations) {
        for (v in vertices)
            for (u in v.value.neighbors)
                u.setValue(
                    when (oper) {
                        Operations.Plus -> u.value + number
                        Operations.Minus -> u.value - number
                        Operations.Times -> u.value * number
                        else -> u.value / number
                    }
                )
    }

    fun connect(first: Pair<Int, Int>, second: Pair<Int, Int>, weight: Double) =
        connect(this[first], this[second], weight)

    private fun connect(first: Vertex, second: Vertex, weight: Double) {
        first.neighbors[second] = weight
        second.neighbors[first] = weight
    }

}