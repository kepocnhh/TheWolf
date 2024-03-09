package org.kepocnhh.thewolf.module.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.BuildConfig
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.module.theme.ThemeLogics
import org.kepocnhh.thewolf.util.compose.BackHandler

@Composable
internal fun SettingsScreen(
    themeState: ThemeState,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
            ) {
                SettingsColors(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    colorsType = themeState.colorsType,
                )
                Spacer(modifier = Modifier.weight(1f)) // todo lang
            }
            // todo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
            ) {
                SettingsVersion(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    versionName = BuildConfig.VERSION_NAME,
                    versionCode = BuildConfig.VERSION_CODE,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun SettingsScreen(
    onBack: () -> Unit,
) {
    BackHandler {
        onBack()
    }
    val themeLogics = App.logics<ThemeLogics>()
    val themeState = themeLogics.state.collectAsState().value
    LaunchedEffect(Unit) {
        if (themeState == null) themeLogics.requestThemeState()
    }
    if (themeState == null) return
    SettingsScreen(
        themeState = themeState,
    )
}
