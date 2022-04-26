class Matrix(private val height: Int, private val width: Int) {

    private val list = List(height + 2) { MutableList(width + 2) { '`' } }

    init {
        for (row in 0..height + 1) {
            for (column in 0..width + 1) {
                list[row][column] = '#'
            }
        }
    }

    fun get(x: Int, y: Int): Char {
        if (x !in 1..height || y !in 1..width)
            return ' '
        return list[x][y]
    }

    fun set(x: Int, y: Int, elem: Char) {
        if (x !in 1..height || y !in 1..width)
            return
        else list[x][y] = elem
    }

    fun printMatrix() {
        for (row in 0..height + 1) {
            for (column in 0..width + 1) {
                    print(list[row][column])
                if (column < width + 1)
                    print("  ")
            }
            println()
        }
    }

}