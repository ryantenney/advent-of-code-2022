package co.tenney.ryan.aoc.day5

import co.tenney.ryan.aoc.AocDay

class Day5 : AocDay<String>(5) {

    var stacks: List<ArrayDeque<String>>? = null
    var moves: List<Move>? = null

    override fun processInput(input: List<String>): Unit {
        val stackLines: List<String> = input.takeWhile({ !it.isBlank() }).reversed()
        moves = input.drop(stackLines.size + 1).map(::parseMove)

        val stackLayout = parseStackLayout(stackLines[0])
        val stacksTemp: List<ArrayDeque<String>> = stackLayout.map({ ArrayDeque<String>() })

        stackLines.drop(1).forEach({ stack ->
            stackLayout.forEachIndexed({ i, v ->
                val contents = stack.substring(v.second).trim()
                if (!contents.isBlank()) {
                    stacksTemp[i].add(contents)
                }
            })
        })

        stacks = stacksTemp.toList()
    }

    fun copyStacks(): MutableList<ArrayDeque<String>> {
        return stacks!!.map({ ArrayDeque<String>(it) }).toMutableList()
    }

    override fun part1(): String {
        var stacksLocal = copyStacks()
        moves!!.forEach({ move ->
            val to = stacksLocal[move.to]
            val from = stacksLocal[move.from]
            for (i in 1 .. move.qty) {
                to.add(from.removeLast())
            }
        })
        return stacksLocal.map({ it.last() }).joinToString("")
    }

    override fun part2(): String {
        var stacksLocal = copyStacks()
        moves!!.forEach({ move ->
            val to = stacksLocal[move.to]
            val from = stacksLocal[move.from]
            val qty = move.qty
            to.addAll(from.takeLast(qty))
            stacksLocal[move.from] = ArrayDeque<String>(from.dropLast(qty))
        })
        return stacksLocal.map({ it.last() }).joinToString("")
    }

}

data class Move(val qty: Int, val from: Int, val to: Int)

val stackLayoutPattern: Regex = Regex("\\d+")

fun parseStackLayout(input: String): List<Pair<Int, IntRange>> {
    val matches = stackLayoutPattern.findAll(input)
    return matches.map({ it.value.toInt() to it.range }).toList()
}

val movePattern: Regex = Regex("move (\\d+) from (\\d+) to (\\d+)")

fun parseMove(input: String): Move {
    val match = movePattern.matchEntire(input)
    if (match == null) {
        throw RuntimeException("no match")
    }
    val groups = match.groups
    return Move(groups[1]!!.value.toInt(), groups[2]!!.value.toInt() - 1, groups[3]!!.value.toInt() - 1)
}
