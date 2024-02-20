package org.kepocnhh.thewolf.util

class MutableTags(tags: Map<String, Any>) {
    private val map = tags.toMutableMap()

    fun <T : Any> getOrElse(key: String, supplier: () -> T): T {
        return synchronized(map) {
            val value = map[key]
            if (value == null) {
                supplier()
            } else {
                value as? T ?: TODO()
            }
        }
    }

    fun <T : Any> getOrNull(key: String): T? {
        return synchronized(map) {
            val value = map[key]
            if (value == null) {
                null
            } else {
                value as? T ?: TODO()
            }
        }
    }

    fun <T : Any> getOrPut(key: String, supplier: () -> T): T {
        return synchronized(map) {
            val previous = map[key]
            if (previous == null) {
                val value = supplier()
                map[key] = value
                value
            } else {
                previous as? T ?: TODO()
            }
        }
    }

    operator fun iterator(): Iterator<Map.Entry<String, Any>>{
        return map.iterator()
    }

    internal fun clear() {
        map.clear()
    }
}
