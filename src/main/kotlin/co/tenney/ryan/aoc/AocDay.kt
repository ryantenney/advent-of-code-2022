package co.tenney.ryan.aoc

import co.tenney.ryan.util.LoggingContext
import co.tenney.ryan.util.inputFileToLines
import co.tenney.ryan.aoc.AocProblem

abstract class AocDay<O>(val day: Int): AocProblem<O>, LoggingContext<AocDay<O>>{
    override fun processInput(): List<String> {
        return inputFileToLines("input-day$day.txt")
            .map {
        //        log.debug(it)
                it
            }
    }
}