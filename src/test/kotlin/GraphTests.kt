import core.Graph
import core.Operations
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GraphTests {

    @Test
    fun addVertexTest() {
        val graph = Graph()
        val listOfVertices = mutableListOf<Pair<Int, Int>>()
        for (i in 1..100) {
            val firstRnd = (0..200).random()
            val secondRnd = (0..200).random()
            if (!listOfVertices.contains(Pair(firstRnd, secondRnd))) {
                graph.addVertex(Pair(firstRnd, secondRnd))
                listOfVertices.add(Pair(firstRnd, secondRnd))
            }
        }
        graph.vertices.forEach {
            assertTrue {
                listOfVertices.contains(it.value.coord)
            }
        }
        assertEquals(graph.vertexNumb, listOfVertices.size)
    }

    @Test
    fun connectTest() {
        val graph = Graph()
        val firstCoords = Pair((0..200).random(), (0..200).random())
        val secondCoords = Pair((0..200).random(), (0..200).random())
        graph.addVertex(firstCoords)
        graph.addVertex(secondCoords)
        graph.connect(firstCoords, secondCoords, 0.5)
        assertTrue {
            graph.vertices[firstCoords]!!
                .neighbors.containsKey(Graph.Vertex(secondCoords))
        }
        assertTrue {
            graph.vertices[secondCoords]!!
                .neighbors.containsKey(Graph.Vertex(firstCoords))
        }
    }

    @Test
    fun changeWeightTest() {
        val graph = Graph()
        val firstCoords = Pair((0..200).random(), (0..200).random())
        val secondCoords = Pair((0..200).random(), (0..200).random())
        graph.addVertex(firstCoords)
        graph.addVertex(secondCoords)
        graph.connect(firstCoords, secondCoords, 0.5)
        graph.changeWeight(firstCoords, secondCoords, 4.0, Operations.Times)
        assertTrue {
            graph.vertices[firstCoords]!!
                .neighbors[Graph.Vertex(secondCoords)] == 2.0
        }
        assertTrue {
            graph.vertices[secondCoords]!!
                .neighbors[Graph.Vertex(firstCoords)] == 2.0
        }
    }

    @Test
    fun changeAllWeightsTest() {
        val graph = Graph()

        val first = Pair(1, 2)
        val second = Pair(3, 4)
        val third = Pair(23, 12)
        val fourth = Pair(14, 4)
        val fifth = Pair(18, 34)

        graph.addVertex(first)
        graph.addVertex(second)
        graph.addVertex(third)
        graph.addVertex(fourth)
        graph.addVertex(fifth)

        graph.connect(first, fourth, 0.4)
        graph.connect(second, third, 0.4)
        graph.connect(third, fifth, 0.4)
        graph.changeAllWeights(0.6, Operations.PLus)

        graph.vertices.forEach {
            it.value.neighbors.forEach {it1 ->
                assertTrue {
                    it1.value == 1.0
                }
            }
        }
    }

}