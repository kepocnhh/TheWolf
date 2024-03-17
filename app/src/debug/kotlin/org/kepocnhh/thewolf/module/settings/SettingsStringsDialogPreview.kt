package org.kepocnhh.thewolf.module.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.PreviewComposition
import org.kepocnhh.thewolf.module.app.StringsType
import org.kepocnhh.thewolf.module.app.StringsTypeProvider
import org.kepocnhh.thewolf.module.app.ThemeState

@Composable
private fun SettingsStringsDialogPreview(
    themeState: ThemeState,
    selected: StringsType,
    list: List<StringsType>,
) {
    PreviewComposition(
        themeState = themeState,
    ) {
        SettingsStringsDialog(
            selected = selected,
            list = list,
            onSelect = {
                // noop
            },
            onDismiss = {
                // noop
            }
        )
    }
}

@Preview(name = "StringsType")
@Composable
private fun SettingsStringsDialogStringsTypePreview(
    @PreviewParameter(StringsTypeProvider::class) stringsType: StringsType,
) {
    val themeState = ThemeState(
        colorsType = ColorsType.Dark,
        stringsType = stringsType,
    )
    SettingsStringsDialogPreview(
        themeState = themeState,
        selected = stringsType,
        list = listOf(
            StringsType.Locale("en"),
            StringsType.Locale("ru"),
            StringsType.Auto,
        ),
    )
}
