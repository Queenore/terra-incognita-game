import core.Cell
import core.Matrix
import org.junit.*
import kotlin.test.assertTrue

class MatrixTests {

    @Test
    fun setAndGetTest() {
        val height = 101
        val width = 101
        for (i in 1..100) {
            val matrix = Matrix(height, width)
            val firstRnd = (1..height).random()
            val secondRnd = (1..width).random()
            matrix.set(firstRnd, secondRnd,Cell.Wall)
            assertTrue { matrix.get(firstRnd, secondRnd) == Cell.Wall }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalSetCoordsTest() {
        val height = 101
        val width = 101
        val matrix = Matrix(height, width)
        matrix.get(0, 95)
    }

    @Test(expected = IllegalArgumentException::class)
    fun illegalGetCoordsTest() {
        val height = 101
        val width = 101
        val matrix = Matrix(height, width)
        matrix.get(120, 12)
    }

}