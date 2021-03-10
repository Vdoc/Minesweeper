package minesweeper

import kotlin.random.Random

class Matrix {
    val x: Int
    val y: Int
    var minesCount = 0
    val m: Array<IntArray>

    constructor (width: Int, heigth: Int) {
        x = width
        y = heigth
        m = Array(y) { IntArray(x) }
    }

    fun getElenent(row: Int, column: Int): Int = m[row][column]

    fun generateMines(minesCount: Int) {
        while (this.minesCount != minesCount) {
            var mine = arrayOf(Random.nextInt(0, 9), Random.nextInt(0, 9))
            if (getElenent(mine[0], mine[1]) != -1) {
                this.minesCount++
                m[mine[0]][mine[1]] = -1
            }
        }
    }

    fun printField() {
        var output = ""
        for (yy in 0 until y) {
            for (xx in 0 until x) {
                output += when (getElenent(yy, xx)) {
                    -1 -> "X"
                    else -> "."
                }
            }
            output += "\n"
        }
        print(output)
    }
}