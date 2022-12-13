package co.tenney.ryan.aoc.common

enum class Direction(val vector: IntPosition) {
    U(IntPosition(0, 1)),
    R(IntPosition(1, 0)),
    D(IntPosition(0, -1)),
    L(IntPosition(-1, 0));
}
