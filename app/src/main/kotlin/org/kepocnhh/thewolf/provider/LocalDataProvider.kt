package org.kepocnhh.thewolf.provider

import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.ThemeState

internal interface LocalDataProvider {
    var themeState: ThemeState
    var tasks: List<Task>
}
