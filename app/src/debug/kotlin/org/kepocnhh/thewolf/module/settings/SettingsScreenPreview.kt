package org.kepocnhh.thewolf.module.settings

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.provider.Contexts

@Composable
private fun SettingsScreenPreview(
    themeState: ThemeState,
) {
    App.Theme.Composition(
        contexts = Contexts(main = Dispatchers.Main, default = Dispatchers.Default),
        onBackPressedDispatcher = OnBackPressedDispatcher(),
        themeState = themeState,
    ) {
        Box(modifier = Modifier.background(App.Theme.colors.background)) {
            SettingsScreen(
                onBack = {
                    // noop
                },
            )
        }
    }
}

private class ColorsTypeProvider : PreviewParameterProvider<ColorsType> {
    override val values = sequenceOf(ColorsType.Dark, ColorsType.Light)
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
