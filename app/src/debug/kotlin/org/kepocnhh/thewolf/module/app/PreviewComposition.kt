package org.kepocnhh.thewolf.module.app

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.provider.Contexts

@Composable
internal fun PreviewComposition(
    contexts: Contexts = Contexts(main = Dispatchers.Main, default = Dispatchers.Default),
    onBackPressedDispatcher: OnBackPressedDispatcher = OnBackPressedDispatcher(),
    themeState: ThemeState,
    stringsMap: Map<String, Strings> = listOf(
        PreviewStrings.En,
        PreviewStrings.Ru,
    ).associateBy { it.language },
    content: @Composable BoxScope.() -> Unit,
) {
    val field = App::class.java.getDeclaredField("_stringsMap")
    field.isAccessible = true
    field.set(App.Companion, stringsMap)
    App.Theme.Composition(
        contexts = contexts,
        onBackPressedDispatcher = onBackPressedDispatcher,
        themeState = themeState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(App.Theme.colors.background),
            content = content,
        )
    }
}
