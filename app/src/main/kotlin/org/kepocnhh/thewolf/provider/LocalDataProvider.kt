package org.kepocnhh.thewolf.provider

import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.ThemeState
import java.util.UUID

internal interface LocalDataProvider {
    var themeState: ThemeState
    var tasks: List<Task>
    var completed: Set<UUID>
}
