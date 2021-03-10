package minesweeper

import kotlin.random.Random

class Matrix {
    val x: Int
    val y: Int
    val m: Array<IntArray>
    var minesCount: Int = 0
    var isGameOver: Boolean = false
    var win: Boolean = true

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
        for (_Y in mine[0] - 1..mine[0] + 1) {
            if (_Y >= 0 && _Y < y) {
                for (_X in mine[1] - 1..mine[1] + 1) {
                    if (_X >= 0 && _X < x) {
                        addMineHint(_Y, _X)
                    }
                }
            }
        }
    }

    private fun addMineHint(_Y: Int, _X: Int) {
        if (getElenent(_Y, _X) == -1) return
        setElement(_Y, _X, getElenent(_Y, _X) + 1)
    }

    fun printField() {
        var output =    " │123456789│\n" +
                        "—│—————————│\n"
        for (_Y in 0 until y) {
            output += "${_Y + 1}│"
            for (_X in 0 until x) {
                output += when (getElenent(_Y, _X)) {
//                    -1 -> "X"       // мина
                    -1 -> "."       // пустое поле
                    -2, 10 -> "*"   // маркер
                    in 1..8 -> getElenent(_Y, _X).toString()
                    else -> "."     // пустое поле
                }
            }
            output += "│\n"
        }
        output += "—│—————————│\n"
        print(output)
    }

    fun check(getAnswer: String): Boolean {
        val _X: Int = getAnswer.split(" ")[0].toInt() - 1
        val _Y: Int = getAnswer.split(" ")[1].toInt() - 1
        val element = getElenent(_Y, _X)

        when (element) {
            -1 -> setElement(_Y, _X, -2)    // пометить мину
             0 -> setElement(_Y, _X, 10)    // поставить метку на пустое мето
            10 -> setElement(_Y, _X, 0)     // снять метку с пустого места
            else -> {
                println("There is a number here!") // попытка пометить цифру
                return false                       // приводит к повтору запроса
            }
        }
        return true
    }

    fun checkWin() {
        var counter = 0
        for (_Y in 0 until y) {
            for (_X in 0 until x) {
                if (getElenent(_Y, _X) == -2) // метка на мине
                    counter++                 // подсчет меток
                if (getElenent(_Y, _X) == 10) { // остались метки на пустых клетках
                    win = false                 // пока НЕ победа
                    return
                }
//                if (getElenent(_Y, _X) == 100) {
//                    isGameOver = true     // игра окончена
//                }                         // проиграл
            }
        }
        if (counter == minesCount) {
            win = true                  // победа
            isGameOver = true           // игра окончена
        }
    }

    fun congrats() {
        if (win) println("Congratulations! You found all the mines!")
    }
}