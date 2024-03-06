package org.kepocnhh.thewolf.module.tasks

import org.kepocnhh.thewolf.entity.Task
import java.util.UUID
import kotlin.time.Duration

internal class TaskFactory(
    private val title: String,
    private val dateTime: Duration, // todo UTC-0
) {
    fun getTask(id: UUID): Task {
        return Task(
            id = id,
            title = title,
            dateTime = dateTime,
            isChecked = false,
        )
    }
}
