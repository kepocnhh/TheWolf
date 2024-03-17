package org.kepocnhh.thewolf.module.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ColorsTypeProvider
import org.kepocnhh.thewolf.module.app.PreviewComposition
import org.kepocnhh.thewolf.module.app.StringsType
import org.kepocnhh.thewolf.module.app.ThemeState

@Composable
private fun NewTaskScreenPreview(
    themeState: ThemeState,
) {
    PreviewComposition(
        themeState = themeState,
    ) {
        NewTaskScreen(
            onBack = {
                // noop
            },
            onNewTask = {
                // noop
            },
        )
    }
}

@Preview(name = "ColorsType")
@Composable
private fun NewTaskScreenColorsTypePreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
        stringsType = StringsType.Auto,
    )
    NewTaskScreenPreview(
        themeState = themeState,
    )
}
