package co.tenney.ryan.aoc.day11

import co.tenney.ryan.aoc.AocDay

class Day11 : AocDay<Long>(11) {

    var monkeys: List<Monkey>? = null

    override fun processInput(input: List<String>): Unit {
        val testInput = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".split("\n")

        val stack = ArrayDeque<String>(input)
        val monkeys = mutableListOf<Monkey>()
        while (!stack.isEmpty()) {
            if (stack.first().isEmpty()) {
                stack.removeFirst()
            } else {
                monkeys += parseMonkey(stack)
            }
        }
        this.monkeys = monkeys.toList()
    }

    fun copyMonkeys(): MutableList<Monkey> {
        return monkeys!!.map({
            it.copy(items = it.items.toMutableList())
        }).toMutableList()
    }

    override fun part1(): Long {
        val monkeys = copyMonkeys()
        repeat (20) {
            for (monkey in monkeys) {
                monkey.inspectAll(monkeys, { it / 3 })
            }
        }
        val topTwo = monkeys.map(Monkey::inspections).sortedDescending().take(2)
        return topTwo[0] * topTwo[1]
    }

    override fun part2(): Long {
        val monkeys = copyMonkeys()
        val commonDivisor = monkeys.map(Monkey::testDivisible).fold(1L) { acc, monkey -> acc * monkey }
        repeat (10000) {
            for (monkey in monkeys) {
                //monkey.inspectAll(monkeys, { println("${it} % ${commonDivisor} = ${it.mod(commonDivisor)}"); it.mod(commonDivisor) })
                monkey.inspectAll(monkeys, { it.mod(commonDivisor) })
            }
        }
        val topTwo = monkeys.map(Monkey::inspections).sortedDescending().take(2)
        return topTwo[0] * topTwo[1]
    }

}

val numberRegex = Regex("\\d+")

fun extractNumbers(input: String): Sequence<Int> {
    return numberRegex.findAll(input).map(MatchResult::value).map(String::toInt)
}

fun parseMonkey(stack: ArrayDeque<String>): Monkey {
    return Monkey(
            extractNumbers(expectPrefix(stack.removeFirst(), "Monkey ")).first(),
            extractNumbers(expectPrefix(stack.removeFirst(), "  Starting items:")).toMutableList(),
            parseOperation(expectPrefix(stack.removeFirst(), "  Operation:")),
            extractNumbers(expectPrefix(stack.removeFirst(), "  Test:")).first(),
            extractNumbers(expectPrefix(stack.removeFirst(), "    If true: throw to monkey")).first(),
            extractNumbers(expectPrefix(stack.removeFirst(), "    If false: throw to monkey")).first())
}

fun parseOperation(input: String): Operation {
    val (op, rhs) = input.replace("  Operation: new = old ", "").trim().split(" ")
    return Operation(Operator.forOperator(op), if (rhs == "old") Long.MAX_VALUE else rhs.toLong())
}

fun expectPrefix(input: String, prefix: String): String {
    if (input.startsWith(prefix)) {
        return input
    }
    throw RuntimeException("'${input}' did not begin with '${prefix}'")
}

val oldIntPlaceholder = Long.MAX_VALUE

data class Monkey(val id: Int, val items: MutableList<Int>, val op: Operation, val testDivisible: Int, val targetIfTrue: Int, val targetIfFalse: Int) {

    var inspections: Long = 0
        get

    fun inspectAll(monkeys: List<Monkey>, worryFn: (Long) -> Long) {
        while (!items.isEmpty()) {
            inspections++
            var longItem: Long = items.removeFirst().toLong()
            longItem = op.apply(longItem)
            val item = worryFn(longItem).toInt()
            val monkeyIndex = if (item % testDivisible == 0) targetIfTrue else targetIfFalse
            monkeys[monkeyIndex].receive(item)
        }
    }

    fun receive(item: Int): Unit {
        items.add(item)
    }

}

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

data class Operation(val op: Operator, val new: Long) {

    fun apply(old: Long): Long {
        if (new == oldIntPlaceholder) {
            return compute(old, op, old)
        }
        return compute(old, op, new)
    }

    fun compute(lhs: Long, op: Operator, rhs: Long): Long {
        return when (op) {
            Operator.ADD -> lhs + rhs
            Operator.SUB -> lhs - rhs
            Operator.MUL -> lhs * rhs
            Operator.DIV -> lhs / rhs
        }
    }

}
