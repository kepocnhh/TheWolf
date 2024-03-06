package org.kepocnhh.thewolf.module.tasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.entity.YMD
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.util.calendarOf
import org.kepocnhh.thewolf.util.calendarOfToday
import org.kepocnhh.thewolf.util.duration
import org.kepocnhh.thewolf.util.toYMD
import sp.kx.logics.Logics
import java.util.Calendar
import java.util.TimeZone
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class TasksLogics(
    private val injection: Injection,
) : Logics(injection.contexts.main) {
    data class State(
        val groups: Map<YMD, List<Task>>,
    )

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    fun requestState() = launch {
        val groups = withContext(injection.contexts.default) {
//            delay(2.seconds) // todo
            val minTime = calendarOfToday(
                hours = 0,
                minutes = 0,
                milliseconds = 0,
            ).duration
            val filtered = injection.locals.tasks.filter { task ->
                minTime < task.dateTime
            }
//            injection.locals.tasks = filtered // todo
            filtered.groupBy { task ->
                calendarOf(dateTime = task.dateTime).toYMD()
            }.mapValues { (_, tasks) ->
                tasks.sortedBy { it.dateTime }
            }
        }
        _state.emit(State(groups = groups))
    }

    fun addTask(
        title: String,
        dateTime: Duration,
    ) = launch {
        withContext(injection.contexts.default) {
            val task = Task(
                id = UUID.randomUUID(),
                title = title,
                isChecked = false,
                dateTime = dateTime,
            )
            injection.locals.tasks = injection.locals.tasks + task
        }
    }
}
