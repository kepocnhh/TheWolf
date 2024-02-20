package org.kepocnhh.thewolf.module.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.util.Logic
import org.kepocnhh.thewolf.util.launch

internal class ThemeLogic(
    private val injection: Injection,
) : Logic(injection.contexts.main) {
    private val _state = MutableStateFlow<ThemeState?>(null)
    val state = _state.asStateFlow()

    fun requestThemeState() = launch {
        _state.value = withContext(injection.contexts.default) {
            injection.locals.themeState
        }
    }

    fun setThemeState(value: ThemeState) = launch {
        withContext(injection.contexts.default) {
            injection.locals.themeState = value
        }
        _state.value = value
    }
}
