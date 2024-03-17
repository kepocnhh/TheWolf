package org.kepocnhh.thewolf.module.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ColorsTypeProvider
import org.kepocnhh.thewolf.module.app.PreviewComposition
import org.kepocnhh.thewolf.module.app.StringsType
import org.kepocnhh.thewolf.module.app.StringsTypeProvider
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

@Preview(name = "ColorsType")
@Composable
private fun SettingsScreenColorsTypePreview(
    @PreviewParameter(ColorsTypeProvider::class) colorsType: ColorsType,
) {
    val themeState = ThemeState(
        colorsType = colorsType,
        stringsType = StringsType.Auto,
    )
    SettingsScreenPreview(
        themeState = themeState,
    )
}

@Preview(name = "StringsType")
@Composable
private fun SettingsScreenStringsTypePreview(
    @PreviewParameter(StringsTypeProvider::class) stringsType: StringsType,
) {
    val themeState = ThemeState(
        colorsType = ColorsType.Dark,
        stringsType = stringsType,
    )
    SettingsScreenPreview(
        themeState = themeState,
    )
}
