package co.tenney.ryan.aoc.day15

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.IntPosition

class Day15 : AocDay<Int>(15) {

    val numbers = Regex("-?\\d+")

    var input: List<SensorBeacon>? = null

    override fun processInput(input: List<String>): Unit {
        val input1 = """
        Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        Sensor at x=9, y=16: closest beacon is at x=10, y=16
        Sensor at x=13, y=2: closest beacon is at x=15, y=3
        Sensor at x=12, y=14: closest beacon is at x=10, y=16
        Sensor at x=10, y=20: closest beacon is at x=10, y=16
        Sensor at x=14, y=17: closest beacon is at x=10, y=16
        Sensor at x=8, y=7: closest beacon is at x=2, y=10
        Sensor at x=2, y=0: closest beacon is at x=2, y=10
        Sensor at x=0, y=11: closest beacon is at x=2, y=10
        Sensor at x=20, y=14: closest beacon is at x=25, y=17
        Sensor at x=17, y=20: closest beacon is at x=21, y=22
        Sensor at x=16, y=7: closest beacon is at x=15, y=3
        Sensor at x=14, y=3: closest beacon is at x=15, y=3
        Sensor at x=20, y=1: closest beacon is at x=15, y=3""".split("\n").map(String::trim)

        val input2 = "Sensor at x=8, y=7: closest beacon is at x=2, y=10".split("\n").map(String::trim)

        this.input = input.filter(String::isNotBlank).map { line ->
            val numbers = this.numbers.findAll(line).map(MatchResult::value).map(String::toInt).toList()
            val (sensorX, sensorY) = numbers.take(2)
            val (beaconX, beaconY) = numbers.drop(2)
            SensorBeacon(IntPosition(sensorX, sensorY), IntPosition(beaconX, beaconY))
        }
    }

    fun width(sensor: IntPosition, distance: Int, yTarget: Int): IntRange? {
        val width = distance - Math.abs(sensor.y - yTarget)
        if (width < 0) {
            return null
        } else {
            return (sensor.x - width) .. (sensor.x + width)
        }
    }

    override fun part1(): Int {
        val targetY = 2000000
        var filtered: List<IntRange> = this.input!!.mapNotNull({
            width(it.sensor, it.distance(), targetY)
        })

        val output = merge(filtered)

        val beaconsOnY = this.input!!.map(SensorBeacon::beacon).distinct().count({ beacon ->
            beacon.y == targetY && output.any({ it.contains(beacon.x) })
        })

        return output.sumOf(IntRange::count) - beaconsOnY
    }

    override fun part2(): Int {
        for (y in 0 .. 4000000) {
            val filtered: MutableList<IntRange> = this.input!!.mapNotNull({
                width(it.sensor, it.distance(), y)
            }).toMutableList()

            val beacons: List<IntRange> = this.input!!.map(SensorBeacon::beacon).distinct().filter({ it.y == y }).map { it.x .. it.x }

            filtered.addAll(beacons)

            val merged = merge(filtered)

            if (merged.size != 1) {
                println(y)
                println(merged)
                println((merged.first().endInclusive + 1) * 4000000L + y)
            }
        }

        return 0
    }

    fun merge(input: List<IntRange>): List<IntRange> {
        val sorted = input.sortedBy(IntRange::endInclusive).sortedBy(IntRange::start)
        val output = mutableListOf<IntRange>()
        var currentRange = sorted.first()
        for (range in sorted.drop(1)) {
            if (range.start <= currentRange.endInclusive) {
                currentRange = currentRange.start .. Math.max(range.endInclusive, currentRange.endInclusive)
            } else {
                output.add(currentRange)
                currentRange = range
            }
        }
        output.add(currentRange)
        return output.toList()
    }

}

data class SensorBeacon(val sensor: IntPosition, val beacon: IntPosition) {

    fun distance(): Int {
        val delta = (sensor - beacon).abs()
        return delta.x + delta.y
    }

}
