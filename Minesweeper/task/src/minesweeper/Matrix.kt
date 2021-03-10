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

    fun setElement(row: Int, column: Int, nomber: Int) {
        m[row][column] = nomber
    }

    fun generateMines(minesCount: Int) {
        while (this.minesCount != minesCount) {
            var mine = arrayOf(Random.nextInt(0, 9), Random.nextInt(0, 9))
            if (getElenent(mine[0], mine[1]) != -1) {
                this.minesCount++
                setElement(mine[0], mine[1], -1)
                addHints(mine)
            }
        }
    }

    private fun addHints(mine: Array<Int>) {
        for (yy in mine[0] - 1..mine[0] + 1) {
            if (yy >= 0 && yy < y) {
                for (xx in mine[1] - 1..mine[1] + 1) {
                    if (xx >= 0 && xx < x) {
                        addMineHint(yy,xx)
                    }
                }
            }
        }
    }

    private fun addMineHint(yy: Int, xx: Int) {
        if (getElenent(yy, xx) == -1) return
        setElement(yy, xx, getElenent(yy, xx) + 1)
    }

    fun printField() {
        var output = ""
        for (yy in 0 until y) {
            for (xx in 0 until x) {
                output += when (getElenent(yy, xx)) {
                    -1 -> "X"
                    in 1..8 -> getElenent(yy, xx).toString()
                    else -> "."
                }
            }
            output += "\n"
        }
        print(output)
    }
}