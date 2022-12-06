package co.tenney.ryan.aoc.day6

import co.tenney.ryan.aoc.AocDay

class Day6 : AocDay<Int>(6) {

    var input: String? = null

    override fun processInput(input: List<String>): Unit {
        this.input = input[0]
    }

    override fun part1(): Int {
        return findMarker(input!!, 4)
    }

    override fun part2(): Int {
        return findMarker(input!!, 14)
    }

}

fun findMarker(input: String, len: Int): Int {
    for (i in 0..(input.length - len)) {
        val range = i..i+(len-1)
        if (input.substring(range).toCharArray().toSet().size == len) {
            return range.endInclusive + 1
        }
    }
    return -1
}
