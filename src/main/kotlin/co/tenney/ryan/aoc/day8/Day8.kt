package co.tenney.ryan.aoc.day8

import co.tenney.ryan.aoc.AocDay

class Day8 : AocDay<Int>(8) {

    var grid: List<List<Int>>? = null

    override fun processInput(input: List<String>): Unit {
        var gridTmp = input.filter(String::isNotEmpty).map({ line: String ->
            line.toCharArray().map({ it - '0' }).toList()
        })
        
        grid = gridTmp
    }

    override fun part1(): Int {
        val yMax = grid!!.size
        val xMax = grid!!.get(0).size
        var count = (yMax + xMax) * 2 - 4
        for (y in 1 .. yMax - 2) {
            for (x in 1 .. xMax - 2) {
                val height = grid!!.get(y).get(x)
                if (height > getMaxHeights(y, y, 0, x - 1)) {
                    // left
                    count++
                } else if (height > getMaxHeights(y, y, x + 1, xMax - 1)) {
                    // right
                    count++
                } else if (height > getMaxHeights(0, y - 1, x, x)) {
                    // top
                    count++
                } else if (height > getMaxHeights(y + 1, yMax - 1, x, x)) {
                    // bottom
                    count++
                }
            }
        }
        return count
    }

    override fun part2(): Int {
        val yMax = grid!!.size
        val xMax = grid!!.get(0).size
        var maxDist = 0
        for (y in 0 .. yMax - 1) {
            for (x in 0 .. xMax - 1) {
                val height = grid!!.get(y).get(x)
                val dist1 = left(y, x, height)
                val dist2 = up(y, x, height)
                val dist3 = down(y, x, height)
                val dist4 = right(y, x, height)
                val score = dist1 * dist2 * dist3 * dist4
                if (score > maxDist) {
                    maxDist = score
                }
            }
        }
        return maxDist
    }

    fun getMaxHeights(yMin: Int, yMax: Int, xMin: Int, xMax: Int): Int {
        var max = 0
        for (y in yMin .. yMax) {
            for (x in xMin .. xMax) {
                max = Math.max(max, grid!!.get(y).get(x))
            }
        }
        return max
    }

    fun left(y: Int, x: Int, height: Int): Int {
        var count = 0
        val row = grid!!.get(y)
        for (i in x - 1 downTo 0) {
            count++
            if (row.get(i) >= height) {
                return count
            }
        }
        return count
    }

    fun up(y: Int, x: Int, height: Int): Int {
        var count = 0
        for (i in y - 1 downTo 0) {
            count++
            if (grid!!.get(i).get(x) >= height) {
                return count
            }
        }
        return count
    }

    fun down(y: Int, x: Int, height: Int): Int {
        var count = 0
        for (i in y + 1 .. grid!!.size - 1) {
            count++
            if (grid!!.get(i).get(x) >= height) {
                return count
            }
        }
        return count
    }

    fun right(y: Int, x: Int, height: Int): Int {
        var count = 0
        val row = grid!!.get(y)
        for (i in x + 1 .. row.size - 1) {
            count++
            if (row.get(i) >= height) {
                return count
            }
        }
        return count
    }

}
