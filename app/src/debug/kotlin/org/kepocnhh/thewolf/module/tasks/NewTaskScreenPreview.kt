package org.kepocnhh.thewolf.module.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
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
    isFocused: Boolean,
    title: String,
) {
    PreviewComposition(
        themeState = themeState,
    ) {
        val titleState = remember { mutableStateOf(TextFieldValue(title)) }
        NewTaskScreen(
            onBack = {
                // noop
            },
            onNewTask = {
                // noop
            },
            onFocusChanged = {
                // noop
            },
            isFocused = isFocused,
            titleState = titleState,
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
        isFocused = false,
        title = "",
    )
}

@Preview(name = "Focused")
@Composable
private fun NewTaskScreenFocusedPreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
        stringsType = StringsType.Auto,
    )
    NewTaskScreenPreview(
        themeState = themeState,
        isFocused = true,
        title = "",
    )
}

@Preview(name = "Title")
@Composable
private fun NewTaskScreenTitlePreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
        stringsType = StringsType.Auto,
    )
    NewTaskScreenPreview(
        themeState = themeState,
        isFocused = true,
        title = "foobar",
    )
}
