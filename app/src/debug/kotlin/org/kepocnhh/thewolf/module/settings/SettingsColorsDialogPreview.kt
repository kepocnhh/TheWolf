package org.kepocnhh.thewolf.module.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ColorsTypeProvider
import org.kepocnhh.thewolf.module.app.PreviewComposition
import org.kepocnhh.thewolf.module.app.ThemeState

@Composable
private fun SettingsColorsDialogPreview(
    themeState: ThemeState,
    selected: ColorsType,
) {
    PreviewComposition(
        themeState = themeState,
    ) {
        SettingsColorsDialog(
            selected = selected,
            onSelect = {
                // noop
            },
            onDismiss = {
                // noop
            }
        )
    }
}

@Preview(name = "ThemeState")
@Composable
private fun SettingsColorsDialogThemeStatePreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
    )
    SettingsColorsDialogPreview(
        themeState = themeState,
        selected = colorsType,
    )
}
