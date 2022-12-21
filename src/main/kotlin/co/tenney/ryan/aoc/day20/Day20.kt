package co.tenney.ryan.aoc.day20

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.Edge
import co.tenney.ryan.aoc.common.IntPosition
import co.tenney.ryan.aoc.common.Itertools
import co.tenney.ryan.aoc.common.Search
import co.tenney.ryan.aoc.common.SearchContext
import co.tenney.ryan.aoc.common.Vertex

class Day20 : AocDay<Long>(20) {

    var values: List<Input>? = null

    override fun processInput(input: List<String>): Unit {
        this.values = input.mapIndexed({ index, value -> Input(value.toInt(), index) })
    }

    override fun part1(): Long {
        return decrypt(listOf<Int>(1000, 2000, 3000))
    }

    override fun part2(): Long {
        return decrypt(listOf<Int>(1000, 2000, 3000), 811589153, 10)
    }

    fun decrypt(outputIndices: List<Int>, decryptionKey: Long = 1L, searchValue: Int = 0, repetitions: Int = 1): Long {
        val values = this.values!!
        val mutableValues = values.toMutableList()
        val len = values.size - 1
        var zero: Input? = null
        repeat (repetitions) {
            for (value in values) {
                if (value.value == searchValue) {
                    zero = value
                } else {
                    val pos = mutableValues.indexOf(value)
                    val decryptedValue = decryptionKey * value.value
                    val newPos = mod(pos + decryptedValue, len.toLong()).toInt()
                    if (newPos != pos) {
                        mutableValues.removeAt(pos)
                        mutableValues.add(newPos, value)
                    }
                }
            }
        }

        val zeroIdx = mutableValues.indexOf(zero)
        return outputIndices
            .map({ index -> mod(zeroIdx + index, len) })
            .mapNotNull(mutableValues::get)
            .sumOf(Input::value)
            .toLong() * decryptionKey
    }

    fun mod(value: Int, modulo: Int): Int {
        return ((value % modulo) + modulo) % modulo
    }

    fun mod(value: Long, modulo: Long): Long {
        return ((value % modulo) + modulo) % modulo
    }

}

data class Input(val value: Int, val pos: Int)
