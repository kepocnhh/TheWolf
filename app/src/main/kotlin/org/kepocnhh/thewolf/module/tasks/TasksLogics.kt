package org.kepocnhh.thewolf.module.tasks

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.Injection
import sp.kx.logics.Logics
import java.util.Calendar
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

    private fun getTasks(): List<Task> {
        val tasks = injection.locals.tasks
        tasks.forEachIndexed { index, task ->
            println("$index] $task")
        } // todo
        val completed = injection.locals.completed
        completed.forEachIndexed { index, id ->
            println("$index] complete $id")
        } // todo
        val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
        return tasks
            .filter {
                val isNotRepeated = it.repeated.isEmpty()
                val today = it.repeated.contains(dayOfWeek)
                val isCompleted = injection.locals.completed.contains(it.id)
                isNotRepeated || today && !isCompleted
            }
            .sortedBy { it.created }
    }

    fun requestState() = launch {
        val tasks = withContext(injection.contexts.default) {
            getTasks()
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

    private fun completeTask(id: UUID) {
        val task = injection.locals.tasks.firstOrNull { it.id == id } ?: TODO()
        val isRepeated = task.repeated.isNotEmpty()
        if (isRepeated) {
            injection.locals.completed += task.id
        } else {
            val tasks = injection.locals.tasks.toMutableList()
            tasks.removeIf { it.id == id }
            injection.locals.tasks = tasks
        }
    }

    fun deleteTask(id: UUID) = launch {
        val tasks = withContext(injection.contexts.default) {
            completeTask(id = id)
            getTasks()
        }
        _state.emit(State(tasks = tasks))
    }
}
