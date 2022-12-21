package co.tenney.ryan.aoc.common

import java.util.PriorityQueue

object Search {

    fun bfs(start: Vertex, edgePredicate: (Edge) -> Boolean = { true }, endCondition: (Vertex) -> Boolean, costFunction: (Int, Vertex) -> Int): Int {
        val toVisit: ArrayDeque<Pair<Vertex, Int>> = ArrayDeque<Pair<Vertex, Int>>()
        toVisit.add(start to 0)

        val visited: MutableSet<Vertex> = mutableSetOf<Vertex>()
        while (!toVisit.isEmpty()) {
            val (vertex, cost) = toVisit.removeFirst()
            if (vertex !in visited) {
                if (endCondition(vertex)) {
                    return cost
                }

                vertex.edges()
                    .filterNot { it.end() in visited }
                    .filter { edgePredicate(it) }
                    .forEach { toVisit.add(it.end() to costFunction(cost, it.end())) }

                visited.add(vertex)
            }
        }
        return Integer.MIN_VALUE
    }

    fun djikstra(start: Vertex, edgePredicate: (Edge) -> Boolean = { true }, endCondition: (Vertex) -> Boolean, costFunction: (Int, Vertex) -> Int, heapFunction: Comparator<Pair<Vertex, Int>>): Int {
        val toVisit: PriorityQueue<Pair<Vertex, Int>> = PriorityQueue<Pair<Vertex, Int>>(heapFunction)
        toVisit.add(start to 0)

        val visited: MutableSet<Vertex> = mutableSetOf<Vertex>()
        while (!toVisit.isEmpty()) {
            val (vertex, cost) = toVisit.poll()
            if (vertex !in visited) {
                if (endCondition(vertex)) {
                    return cost
                }

                vertex.edges()
                    .filterNot { it.end() in visited }
                    .filter { edgePredicate(it) }
                    .forEach { toVisit.add(it.end() to costFunction(cost, it.end())) }

                visited.add(vertex)
            }
        }
        return Integer.MIN_VALUE
    }

    fun <S: Comparable<S>> bfs(ctx: SearchContext<S>, initial: S): S? {
        val states: ArrayDeque<S> = ArrayDeque<S>()
        states.add(initial)

        while (!states.isEmpty()) {
            val state = states.removeFirst()

            if (ctx.complete(state)) {
                return state
            }

            states.addAll(ctx.nextStates(state))
        }

        return null
    }

    fun <S: Comparable<S>> dfs(ctx: SearchContext<S>, initial: S): S? {
        val states: ArrayDeque<S> = ArrayDeque<S>()
        states.add(initial)

        while (!states.isEmpty()) {
            val state = states.removeFirst()

            if (ctx.complete(state)) {
                return state
            }

            states.addAll(ctx.nextStates(state))
        }

        return null
    }

    fun <S: Comparable<S>> djikstra(ctx: SearchContext<S>, initial: S): S? {
        val states: PriorityQueue<S> = PriorityQueue<S>()
        states.add(initial)

        var result: S? = null
        while (!states.isEmpty()) {
            val state = states.poll()

            if (ctx.complete(state)) {
                if (result == null || state < result) {
                    println(state)
                    result = state
                }
            } else {
                states.addAll(ctx.nextStates(state))
            }
        }

        return result
    }

}

interface SearchContext<S: Comparable<S>> {

    fun complete(state: S): Boolean
    fun nextStates(state: S): List<S>

}