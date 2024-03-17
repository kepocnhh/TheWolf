package org.kepocnhh.thewolf.module.tasks

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.Injection
import sp.kx.logics.Logics
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

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
//            delay(2.seconds) // todo
            injection.locals.tasks.sortedBy { it.created }
        }
        _state.emit(State(tasks = tasks))
    }

    fun addTask(title: String, repeated: Set<Int>?) = launch {
        val tasks = withContext(injection.contexts.default) {
            val task = Task(
                id = UUID.randomUUID(),
                title = title,
                created = System.currentTimeMillis().milliseconds,
                repeated = repeated.orEmpty(),
            )
            (injection.locals.tasks + task).sortedBy { it.created }
        }
        withContext(injection.contexts.default) {
            injection.locals.tasks = tasks
        }
        _state.emit(State(tasks = tasks))
    }

    fun deleteTask(id: UUID) = launch {
        val tasks = withContext(injection.contexts.default) {
            val tasks = injection.locals.tasks.toMutableList()
            tasks.removeIf { it.id == id }
            injection.locals.tasks = tasks
            tasks
        }
        _state.emit(State(tasks = tasks))
    }
}
