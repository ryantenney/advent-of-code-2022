package co.tenney.ryan.aoc.day3

import co.tenney.ryan.aoc.AocDay

class Day3 : AocDay<Int>(3){

    var items: List<List<Item>>? = null

    override fun processInput(input: List<String>): Unit {
        items = input.map(::parseItems)
    }

    override fun part1(): Int {
        return items!!.map({
            val split = splitItems(it)
            val intersection = split[0] intersect split[1]
            if (intersection.size != 1) {
                throw RuntimeException("Invalid")
            }
            intersection.iterator().next().priority
        }).sum()
    }

    override fun part2(): Int {
        return items!!.chunked(3).map({
            val intersection: HashSet<Item> = HashSet(it[0])
            intersection.retainAll(it[1])
            intersection.retainAll(it[2])
            if (intersection.size != 1) {
                throw RuntimeException("Invalid")
            }
            intersection.iterator().next().priority
        }).sum()
    }

    fun parseItems(sack: String): List<Item> = sack.toCharArray().map(::Item)

    fun splitItems(items: List<Item>): List<Set<Item>> = items.chunked(items.size / 2).map({ it.toSet() })

}

data class Item(val letter: Char) : Comparable<Item> {

    init {
        require((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z')) { "" }
    }

    val priority: Int = if (letter >= 'a') letter - 'a' + 1 else letter - 'A' + 27

    override fun compareTo(other: Item): Int = Integer.compare(priority, other.priority)

}