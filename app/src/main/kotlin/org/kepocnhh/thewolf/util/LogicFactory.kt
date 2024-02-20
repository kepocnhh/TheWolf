package org.kepocnhh.thewolf.util

interface LogicFactory {
    fun <T : Logic> create(type: Class<T>): T
}
