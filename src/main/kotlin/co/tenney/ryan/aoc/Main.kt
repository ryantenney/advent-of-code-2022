package co.tenney.ryan.aoc

import co.tenney.ryan.util.outputResult
import co.tenney.ryan.util.time

fun main(args: Array<String>) {
    listOf(Day1(), Day2()).forEach {
        it.run {
            val input = processInput()
            time { part1(input) }.also { outputResult(day, 1, it) }
            time { part2(input) }.also { outputResult(day, 2, it) }
        }
    }
}