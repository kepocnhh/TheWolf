package org.kepocnhh.thewolf.util

class LogicProvider(
    private val factory: LogicFactory = SimpleLogicFactory,
) {
    private object SimpleLogicFactory : LogicFactory {
        override fun <T : Logic> create(type: Class<T>): T {
            return type
                .getConstructor()
                .newInstance()
        }
    }

    private data class CompositeKey<T : Logic>(val label: String, val type: Class<T>)

    private val logics: MutableMap<CompositeKey<out Logic>, Logic> = mutableMapOf()

    fun <T : Logic> get(label: String, type: Class<T>): T {
        return logics.getOrPut(CompositeKey(label = label, type = type)) { factory.create(type) } as T
    }

    fun <T : Logic> contains(label: String, type: Class<T>): Boolean {
        return logics.containsKey(CompositeKey(label = label, type = type))
    }

    fun <T : Logic> remove(label: String, type: Class<T>) {
        logics
            .remove(CompositeKey(label = label, type = type))
            ?.clear()
    }
}

inline fun <reified T : Logic> LogicProvider.get(label: String): T {
    return get(label = label, type = T::class.java)
}

inline fun <reified T : Logic> LogicProvider.contains(label: String): Boolean {
    return contains(label = label, type = T::class.java)
}

inline fun <reified T : Logic> LogicProvider.remove(label: String) {
    remove(label = label, type = T::class.java)
}
