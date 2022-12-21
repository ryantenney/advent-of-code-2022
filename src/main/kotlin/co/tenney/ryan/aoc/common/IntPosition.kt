package co.tenney.ryan.aoc.common

import kotlin.math.sign

data class IntPosition(val x: Int, val y: Int) {

    operator fun plus(b: IntPosition) = IntPosition(x + b.x, y + b.y)

    operator fun plus(b: Vector) = this + b.vector()

    operator fun minus(b: IntPosition) = IntPosition(x - b.x, y - b.y)

    operator fun minus(b: Vector) = this - b.vector()

    fun abs(): IntPosition = IntPosition(Math.abs(x), Math.abs(y))

    @Deprecated("Use operator")
    fun transpose(vector: Vector) = this + vector.vector()

    fun sign() = IntPosition(x.sign, y.sign)

    companion object {

        fun fromString(input: String): IntPosition {
            val (x, y) = input.split(",").map(String::toInt)
            return IntPosition(x, y)
        }

    }

}
