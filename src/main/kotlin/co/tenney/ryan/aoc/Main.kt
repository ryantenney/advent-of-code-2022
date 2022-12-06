package co.tenney.ryan.aoc

import co.tenney.ryan.util.outputResult
import co.tenney.ryan.util.time
import co.tenney.ryan.util.inputFileToLines
import co.tenney.ryan.aoc.day1.Day1
import co.tenney.ryan.aoc.day2.Day2
import co.tenney.ryan.aoc.day3.Day3
import co.tenney.ryan.aoc.day4.Day4
import co.tenney.ryan.aoc.day5.Day5
import co.tenney.ryan.aoc.day6.Day6

fun main(args: Array<String>) {
    listOf(Day1(), Day2(), Day3(), Day4(), Day5(), Day6()).forEach {
        it.run {
            processInput(readInput(day))
            time { part1() }.also { outputResult(day, 1, it) }
            time { part2() }.also { outputResult(day, 2, it) }
        }
    }
}

fun readInput(day: Int): List<String> {
    return inputFileToLines("input-day$day.txt")
}