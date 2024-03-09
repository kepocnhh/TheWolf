package org.kepocnhh.thewolf.module.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ColorsTypeProvider
import org.kepocnhh.thewolf.module.app.PreviewComposition
import org.kepocnhh.thewolf.module.app.ThemeState

@Composable
private fun SettingsScreenPreview(
    themeState: ThemeState,
) {
    PreviewComposition(
        themeState = themeState,
    ) {
        SettingsScreen(
            themeState = themeState,
            onThemeState = {
                // noop
            },
        )
    }
}

@Preview(name = "ThemeState")
@Composable
private fun SettingsScreenThemeStatePreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
    )
    SettingsScreenPreview(
        themeState = themeState,
    )
}
