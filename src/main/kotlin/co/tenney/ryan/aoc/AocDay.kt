package co.tenney.ryan.aoc

import co.tenney.ryan.util.LoggingContext
import co.tenney.ryan.util.inputFileToLines
import co.tenney.ryan.aoc.AocProblem

abstract class AocDay<O>(val day: Int): AocProblem<List<String>, O>, LoggingContext<AocDay<O>> {

}