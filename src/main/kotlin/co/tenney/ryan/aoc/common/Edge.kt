package co.tenney.ryan.aoc.common

interface Edge {

    fun start(): Vertex
    fun end(): Vertex
    fun cost(): Int

}