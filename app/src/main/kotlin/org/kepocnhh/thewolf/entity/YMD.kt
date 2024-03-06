package org.kepocnhh.thewolf.entity

internal data class YMD(
    val year: Int,
    val month: Int,
    val day: Int,
) : Comparable<YMD> {
    init {
        require(year > 0)
        require(month in 0..11)
        require(day in 1..31)
    }

    override fun compareTo(other: YMD): Int {
        if (year > other.year) return 1
        if (year < other.year) return -1
        if (month > other.month) return 1
        if (month < other.month) return -1
        if (day > other.day) return 1
        if (day < other.day) return -1
        return 0
    }
}
