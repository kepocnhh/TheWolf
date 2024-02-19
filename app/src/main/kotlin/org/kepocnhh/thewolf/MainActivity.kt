package org.kepocnhh.thewolf

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import org.kepocnhh.thewolf.module.theme.ThemeViewModel

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BackHandler {
                finish()
            }
            val themeViewModel = App.viewModel<ThemeViewModel>()
            val themeState = themeViewModel.state.collectAsState().value
            LaunchedEffect(Unit) {
                if (themeState == null) {
                    themeViewModel.requestThemeState()
                }
            }
            if (themeState != null) {
                App.Theme.Composition(themeState = themeState) {
                    TestScreen()
                }
            }
        }
    }

    @Composable
    private fun TestScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(App.Theme.colors.background)
        ) {
            BasicText(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "${BuildConfig.APPLICATION_ID}\n${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}",
                style = TextStyle(color = App.Theme.colors.foreground),
            )
        }
    }
}
