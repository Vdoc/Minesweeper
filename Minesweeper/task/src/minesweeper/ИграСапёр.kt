package minesweeper

import java.util.*

class ИграСапёр {
    val получен = Scanner(System.`in`)
    val игровоеПоле: Таблица

    constructor (ширина: Int, высота: Int) {
        игровоеПоле = Таблица(ширина, высота)
    }

    fun запуститьИгру() {
        print("How many mines do you want on the field? ")
        игровоеПоле.закопатьМиныВСлучайныхМестах(получитьОтветОтИгрока().toInt())
        println()

        игровоеПоле.отрисоватьНаЭкране()
        while (!игровоеПоле.играОкончена) {
            do {
                print("Set/delete mines marks (x and y coordinates): ")
            } while (!игровоеПоле.исследоватьПолеИлиПоставитьМетку(получитьОтветОтИгрока()))
            println()

            игровоеПоле.проверитьУсловияПобеды()
            игровоеПоле.отрисоватьНаЭкране()
        }
        игровоеПоле.поздравитьСОкончаниемИгры()
    }

    private fun получитьОтветОтИгрока(ответ: String = получен.nextLine()): String = ответ
}
