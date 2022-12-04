package co.tenney.ryan.aoc.day4

import co.tenney.ryan.aoc.AocDay

class Day4 : AocDay<Int>(4) {

    var ranges: List<List<ClosedRange<Int>>>? = null

    override fun processInput(input: List<String>): Unit {
        ranges = input.map({ assignments ->
            assignments.split(',').map({ assignment ->
                val range = assignment.split('-').map(Integer::parseInt)
                range[0] .. range[1]
            })
        })
    }

    override fun part1(): Int {
        return ranges!!.filter({ containsFully(it[0], it[1]) }).count()
    }

    override fun part2(): Int {
        return ranges!!.filter({ overlaps(it[0], it[1]) }).count()
    }

    fun containsFully(a: ClosedRange<Int>, b: ClosedRange<Int>): Boolean {
        return (b.start in a && b.endInclusive in a) ||
                (a.start in b && a.endInclusive in b)
    }

    fun overlaps(a: ClosedRange<Int>, b: ClosedRange<Int>): Boolean {
        return b.start in a || b.endInclusive in a ||
                a.start in b || a.endInclusive in b
    }

}