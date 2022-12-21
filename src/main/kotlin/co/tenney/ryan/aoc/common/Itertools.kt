package co.tenney.ryan.aoc.common

object Itertools {

    fun <S> combinations(values: Iterable<S>): List<Pair<S, S>> {
        val output = mutableListOf<Pair<S, S>>()
        for (a in values) {
            for (b in values) {
                if (a != b) {
                    output.add(a to b)
                }
            }
        }
        return output.toList()
    }

}
