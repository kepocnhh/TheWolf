package org.kepocnhh.thewolf

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.module.app.Injection
import sp.kx.logics.Logics

internal class BarLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    data class State(val clicks: Int)
    private val _state = MutableStateFlow(State(clicks = 0))
    val state = _state.asStateFlow()

    fun click() = launch {
        _state.value = withContext(injection.contexts.default) {
            state.value.copy(clicks = state.value.clicks + 1)
        }
    }
}
