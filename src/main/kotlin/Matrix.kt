class Matrix(private val height: Int, private val width: Int) {

    private val list = List(height + 2) { MutableList(width + 2) { Cell.Wall } }

    fun get(row: Int, column: Int): Cell {
        if (row !in 1..height || column !in 1..width)
            throw IllegalArgumentException()
        return list[row][column]
    }

    fun set(row: Int, column: Int, cell: Cell) {
        if (row !in 1..height || column !in 1..width)
            throw IllegalArgumentException()
        list[row][column] = cell
    }

    override fun toString(): String {
        val str = StringBuilder()
        for (row in 0..height + 1) {
            for (column in 0..width + 1) {
                str.append(when (list[row][column]) {
                    Cell.Trace -> "."
                    Cell.Wall -> "@"
                    else -> " "
                })
                if (column < width + 1)
                    str.append("  ")
            }
            str.append("\n")
        }
        return str.toString()
    }

}