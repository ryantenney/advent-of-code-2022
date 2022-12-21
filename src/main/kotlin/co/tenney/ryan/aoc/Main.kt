package co.tenney.ryan.aoc

import java.time.LocalDate
import java.time.ZoneId

import co.tenney.ryan.util.outputResult
import co.tenney.ryan.util.time
import co.tenney.ryan.util.inputFileToLines
import co.tenney.ryan.aoc.day1.Day1
import co.tenney.ryan.aoc.day2.Day2
import co.tenney.ryan.aoc.day3.Day3
import co.tenney.ryan.aoc.day4.Day4
import co.tenney.ryan.aoc.day5.Day5
import co.tenney.ryan.aoc.day6.Day6
import co.tenney.ryan.aoc.day7.Day7
import co.tenney.ryan.aoc.day8.Day8
import co.tenney.ryan.aoc.day9.Day9
import co.tenney.ryan.aoc.day10.Day10
import co.tenney.ryan.aoc.day11.Day11
import co.tenney.ryan.aoc.day12.Day12
import co.tenney.ryan.aoc.day13.Day13
import co.tenney.ryan.aoc.day14.Day14
import co.tenney.ryan.aoc.day15.Day15
import co.tenney.ryan.aoc.day16.Day16
import co.tenney.ryan.aoc.day17.Day17
import co.tenney.ryan.aoc.day18.Day18
import co.tenney.ryan.aoc.day19.Day19
import co.tenney.ryan.aoc.day20.Day20
import co.tenney.ryan.aoc.day21.Day21

fun main(args: Array<String>) {
    val today = LocalDate.now(ZoneId.of("America/New_York"))
    val dayOfMonth: Int = if (today.year == 2022 && today.monthValue == 12) today.dayOfMonth else -1
    //val dayOfMonth: Int = 16
    listOf(Day1(), Day2(), Day3(), Day4(), Day5(), Day6(), Day7(), Day8(), Day9(), Day10(),
           Day11(), Day12(), Day13(), Day14(), Day15(), Day16(), Day17(), Day18(), Day19(),
           Day20(), Day21()).forEach {
        if (dayOfMonth == -1 || dayOfMonth == it.day) it.run {
            processInput(readInput(day))
            time { part1() }.also { outputResult(day, 1, it) }
            time { part2() }.also { outputResult(day, 2, it) }
        }
    }
}

fun readInput(day: Int): List<String> {
    return inputFileToLines("input-day$day.txt")
}