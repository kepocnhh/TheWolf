package org.kepocnhh.thewolf.module.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.Strings
import sp.kx.logics.Logics

// todo
internal class SettingsLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    private val _stringsState = MutableStateFlow<Map<String, Strings>?>(null)
    val stringsState = _stringsState.asStateFlow()

    fun requestStrings() = launch {
        val map = withContext(injection.contexts.default) {
            TODO()
        }
        _stringsState.emit(map)
    }
}
