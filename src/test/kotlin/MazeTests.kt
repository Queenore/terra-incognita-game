import core.Cell
import core.Matrix
import core.Maze
import org.junit.*
import java.lang.reflect.Field
import kotlin.test.assertTrue

class MazeTests {

    @Test
    fun getGraphFormMazeTest() {
        val maze = Maze(33, 33)
        maze.newRandomMaze()
        maze.dispelMaze(50.0)
        val graph = maze.graphFromMaze(0.5)

        val m: Field = maze.javaClass.getDeclaredField("matrix")
        m.trySetAccessible()
        val matrix = (m.get(maze)) as Matrix

        graph.vertices.forEach {
            assertTrue {
                matrix.get(it.value.coord.first, it.value.coord.second) == Cell.Empty
            }
            it.value.neighbors.forEach { it1 ->
                assertTrue {
                    matrix.get(it1.key.coord.first, it1.key.coord.second) == Cell.Empty
                }
            }
        }
    }

}