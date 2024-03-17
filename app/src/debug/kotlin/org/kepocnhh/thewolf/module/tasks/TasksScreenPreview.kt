package org.kepocnhh.thewolf.module.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ColorsTypeProvider
import org.kepocnhh.thewolf.module.app.PreviewComposition
import org.kepocnhh.thewolf.module.app.StringsType
import org.kepocnhh.thewolf.module.app.ThemeState
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

@Composable
private fun TasksScreenPreview(
    themeState: ThemeState,
    tasks: List<Task> = emptyList(),
) {
    PreviewComposition(
        themeState = themeState,
    ) {
        TasksScreen(
            tasks = tasks,
            onNewTask = {
                // noop
            },
            onSettings = {
                // noop
            },
            onDelete = {
                // noop
            },
        )
    }
}

@Preview(name = "ColorsType")
@Composable
private fun TasksScreenColorsTypePreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
        stringsType = StringsType.Auto,
    )
    TasksScreenPreview(
        themeState = themeState,
    )
}

@Preview(name = "Tasks")
@Composable
private fun TasksScreenTasksPreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
        stringsType = StringsType.Auto,
    )
    TasksScreenPreview(
        themeState = themeState,
        tasks = listOf(
            Task(
                id = UUID.randomUUID(),
                title = "foo",
                created = 1.seconds,
                repeated = emptySet(),
            ),
            Task(
                id = UUID.randomUUID(),
                title = "bar",
                created = 2.seconds,
                repeated = emptySet(),
            ),
        ),
    )
}
