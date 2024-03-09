package org.kepocnhh.thewolf.module.settings

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ColorsTypeProvider
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.provider.Contexts

@Composable
private fun SettingsColorsDialogPreview(
    themeState: ThemeState,
    selected: ColorsType,
) {
    App.Theme.Composition(
        contexts = Contexts(main = Dispatchers.Main, default = Dispatchers.Default),
        onBackPressedDispatcher = OnBackPressedDispatcher(),
        themeState = themeState,
    ) {
        Box(modifier = Modifier.background(App.Theme.colors.background)) {
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
