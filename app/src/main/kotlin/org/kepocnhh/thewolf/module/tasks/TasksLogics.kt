package org.kepocnhh.thewolf.module.tasks

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.entity.YMD
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.util.calendarOfToday
import org.kepocnhh.thewolf.util.duration
import org.kepocnhh.thewolf.util.setDateTime
import org.kepocnhh.thewolf.util.toYMD
import sp.kx.logics.Logics
import java.util.UUID
import kotlin.time.Duration

internal class TasksLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    data class State(
        val groups: Map<YMD, List<Task>>,
    )

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private fun getTaskGroups(tasks: List<Task>): Map<YMD, List<Task>> {
        val calendar = calendarOfToday(
            hours = 0,
            minutes = 0,
            milliseconds = 0,
        )
        val minTime = calendar.duration
        return tasks.filter { task ->
            minTime < task.dateTime
        }.groupBy { task ->
            calendar.setDateTime(dateTime = task.dateTime)
            calendar.toYMD()
        }.mapValues { (_, tasks) ->
            tasks.sortedBy { it.dateTime }
        }
    }

    fun requestState() = launch {
        val groups = withContext(injection.contexts.default) {
//            delay(2.seconds) // todo
            getTaskGroups(tasks = injection.locals.tasks)
        }
        _state.emit(State(groups = groups))
    }

    fun addTask(factory: TaskFactory) = launch {
        val tasks = withContext(injection.contexts.default) {
            val task = factory.getTask(id = UUID.randomUUID())
            injection.locals.tasks + task
        }
        val groups = withContext(injection.contexts.default) {
            injection.locals.tasks = tasks
            getTaskGroups(tasks = tasks)
        }
        _state.emit(State(groups = groups))
    }
}
