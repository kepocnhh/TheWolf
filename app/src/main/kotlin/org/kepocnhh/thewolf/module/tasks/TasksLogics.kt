package org.kepocnhh.thewolf.module.tasks

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.entity.YMD
import org.kepocnhh.thewolf.module.app.Injection
import sp.kx.logics.Logics
import java.util.Calendar
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
            val minTimeInMillis = Calendar.getInstance().let {
                it[Calendar.HOUR] = 0
                it[Calendar.MINUTE] = 0
                it[Calendar.MILLISECOND] = 0
                it.timeInMillis
            }
            val filtered = injection.locals.tasks.filter { task ->
                minTimeInMillis <= task.date.inWholeMilliseconds
            }
//            injection.locals.tasks = filtered // todo
            filtered.groupBy { task ->
                val calendar = Calendar.getInstance().also {
                    it.timeInMillis = task.date.inWholeMilliseconds
                }
                YMD(
                    year = calendar[Calendar.YEAR],
                    month = calendar[Calendar.MONTH],
                    day = calendar[Calendar.DAY_OF_MONTH],
                )
            }.mapValues { (_, tasks) ->
                tasks.sortedBy { it.date }
            }
        }
        _state.emit(State(groups = groups))
    }
}
