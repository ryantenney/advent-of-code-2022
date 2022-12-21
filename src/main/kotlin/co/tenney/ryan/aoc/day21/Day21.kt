package co.tenney.ryan.aoc.day21

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.Operator

class Day21 : AocDay<Long>(21) {

    var root: Node? = null

    override fun processInput(input: List<String>): Unit {
        val map: Map<String, Node> = input.map({
            val (name, formula) = it.split(": ")
            name to if (formula.length == 11) {
                val (lhs, op, rhs) = formula.split(" ")
                Operation(name, Named(lhs), Operator.forOperator(op), Named(rhs))
            } else {
                Literal(name, formula.toLong())
            }
        }).toMap()

        this.root = buildTree(map, map.get("root")!!)
    }

    fun buildTree(input: Map<String, Node>, node: Node): Node = when (node) {
        is Named -> buildTree(input, input.get(node.name)!!)
        is Operation -> node.copy(lhs = buildTree(input, node.lhs), rhs = buildTree(input, node.rhs))
        else -> node
    }

    override fun part1(): Long {
        return process(this.root!!)
    }

    override fun part2(): Long {
        val root = this.root!! as Operation

        if (findHumn(root.lhs) > 0) {
            val lhs = simplify(root.lhs, { it.name != "humn" })
            val rhsValue = process(root.rhs)
            return solve(rhsValue, lhs)
        } else {
            val lhsValue = process(root.rhs)
            val rhs = simplify(root.rhs, { it.name != "humn" })
            return solve(lhsValue, rhs)
        }
    }

    fun solve(value: Long, eq: Node): Long {
        if (eq is Named) {
            return value
        }

        if (eq is Operation) {
            val op = eq.op
            val lhs = eq.lhs
            val rhs = eq.rhs
            if (lhs is Operation && rhs is Literal) {
                return solve(op.inverse().apply(value, rhs.value), lhs)
            } else if (lhs is Literal && rhs is Operation) {
                return when (op) {
                    Operator.ADD -> solve(value - lhs.value, rhs)
                    Operator.SUB -> solve(-(value - lhs.value), rhs)
                    Operator.MUL -> solve(value / lhs.value, rhs)
                    Operator.DIV -> throw RuntimeException()
                }
            } else if (lhs is Named && rhs is Literal) {
                return op.inverse().apply(value, rhs.value)
            } else if (lhs is Literal && rhs is Named) {
                return when (op) {
                    Operator.ADD -> value - lhs.value
                    Operator.SUB -> -(value - lhs.value)
                    Operator.MUL -> value / lhs.value
                    Operator.DIV -> throw RuntimeException()
                }
            }
        }

        throw RuntimeException()
    }

    fun process(node: Node): Long = when (node) {
        is Literal -> node.value
        is Operation -> node.op.apply(process(node.lhs), process(node.rhs))
        else -> process(node)
    }

    fun findHumn(node: Node, depth: Int = 1): Int {
        if (node.name == "humn") return depth
        return when (node) {
            is Literal -> -1
            is Operation -> Math.max(findHumn(node.lhs, depth + 1), findHumn(node.rhs, depth + 1))
            else -> throw RuntimeException()
        }
    }

    fun print(node: Node): String = when (node) {
        is Literal -> node.value.toString()
        is Operation -> "(${print(node.lhs)} ${node.op.symbol} ${print(node.rhs)})"
        is Named -> "$${node.name}"
    }

    fun simplify(node: Node, canSimplify: (Node) -> Boolean): Node = when (node) {
        is Literal -> node
        is Operation -> {
            val newLhs = if (canSimplify(node.lhs)) {
                simplify(node.lhs, canSimplify)
            } else {
                Named(node.lhs.name)
            }

            val newRhs = if (canSimplify(node.rhs)) {
                simplify(node.rhs, canSimplify)
            } else {
                Named(node.rhs.name)
            }

            if (newLhs is Literal && newRhs is Literal) {
                Literal(node.name, node.op.apply(newLhs.value, newRhs.value))
            } else {
                node.copy(lhs = newLhs, rhs = newRhs)
            }
        }
        else -> throw RuntimeException()
    }

}

sealed interface Node {
    val name: String
}

data class Literal(override val name: String, val value: Long): Node

data class Named(override val name: String): Node

data class Operation(override val name: String, val lhs: Node, val op: Operator, val rhs: Node): Node

fun Operator.apply(lhs: Long, rhs: Long) = when (this) {
    Operator.ADD -> lhs + rhs
    Operator.SUB -> lhs - rhs
    Operator.MUL -> lhs * rhs
    Operator.DIV -> lhs / rhs
}

fun Operator.inverse() = when (this) {
    Operator.ADD -> Operator.SUB
    Operator.SUB -> Operator.ADD
    Operator.MUL -> Operator.DIV
    Operator.DIV -> Operator.MUL
}
