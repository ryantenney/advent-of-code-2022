package co.tenney.ryan.aoc

interface AocProblem<I, O> {
    fun processInput(input: I): Unit
    fun part1(): O
    fun part2(): O
}