package co.tenney.ryan.aoc.day9

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.Direction
import co.tenney.ryan.aoc.common.IntPosition

import kotlin.math.abs
import kotlin.math.sign

class Day9 : AocDay<Int>(9) {

    val DEBUG = false

    val SIZE = IntPosition(11, 11)
    val ORIGIN = IntPosition(5, 5)

    //val SIZE = IntPosition(6, 5)
    //val ORIGIN = IntPosition(0, 0)

    //val SIZE = IntPosition(21, 26)
    //val ORIGIN = IntPosition(11, 5)

    var moves: List<Pair<Direction, Int>>? = null

    override fun processInput(input: List<String>): Unit {
        moves = input.map({
            val (move, scale) = it.split(" ")
            Direction.valueOf(move) to scale.toInt()
        })
    }

    override fun part1(): Int {
        var head = IntPosition(0, 0)
        var tail = IntPosition(0, 0)
        val tails = mutableSetOf<IntPosition>(tail)
        moves!!.forEach({ (move, scale) ->
            for (i in 0 until scale) {
                val newHead = head.transpose(move)
                val newTail = moveTail(newHead, head, tail)
                //val newTail = tail.follow(newHead)
                head = newHead
                tail = newTail
                tails.add(tail)
            }
        })
        return tails.size
    }

    override fun part2(): Int {
        val rope = Array(10) { IntPosition(0, 0) }
        val tails = mutableSetOf<IntPosition>(IntPosition(0, 0))

        moves!!.forEach({ (move, scale) ->
            for (i in 0 until scale) {
                var oldHead = rope[0]
                var newHead = oldHead.transpose(move)
                rope[0] = newHead
                for (x in 1 .. rope.size - 1) {
                    val oldTail = rope[x]
                    //val newTail = moveTail(newHead, oldHead, oldTail)
                    val newTail = oldTail.follow(newHead)
                    rope[x] = newTail
                    oldHead = oldTail
                    newHead = newTail
                }
                tails.add(rope[9])
            }
        })
        return tails.size
    }

    fun moveTail(newHead: IntPosition, oldHead: IntPosition, tail: IntPosition): IntPosition {
        val newDiff = (newHead - tail).abs()
        return if (newDiff == IntPosition(2, 0) || newDiff == IntPosition(0, 2)) {
            val headDiff = newHead - oldHead
            tail + headDiff
        } else if (newDiff == IntPosition(2, 1) || newDiff == IntPosition(1, 2)) {
            oldHead
        } else {
            tail
        }
    }

    fun print(move: Direction, scale:Int, rope: Array<IntPosition>) {
        println("== ${move} ${scale} ==")

        val board = Array<Array<String>>(SIZE.y) { Array<String>(SIZE.x) { "." } }
        for (i in 0 until rope.size) {
            val knot = rope[i] + ORIGIN
            val value = board[knot.y][knot.x]
            if (value == ".") {
                board[knot.y][knot.x] = if (i == 0) "H" else i.toString()
            }
        }

        if (board[ORIGIN.y][ORIGIN.x] == ".") {
            board[ORIGIN.y][ORIGIN.x] = "s"
        }

        for (y in SIZE.y - 1 downTo 0) {
            println(board[y].joinToString(""))
        }

        println()
    }

}

fun IntPosition.follow(other: IntPosition): IntPosition {
    return this + (other - this).sign()
}

fun IntPosition.follow2(other: IntPosition): IntPosition {
    val d_X = other.x - x
    val d_Y = other.y - y
    if (abs(d_X) > 0 || abs(d_Y) > 0) {
        return IntPosition(x + d_X.sign, y + d_Y.sign)
    }
    return this
}
