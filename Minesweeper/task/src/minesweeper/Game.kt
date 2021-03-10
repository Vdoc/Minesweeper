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
        field.generateMines(receivedAnswer().toInt())
        println()

        field.printField()
        while (!field.isGameOver) {
            do {
                print("Set/delete mines marks (x and y coordinates): ")
            } while (!field.check(receivedAnswer()))
            println()

            field.checkWin()
            field.printField()
        }
        field.congrats()
    }

    private fun receivedAnswer(answer: String = scanner.nextLine()): String = answer
}
