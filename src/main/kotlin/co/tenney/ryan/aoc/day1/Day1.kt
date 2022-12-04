package co.tenney.ryan.aoc.day1

import co.tenney.ryan.aoc.AocDay

class Day1 : AocDay<Int>(1) {

    var calories: List<Int>? = null

    override fun processInput(input: List<String>): Unit {
        var currentCalories: Int = 0
        var caloriesList = mutableListOf<Int>()
        input.forEach {
            if (it.isBlank()) {
                caloriesList.add(currentCalories)
                currentCalories = 0
            } else {
                currentCalories += Integer.parseInt(it)
            }
        }
        calories = caloriesList.toList().sortedDescending()
    }

    override fun part1(): Int {
        return calories!!.get(0)
    }

    override fun part2(): Int {
        return calories!!.subList(0, 3).sum()
    }

}