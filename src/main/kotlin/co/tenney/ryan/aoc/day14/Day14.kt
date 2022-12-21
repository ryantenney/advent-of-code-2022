package co.tenney.ryan.aoc.day14

import co.tenney.ryan.aoc.AocDay

class Day14 : AocDay<Int>(14) {

    var rockLines: List<RockLine>? = null

    override fun processInput(input: List<String>): Unit {
        this.rockLines = input.filter(String::isNotBlank).map { line ->
            RockLine(line.split("->").map(String::trim).map(Rock::fromString))
        }
    }

    override fun part1(): Int {
        val sandOrigin = Rock(500, 0)
        val rocks = this.rockLines!!.flatMap(RockLine::points).toMutableSet()
        val abyss = rocks.toList().maxOf(Rock::y) + 1
        var count = 0
        while (drop(sandOrigin, rocks, abyss)) {
            count++
        }
        return count
    }

    override fun part2(): Int {
        val sandOrigin = Rock(500, 0)
        val rocks = this.rockLines!!.flatMap(RockLine::points).toMutableSet()
        val abyss = rocks.toList().maxOf(Rock::y) + 2
        for (x in -500 .. 1000) {
            rocks.add(Rock(x, abyss))
        }
        var count = 0
        while (sandOrigin !in rocks && drop(sandOrigin, rocks, abyss)) {
            count++
        }
        return count
    }

    fun drop(start: Rock, rocks: MutableSet<Rock>, abyss: Int): Boolean {
        var position = start
        while (position.y < abyss) {
            val down = position + Direction.DOWN
            val downLeft = down + Direction.LEFT
            val downRight = down + Direction.RIGHT
            if (down !in rocks) {
                position = down
            } else if (downLeft !in rocks) {
                position = downLeft
            } else if (downRight !in rocks) {
                position = downRight
            } else {
                rocks.add(position)
                return true
            }
        }
        return false
    }

}

data class RockLine(val rocks: List<Rock>) {

    fun points(): List<Rock> {
        var lastPoint = rocks.first()
        val output = mutableListOf<Rock>()
        for (point in rocks.drop(1)) {
            output.add(lastPoint)
            val delta = point - lastPoint
            if (delta.x == 0 || delta.y == 0) {
                val abs = delta.abs()
                repeat(Math.max(abs.x, abs.y)) {
                    lastPoint += delta.sign()
                    output.add(lastPoint)
                }
                if (lastPoint != point) {
                    throw RuntimeException()
                }
            } else {
                throw RuntimeException()
            }
        }
        return output.toList()
    }

}

enum class Direction(val vector: Rock) {
    DOWN(Rock(0, 1)),
    RIGHT(Rock(1, 0)),
    LEFT(Rock(-1, 0))
}

operator fun Rock.plus(dir: Direction) = this + dir.vector

typealias Rock = co.tenney.ryan.aoc.common.IntPosition
