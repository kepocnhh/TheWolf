package org.kepocnhh.thewolf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.kepocnhh.thewolf.module.tasks.TasksScreen
import org.kepocnhh.thewolf.module.theme.ThemeLogics
import org.kepocnhh.thewolf.util.compose.BackHandler

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ComposeView(this)
        setContentView(view)
        view.setContent {
            val themeLogic = App.logics<ThemeLogics>()
            val themeState = themeLogic.state.collectAsState().value
            LaunchedEffect(Unit) {
                if (themeState == null) {
                    themeLogic.requestThemeState()
                }
            }
            if (themeState != null) {
                App.Theme.Composition(
                    contexts = App.contexts,
                    onBackPressedDispatcher = onBackPressedDispatcher,
                    themeState = themeState,
                ) {
                    BackHandler {
                        finish()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(App.Theme.colors.background),
                    ) {
                        TasksScreen()
                    }
                }
            }
        }
    }

    @Composable
    private fun Button(text: String, onClick: () -> Unit) {
        val textStyle = TextStyle(
            color = App.Theme.colors.foreground,
            textAlign = TextAlign.Center,
        )
        BasicText(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable(onClick = onClick)
                .wrapContentHeight(),
            text = text,
            style = textStyle,
        )
    }
}
