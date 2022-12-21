package co.tenney.ryan.aoc.day12

import co.tenney.ryan.aoc.AocDay
import co.tenney.ryan.aoc.common.IntPosition
import co.tenney.ryan.aoc.common.Direction

class Day12 : AocDay<Int>(12) {

    var nodes: Map<IntPosition, Node>? = null
    var start: Node? = null
    var end: Node? = null

    override fun processInput(input: List<String>): Unit {
        val nodeList = input
            .filter(String::isNotBlank)
            .flatMapIndexed { y, line ->
                line.toCharArray()
                    .mapIndexed { x, elevChar ->
                        val elev = when (elevChar) {
                            'S' -> 'a'
                            'E' -> 'z'
                            else -> elevChar
                        } - 'a'
                        val node = Node(IntPosition(x, y), elev)
                        when (elevChar) {
                            'S' -> start = node
                            'E' -> end = node
                        }
                        node
                    }
            }

        val nodes = nodeList.map { node -> node.pos to node }.toMap()

        nodeList.forEach { node ->
            neighbors(node, nodes).forEach(node::addNeighbor)
        }

        this.nodes = nodes
    }

    override fun part1(): Int {
        return search(start!!, { n1, n2 -> n2.elev - n1.elev <= 1 }, { it == end!! })
    }

    override fun part2(): Int {
        return search(end!!, { n1, n2 -> n1.elev - n2.elev <= 1 }, { it.elev == 0 })
    }

    fun search(start: Node, validPredicate: (Node, Node) -> Boolean, endCondition: (Node) -> Boolean): Int {
        val toVisit: ArrayDeque<Pair<Node, Int>> = ArrayDeque<Pair<Node, Int>>()
        toVisit.add(start to 0)

        val visited: MutableSet<Node> = mutableSetOf<Node>()
        while (!toVisit.isEmpty()) {
            val (node, cost) = toVisit.removeFirst()
            if (node !in visited) {
                if (endCondition(node)) {
                    return cost
                }

                /*
                node.neighbors
                    .filterNot { it in visited }
                    .filter { validPredicate(node, it) }
                    .forEach { toVisit.add(it to cost + 1) }
                    */

                node.edges()
                    .filterNot { it.end in visited }
                    .filter { validPredicate(node, it.end) }
                    .forEach { toVisit.add(it.end to cost + 1) }

                visited.add(node)
            }
        }
        return Integer.MIN_VALUE
    }

    fun neighbors(node: Node, nodes: Map<IntPosition, Node>): List<Node> {
        return Direction.values()
                .map { dir -> node.pos.transpose(dir) }
                .mapNotNull(nodes::get)
    }

}

data class Node(val pos: IntPosition, val elev: Int) {

    var neighbors: MutableSet<Node> = mutableSetOf<Node>()

    fun addNeighbor(neighbor: Node) {
        neighbors.add(neighbor)
    }

    fun neighbors(): Set<Node> {
        return neighbors.toSet()
    }

    fun edges(): Set<Edge> {
        return neighbors.map {
            Edge(this, it, it.elev - elev)
        }.toSet()
    }

}

data class Edge(val start: Node, val end: Node, val weight: Int)
