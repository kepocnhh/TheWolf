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
    private val mainCoroutineContext: CoroutineContext
    private val tags: MutableTags

    constructor() : this(
        mainCoroutineContext = EmptyCoroutineContext,
        tags = emptyMap(),
    )

    constructor(mainCoroutineContext: CoroutineContext) : this(
        mainCoroutineContext = mainCoroutineContext,
        tags = emptyMap(),
    )

    constructor(
        mainCoroutineContext: CoroutineContext,
        tags: Map<String, Any>,
    ) {
        this.mainCoroutineContext = mainCoroutineContext
        this.tags = MutableTags(tags)
    }

    internal fun clear() {
        for ((_, tag) in tags) {
            if (tag !is Closeable) continue
            tag.close()
        }
        tags.clear()
    }

    protected fun <T> getCoroutineScope(
        key: String,
        supplier: () -> T,
    ): CoroutineScope where T : Closeable, T : CoroutineScope {
        return tags.getOrPut(key = key, supplier = supplier)
    }

    protected val coroutineScope: CoroutineScope
        get() {
            return getCoroutineScope(
                key = COROUTINE_SCOPE_KEY,
                supplier = { CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) },
            )
        }

    protected fun launch(
        context: CoroutineContext = mainCoroutineContext,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        coroutineScope.launch(context, block = block)
    }

    companion object {
        private const val COROUTINE_SCOPE_KEY = "org.kepocnhh.thewolf.util.COROUTINE_SCOPE_KEY" // todo
    }
}

private class FooLogic(
    private val injection: Injection,
) : Logic() {
    fun foo() {
        coroutineScope.launch(injection.contexts.main) {
            // todo
        }
    }

    fun foo2() {
        getCoroutineScope(key = "foo") {
            CloseableCoroutineScope(SupervisorJob())
        }.launch(injection.contexts.main) {
            // todo
        }
    }
}

private class BarLogic(
    private val injection: Injection,
) : Logic(injection.contexts.main) {
    fun bar() = launch {
        // todo
    }
}

private class BazLogic(
    private val injection: Injection,
) : Logic() {
    fun baz() = launch(injection.contexts.main) {
        // todo
    }
}

internal class CloseableCoroutineScope(
    override val coroutineContext: CoroutineContext,
) : Closeable, CoroutineScope {
    override fun close() {
        coroutineContext.cancel()
    }
}
