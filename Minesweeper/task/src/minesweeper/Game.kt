package minesweeper

import java.util.*

class Game {
    val scanner = Scanner(System.`in`)
    val field: Matrix

    constructor (width: Int, heigth: Int) {
        field = Matrix(width, heigth)
    }

    fun start() {
        print("How many mines do you want on the field? ")
        field.generateMines(takeAnAnswer().toInt())
        field.printField()
    }

    private fun takeAnAnswer(answer: String = scanner.next()): String = answer
}