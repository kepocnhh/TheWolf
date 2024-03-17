package org.kepocnhh.thewolf.entity

import java.util.UUID
import kotlin.time.Duration

internal data class Task(
    val id: UUID,
    val title: String,
    val created: Duration, // todo UTC-0
    val repeated: Set<Int>, // days of week
)
