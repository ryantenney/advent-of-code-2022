package co.tenney.ryan.aoc.common

enum class Operator(val symbol: String) {

    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    companion object {

        val indexByOperator = Operator.values().map({ it.symbol to it }).toMap()

        fun forOperator(input: String): Operator {
            val result = indexByOperator.get(input)
            if (result == null) {
                throw RuntimeException("Operator '${input}'")
            }
            return result
        }

    }
}
