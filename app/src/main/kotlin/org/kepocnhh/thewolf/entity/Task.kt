package org.kepocnhh.thewolf.entity

import java.util.UUID
import kotlin.time.Duration

internal data class Task(
    val id: UUID,
    val title: String,
    val isChecked: Boolean,
    val date: Duration,
)
