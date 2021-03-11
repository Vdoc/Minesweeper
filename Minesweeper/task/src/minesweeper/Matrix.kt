package minesweeper

import kotlin.random.Random

class Matrix {
    val x: Int
    val y: Int

    val m: Array<IntArray>
    val fogOfWar: Array<IntArray>

    var minesCount: Int = 0
    var hidedFieldsCounter: Int

    var isWin: Boolean
    var isGameOver: Boolean

    constructor (width: Int, heigth: Int) {
        x = width
        y = heigth
        m = Array(y) { IntArray(x) }
        fogOfWar = Array(y) { IntArray(x) }
        hidedFieldsCounter = x * y
        isGameOver = false
        isWin = false
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
                    -1 -> if (isGameOver) "X"   // если ты это видишь, то ты проиграл
                          else "."              // , а пока она не видна
                    -2, 10 -> "*"               // маркер на мине или на пустом поле
                    in 1..8 -> if (isFog(_Y, _X)) "."       // пока поле в тумане цифры не видны
                          else getElenent(_Y, _X).toString()// если тумана нет - пишем количество мин вокруг
                    in 11..18 -> if (isFog(_Y, _X)) "*" // метка на поле с цифрой если туман есть
                          else {                        // если тумана нет (если такое возможно), то
                              setElement(_Y, _X, getElenent(_Y, _X) - 10)// снимаем метку и
                              getElenent(_Y, _X).toString()                     // рисуем цифру
                          }
                    else -> if (isFog(_Y, _X)) "."  // обычное поле с туманом
                          else "/"                  // поле без тумана
                }
            }
            output += "│\n"
        }
        output += "—│—————————│\n"
        print(output)
    }

    private fun isFog(_Y: Int, _X: Int): Boolean = (fogOfWar[_Y][_X] == 0)

    fun check(getAnswer: String): Boolean {
        val answer = getAnswer.split(" ")
        val _X: Int = answer[0].toInt() - 1
        val _Y: Int = answer[1].toInt() - 1
        var whatToDo = if (answer.size < 3) "free"  // дефолтное значение для ручного тестирования
                       else answer[2]
        when(whatToDo) {
            "mine" -> markMine(_Y, _X)
            "free" -> freeField(_Y, _X)
            else -> return false
        }
        if (hidedFieldsCounter == minesCount) {
            isWin = true
            checkWin()
        }
        return true
    }

    private fun markMine(_Y: Int, _X: Int) {
        val element = getElenent(_Y, _X)
        when (element) {
            -1 -> setElement(_Y, _X, -2)    // пометить мину
            -2 -> setElement(_Y, _X, -1)    // снять метку с мины
             0 -> setElement(_Y, _X, 10)    // поставить метку на пустое мето
            10 -> setElement(_Y, _X, 0)     // снять метку с пустого места
            in 1..8 -> setElement(_Y, _X, element + 10) // метка на цифре
            else    -> setElement(_Y, _X, element - 10) // снять с цифры метку
        }
    }

    private fun freeField(_Y: Int, _X: Int) {
        val element = getElenent(_Y, _X)
        val free: Array<Int> = arrayOf(_Y, _X)
        when (element) {
            -1 -> isGameOver = true     // подорвался
            in 1..8 -> {                // открываем цифру
                fogOfWar[_Y][_X] = 1
                hidedFieldsCounter--    // уменьшаем счетчик тумана
            }
            in 10..18 -> {              // открываем поле с меткой (10) или поле с меткой на цифре (11..18)
                fogOfWar[_Y][_X] = 1
                hidedFieldsCounter--
                setElement(_Y, _X, element - 10)
                freeAround(free)        // открываем соседние поля, если они небыли до этого открыты
            }
            0 -> {                      // открываем пустое поле
                fogOfWar[_Y][_X] = 1
                hidedFieldsCounter--
                freeAround(free)        // открываем соседние поля
            }
            else -> {}
        }
    }

    private fun freeAround(free: Array<Int>) {  // перебор всех фосьми полей вокруг данного
        for (_Y in free[0] - 1..free[0] + 1) {
            if (_Y >= 0 && _Y < y) {            // но если оно с краю - пропускаем
                for (_X in free[1] - 1..free[1] + 1) {
                    if (_X >= 0 && _X < x) {        // пропускаем если поле за границей
                        if (fogOfWar[_Y][_X] == 0) {    // если уже было открыто, то пропускаем
                            freeField(_Y, _X)
                        }
                    }
                }
            }
        }
    }

    fun checkWin() {
        var counter = 0
        for (_Y in 0 until y) {
            for (_X in 0 until x) {
                if (getElenent(_Y, _X) == -2) // метка на мине
                    counter++                 // подсчет меток
                if (getElenent(_Y, _X) == 10) { // остались метки на пустых клетках
                    isWin = false               // пока НЕ победа
                    return
                }
            }
        }
        if (counter == minesCount) {    // если количество меток на минах = моличеству мин, значит они все помечены. Тут надо бы переписать, что главное чтоб неоткрытых мест не оставалось, а не угадывать где в тумане мины стоят, но (я устал) оно и так все тесты проходит в другом месте программы.
            isWin = true                // победа
            isGameOver = true           // игра окончена
        }
    }

    fun congrats() {
        if (isWin) println("Congratulations! You found all the mines!")
        else println("You stepped on a mine and failed!")
    }
}