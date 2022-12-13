package co.tenney.ryan.aoc.day10

import co.tenney.ryan.aoc.AocDay

class Day10 : AocDay<Int>(10) {

    var rawInput: List<String>? = null
    var input: List<Pair<String, Int>>? = null

    override fun processInput(input: List<String>): Unit {
        val input1 = """
noop
addx 3
addx -5""".split("\n")

        val input2 = """
        addx 15
        addx -11
        addx 6
        addx -3
        addx 5
        addx -1
        addx -8
        addx 13
        addx 4
        noop
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx -35
        addx 1
        addx 24
        addx -19
        addx 1
        addx 16
        addx -11
        noop
        noop
        addx 21
        addx -15
        noop
        noop
        addx -3
        addx 9
        addx 1
        addx -3
        addx 8
        addx 1
        addx 5
        noop
        noop
        noop
        noop
        noop
        addx -36
        noop
        addx 1
        addx 7
        noop
        noop
        noop
        addx 2
        addx 6
        noop
        noop
        noop
        noop
        noop
        addx 1
        noop
        noop
        addx 7
        addx 1
        noop
        addx -13
        addx 13
        addx 7
        noop
        addx 1
        addx -33
        noop
        noop
        noop
        addx 2
        noop
        noop
        noop
        addx 8
        noop
        addx -1
        addx 2
        addx 1
        noop
        addx 17
        addx -9
        addx 1
        addx 1
        addx -3
        addx 11
        noop
        noop
        addx 1
        noop
        addx 1
        noop
        noop
        addx -13
        addx -19
        addx 1
        addx 3
        addx 26
        addx -30
        addx 12
        addx -1
        addx 3
        addx 1
        noop
        noop
        noop
        addx -9
        addx 18
        addx 1
        addx 2
        noop
        noop
        addx 9
        noop
        noop
        noop
        addx -1
        addx 2
        addx -37
        addx 1
        addx 3
        noop
        addx 15
        addx -21
        addx 22
        addx -6
        addx 1
        noop
        addx 2
        addx 1
        noop
        addx -10
        noop
        noop
        addx 20
        addx 1
        addx 2
        addx 2
        addx -6
        addx -11
        noop
        noop
        noop""".split("\n").map(String::trim)

        this.rawInput = input
        this.input = input.map {
            val split = it.split(" ")
            if (split.size == 1) {
                split[0] to 0
            } else {
                split[0] to split[1].toInt()
            }
        }
    }

    override fun part1(): Int {
        /* Correct output:
         * #20 addx 4 - 0 + (20 * 17) = 0 + 340 = 340
         * #60 addx -8 - 340 + (60 * 17) = 340 + 1020 = 1360
         * #100 noop - 1360 + (100 * 23) = 1360 + 2300 = 3660
         * #140 addx -2 - 3660 + (140 * 22) = 3660 + 3080 = 6740
         * #180 addx 7 - 6740 + (180 * 21) = 6740 + 3780 = 10520
         * #220 addx 4 - 10520 + (220 * 16) = 10520 + 3520 = 14040
         */
        var x: Int = 1
        val insnStack = ArrayDeque<Pair<String, Int>>(input!!)
        var insnSteps = 0
        var value = 0
        var nextAddx = 0
        var lastOpcode: String = ""
        var lastArg: Int = 0
        for (step in 1 .. 221) {
            if (insnSteps == 0) {
                val (opcode, arg) = insnStack.removeFirst()
                lastOpcode = opcode
                lastArg = arg
                if (opcode == "addx") {
                    insnSteps = 2
                    nextAddx = arg
                } else {
                    insnSteps = 1
                }
            }
            if (step % 40 == 20) {
                log.info("#${step} ${lastOpcode} ${lastArg} - ${value} + (${step} * ${x}) = ${value} + ${step * x} = ${value + (step * x)}")
                value += step * x
            }
            insnSteps--
            x += nextAddx
            nextAddx = 0
        }
        return value
        /*
        return input!!.flatMap { (opcode, arg) ->
                if (opcode == "noop") {
                    listOf<Int>(x)
                } else if (opcode == "addx") {
                    val lastX = x
                    x += arg
                    listOf<Int>(lastX, x)
                } else {
                    listOf<Int>()
                }
            }
            .filterIndexed { i, v -> log.info("${i + 1} ${v} ${(i + 1) * v}"); true }
            .mapIndexed { i, v ->
                (i + 1) * v
            }
            //.filterIndexed { i, v -> (i + 1) in listOf<Int>(20, 60, 100, 140, 180, 220)}
            .filterIndexed { i, v -> (i + 1) <= 220 }
            .sum()
            */
    }

    override fun part2(): Int {
        return input!!.flatMap { (opcode, arg) ->
                val cmd = "${opcode} ${arg}"
                if (opcode == "noop") {
                    listOf<String>(cmd)
                } else if (opcode == "addx") {
                    listOf<String>(cmd, cmd)
                } else {
                    listOf<String>()
                }
            }
            //.filterIndexed { i, v -> log.info("${i + 1} ${v}"); (i + 1) <= 220 }
            .count()
    }

}
