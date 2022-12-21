package co.tenney.ryan.aoc.day16

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.Edge
import co.tenney.ryan.aoc.common.IntPosition
import co.tenney.ryan.aoc.common.Itertools
import co.tenney.ryan.aoc.common.Search
import co.tenney.ryan.aoc.common.SearchContext
import co.tenney.ryan.aoc.common.Vertex

class Day16 : AocDay<Int>(16) {

    val pattern = Regex("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)")

    var valves: Map<String, Valve>? = null

    override fun processInput(input: List<String>): Unit {
        val input1 = """
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II""".split("\n").map(String::trim)

        val valvesMap = input1.filter(String::isNotBlank).map { line ->
            val (_, valve, flow, tunnels) = pattern.matchEntire(line)!!.groupValues
            valve to (flow.toInt() to tunnels.split(",").map(String::trim).toList())
        }.toMap()

        val valves = valvesMap.map {
            val (valveId, info) = it
            val (flow, _) = info
            valveId to Valve(valveId, flow)
        }.toMap()

        valvesMap.forEach {
            val (valveId, info) = it
            val (_, tunnelIds) = info
            val valve = valves.get(valveId)!!
            tunnelIds.forEach { valve.addTunnel(valves.get(it)!!) }
        }

        val nonZeroFlowValvePairs = Itertools.combinations(valves.values.filter { it.flow != 0 || it.valve == "AA" })

        nonZeroFlowValvePairs.forEach { pair ->
            val cost = Search.bfs(pair.first, { !(it as Tunnel).synthetic }, { it == pair.second }, { cost, _ -> cost + 1 })
            //println("${pair.first.valve} -> ${pair.second.valve} == ${cost}")
            pair.first.addTunnel(pair.second, cost, true)
        }

        valves.values.forEach { it.removeTunnels { !it.synthetic } }

        this.valves = valves
    }

    override fun part1(): Int {
        val initial = ValveState.initial(valves!!.get("AA")!!)
        val ctx = ValveSearchContext()
        val primal = Search.djikstra(ctx, initial)!!
        println(primal)
        return primal.throughput
    }

    override fun part2(): Int {
        return 0
    }

}

data class Valve(val valve: String, val flow: Int): Vertex {

    private val tunnels: MutableList<Tunnel> = mutableListOf<Tunnel>()

    override fun edges() = tunnels.toList()

    fun addTunnel(end: Valve, distance: Int = 1, synthetic: Boolean = false) {
        this.tunnels.add(Tunnel(this, end, distance, synthetic))
    }

    fun removeTunnels(filter: (Tunnel) -> Boolean) {
        this.tunnels.removeIf(filter)
    }

}

data class Tunnel(val start: Valve, val end: Valve, val distance: Int, val synthetic: Boolean): Edge {
    override fun start() = start
    override fun end() = end
    override fun cost() = distance
}

class ValveSearchContext: SearchContext<ValveState> {

    val revisit = true

    override fun complete(state: ValveState): Boolean {
        return state.time >= 30
    }

    override fun nextStates(state: ValveState): List<ValveState> {
        val moveStates = state.location.edges()
            //.filter { revisit || it.end !in state.opened }
            .filter(Tunnel::synthetic)
            .mapNotNull(state::move)

        if (state.isOpen() || state.flow() == 0) {
            return moveStates
        } else {
            return moveStates + state.open()
        }
    }

}

data class ValveState(val time: Int, val throughput: Int, val location: Valve, val opening: Boolean, val opened: List<Valve>): Comparable<ValveState> {

    fun move(tunnel: Tunnel): ValveState? {
        if (time + tunnel.distance > 30) {
            return null
        }
        val nextThroughput = throughput + (opened.sumOf { it.flow } * tunnel.distance)
        return copy(time = time + tunnel.distance, throughput = nextThroughput, location = tunnel.end, opening = false)
    }

    fun open(): ValveState {
        val nextThroughput = throughput + opened.sumOf { it.flow }
        return copy(time = time + 1, throughput = nextThroughput, opening = true, opened = opened + location)
    }

    fun flow(): Int {
        return location.flow
    }

    fun isOpen(): Boolean {
        return location in opened
    }

    override fun compareTo(other: ValveState): Int {
        val throughputCmp = -throughput.compareTo(other.throughput)
        if (throughputCmp != 0) {
            return throughputCmp
        }

        val flowCmp = -location.flow.compareTo(other.flow())
        if (flowCmp != 0) {
            return flowCmp
        }

        val timeCmp = time.compareTo(other.time)
        if (timeCmp != 0) {
            return timeCmp
        }

        return 0
    }

    companion object {

        fun initial(location: Valve): ValveState {
            return ValveState(0, 0, location, false, emptyList<Valve>())
        }

    }

}
