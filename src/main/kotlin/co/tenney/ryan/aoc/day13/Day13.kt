package co.tenney.ryan.aoc.day13

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.IntPosition
import co.tenney.ryan.aoc.common.Direction

class Day13 : AocDay<Int>(13) {

    val debug = false
    var input: List<Packet>? = null

    override fun processInput(input: List<String>): Unit {
        this.input = input.filter(String::isNotBlank).map(::parse).toList()
    }

    override fun part1(): Int {
        return pairs(this.input!!).mapIndexed({ index, pair ->
            if (debug) println("== Pair ${index + 1} ==")
            var result: Int = 0
            if (compare(pair.first, pair.second) == Trinary.TRUE) {
                result = index + 1
            }
            if (debug) println()
            result
        }).sum()
    }

    override fun part2(): Int {
        val input: MutableList<Packet> = this.input!!.toMutableList()
        input += dividerPacket(2)
        input += dividerPacket(6)
        input.sortWith(::comparator)
        return (input.indexOf(dividerPacket(2)) + 1) * (input.indexOf(dividerPacket(6)) + 1)
    }

    fun pairs(input: List<Packet>): List<Pair<Packet, Packet>> {
        val buffer = mutableListOf<Pair<Packet, Packet>>()
        var lastItem: Packet? = null
        input.forEach { line ->
            if (lastItem == null) {
               lastItem = line
            } else {
                buffer += lastItem!! to line
                lastItem = null
            }
        }
        return buffer.toList()
    }
    
    fun dividerPacket(i: Int): Packet = listOf<List<Int>>(listOf<Int>(i))

    fun comparator(left: Any, right: Any): Int {
        return when (compare(left, right)) {
            Trinary.TRUE -> -1
            Trinary.FALSE -> 1
            Trinary.ABSENT -> 0
        }
    }

    fun compare(left: Any, right: Any, indent: Int = 0): Trinary {
        val nextIndent = indent + 2
        val indentStr = if (indent > 0) " ".repeat(indent) else ""
        if (debug) {
            val strLeft = left.toString().replace(" ", "")
            val strRight = right.toString().replace(" ", "")
            println("${indentStr}- Compare ${strLeft} vs ${strRight}")
        }
        if (left is Int && right is Int) {
            // If both values are integer, the lower integer should come first. If the left
            // integer is lower than the right integer, the inputs are in the right order.
            // If the left integer is higher than the right integer, the inputs are not in
            // the right order. Otherwise, the inputs are the same integer; continue checking
            // the next part of the input.
            if (left == right) {
                return Trinary.ABSENT
            } else if (left < right) {
                if (debug) println("${indentStr}  - Left side is smaller, so inputs are in the right order")
                return Trinary.TRUE
            } else {
                if (debug) println("${indentStr}  - Right side is smaller, so inputs are not in the right order")
                return Trinary.FALSE
            }

        } else if (left is List<*> && right is List<*>) {
            // If both values are lists, compare the first value of each list, then the second value, and so on.
            for (i in 0 until Math.max(left.size, right.size)) {
                if (i == left.size) {
                    // If the left list runs out of items first, the inputs are in the right order.
                    if (debug) println("${indentStr}  - Left side ran out of items, so inputs are in the right order")
                    return Trinary.TRUE
                }

                if (i == right.size) {
                    // If the right list runs out of items first, the inputs are not in the right order.
                    if (debug) println("${indentStr}  - Right side ran out of items, so inputs are not in the right order")
                    return Trinary.FALSE
                }

                val result = compare(left.get(i)!!, right.get(i)!!, nextIndent)
                if (result != Trinary.ABSENT) {
                    return result
                }
            }

            // If the lists are the same length and no comparison makes a decision
            // about the order, continue checking the next part of the input.
            return Trinary.ABSENT

        } else if ((left is Int != right is Int) && (left is List<*> != right is List<*>)) {
            // If exactly one value is an integer, convert the integer to a list which contains
            // that integer as its only value, then retry the comparison. For example, if
            // comparing [0,0,0] and 2, convert the right value to [2] (a list containing 2);
            // the result is then found by instead comparing [0,0,0] and [2].
            if (left is Int) {
                if (debug) println("${indentStr}  - Mixed types; convert left to [${left}] and retry comparison")
                return compare(listOf<Int>(left), right, nextIndent)
            } else if (right is Int) {
                if (debug) println("${indentStr}  - Mixed types; convert right to [${right}] and retry comparison")
                return compare(left, listOf<Int>(right), nextIndent)
            }
        }
        if (debug) println(left)
        if (debug) println(right)
        throw RuntimeException()
    }

    val tokenizer = Regex("(\\d+|\\[|\\]|,)")

    fun parse(input: String): Packet {
        val tokens = tokenizer.findAll(input).map(MatchResult::value).drop(1).toList().dropLast(1)
        var currentList: MutableList<Any> = mutableListOf<Any>()
        val output: MutableList<Any> = currentList
        val stack: ArrayDeque<MutableList<Any>> = ArrayDeque<MutableList<Any>>()
        for (token in tokens) {
            when (token) {
                "," -> {}
                "[" -> {
                    stack.add(currentList)
                    currentList = mutableListOf<Any>()
                }
                "]" -> {
                    val pop: MutableList<Any> = stack.removeLast()
                    pop.add(currentList.toList())
                    currentList = pop
                }
                else -> {
                    currentList.add(token.toInt())
                }
            }
        }
        if (output.toString().replace(" ", "") != input) {
            throw RuntimeException()
        }
        return output.toList()
    }

}

enum class Trinary {
    TRUE, FALSE, ABSENT
}

typealias Packet = List<Any>