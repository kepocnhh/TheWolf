package org.kepocnhh.thewolf.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.kepocnhh.thewolf.module.app.Injection
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class Logic {
    internal val tags: MutableTags

    constructor() : this(emptyMap())

    constructor(context: CoroutineContext) : this(mapOf(COROUTINE_CONTEXT_KEY to context))

    constructor(tags: Map<String, Any>) {
        this.tags = MutableTags(tags)
    }

    internal fun clear() {
        for ((_, tag) in tags) {
            if (tag !is Closeable) continue
            tag.close()
        }
        tags.clear()
    }
}

private class FooLogic(
    private val injection: Injection,
) : Logic(tags = mapOf(COROUTINE_CONTEXT_KEY to injection.contexts.main)) {
    fun foo() {
        coroutineScope.launch(tags.getOrNull(COROUTINE_CONTEXT_KEY) ?: TODO()) {
            // todo
        }
    }
}

private class BarLogic(
    private val injection: Injection,
) : Logic(context = injection.contexts.main) {
    fun bar() = launch {
        // todo
    }
}

fun <T> Logic.getCoroutineScope(
    key: String,
    supplier: () -> T,
): CoroutineScope where T : Closeable, T : CoroutineScope {
    return tags.getOrPut(key = key, supplier = supplier)
}

private const val COROUTINE_SCOPE_KEY = "org.kepocnhh.thewolf.util.COROUTINE_SCOPE_KEY" // todo
private const val COROUTINE_CONTEXT_KEY = "org.kepocnhh.thewolf.util.COROUTINE_CONTEXT_KEY" // todo

val Logic.coroutineScope: CoroutineScope
    get() {
        return getCoroutineScope(
            key = COROUTINE_SCOPE_KEY,
            supplier = { CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) },
        )
    }

fun Logic.launch(
    context: CoroutineContext = tags.getOrElse(COROUTINE_CONTEXT_KEY) { EmptyCoroutineContext },
    block: suspend CoroutineScope.() -> Unit,
) {
    coroutineScope.launch(context, block = block)
}

internal class CloseableCoroutineScope(
    override val coroutineContext: CoroutineContext,
) : Closeable, CoroutineScope {
    override fun close() {
        coroutineContext.cancel()
    }
}
