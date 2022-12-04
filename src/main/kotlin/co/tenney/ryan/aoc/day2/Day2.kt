package co.tenney.ryan.aoc.day2

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.util.LoggingContext

class Day2 : AocDay<Int>(2), LoggingContext<Day2> {

    var rounds: List<Round>? = null

    override fun processInput(input: List<String>): Unit {
        rounds = input.map(::parseRound)
    }

    override fun part1(): Int {
        return rounds!!.map({ scoreRound(it).score + it.me.score }).sum()
    }

    override fun part2(): Int {
        return rounds!!.map({ it ->
            val desiredResult = moveToResult(it.me)
            val move = getMove(it.opponent, desiredResult)
            val round = Round(it.opponent, move)
            val result = scoreRound(round)
            result.score + move.score
        }).sum()
    }

    data class Round(val opponent: Move, val me: Move)

    enum class Result(val score: Int) {
        LOST(0),
        DRAW(3),
        WON(6),
        INVALID(Integer.MIN_VALUE)
    }

    enum class Move(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
        INVALID(Integer.MIN_VALUE)
    }

    fun parseRound(round: String): Round {
        val opponent = parseMove(round[0])
        val me = parseMove(round[2])
        return Round(opponent, me)
    }

    fun parseMove(move: Char): Move {
        return when (move) {
            'A', 'X' -> Move.ROCK
            'B', 'Y' -> Move.PAPER
            'C', 'Z' -> Move.SCISSORS
            else -> Move.INVALID
        }
    }

    fun moveToResult(result: Move): Result {
        return when (result) {
            Move.ROCK -> Result.LOST
            Move.PAPER -> Result.DRAW
            Move.SCISSORS -> Result.WON
            Move.INVALID -> Result.INVALID
        }
    }

    fun getMove(opponent: Move, result: Result): Move {
        if (result == Result.DRAW) {
            return opponent
        }
        return when (Pair(opponent, result)) {
            Pair(Move.ROCK, Result.LOST) -> Move.SCISSORS
            Pair(Move.ROCK, Result.WON) -> Move.PAPER
            Pair(Move.PAPER, Result.LOST) -> Move.ROCK
            Pair(Move.PAPER, Result.WON) -> Move.SCISSORS
            Pair(Move.SCISSORS, Result.LOST) -> Move.PAPER
            Pair(Move.SCISSORS, Result.WON) -> Move.ROCK
            else -> Move.INVALID
        }
    }

    fun scoreRound(round: Round): Result {
        return when (round) {
            Round(Move.ROCK, Move.ROCK) -> Result.DRAW
            Round(Move.ROCK, Move.PAPER) -> Result.WON
            Round(Move.ROCK, Move.SCISSORS) -> Result.LOST
            Round(Move.PAPER, Move.ROCK) -> Result.LOST
            Round(Move.PAPER, Move.PAPER) -> Result.DRAW
            Round(Move.PAPER, Move.SCISSORS) -> Result.WON
            Round(Move.SCISSORS, Move.ROCK) -> Result.WON
            Round(Move.SCISSORS, Move.PAPER) -> Result.LOST
            Round(Move.SCISSORS, Move.SCISSORS) -> Result.DRAW
            else -> Result.INVALID
        }
    }

}