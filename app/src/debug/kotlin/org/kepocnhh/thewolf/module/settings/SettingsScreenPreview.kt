package org.kepocnhh.thewolf.module.settings

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

@Preview(name = "foo")
@Composable
private fun SettingsScreenFooPreview() {
    val themeState = ThemeState(
        colorsType = ColorsType.Light
    )
    SettingsScreenPreview(
        themeState = themeState,
    )
}
