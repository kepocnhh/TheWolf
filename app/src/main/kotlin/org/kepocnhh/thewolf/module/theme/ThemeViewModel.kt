package org.kepocnhh.thewolf.module.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.util.lifecycle.AbstractViewModel

internal class ThemeViewModel(
    private val injection: Injection,
) : AbstractViewModel() {
    private val _state = MutableStateFlow<ThemeState?>(null)
    val state = _state.asStateFlow()

    fun requestThemeState() = injection.launch {
        _state.value = withContext(injection.contexts.default) {
            injection.locals.themeState
        }
    }

    fun setThemeState(value: ThemeState) = injection.launch {
        withContext(injection.contexts.default) {
            injection.locals.themeState = value
        }
        _state.value = value
    }
}
