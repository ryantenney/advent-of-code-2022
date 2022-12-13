package co.tenney.ryan.aoc.common

data class IntPosition(val x: Int, val y: Int) {

    operator fun plus(b: IntPosition) = IntPosition(x + b.x, y + b.y)

    operator fun minus(b: IntPosition) = IntPosition(x - b.x, y - b.y)

    fun abs(): IntPosition = IntPosition(Math.abs(x), Math.abs(y))

    fun transpose(direction: Direction) = this + direction.vector

}
