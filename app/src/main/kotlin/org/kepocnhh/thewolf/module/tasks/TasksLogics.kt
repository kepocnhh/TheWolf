package org.kepocnhh.thewolf.module.tasks

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.Injection
import sp.kx.logics.Logics

internal class TasksLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    data class State(
        val tasks: List<Task>,
    )

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    fun requestState() = launch {
        val tasks = withContext(injection.contexts.default) {
            injection.locals.tasks.sortedBy { it.date }
        }
        _state.emit(State(tasks = tasks))
    }
}
