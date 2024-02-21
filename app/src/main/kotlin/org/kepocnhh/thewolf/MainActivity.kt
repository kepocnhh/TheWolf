package org.kepocnhh.thewolf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.theme.ThemeLogic

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ComposeView(this)
        setContentView(view)
        view.setContent {
//            BackHandler {
//                finish()
//            }
            val themeLogic = App.logic<ThemeLogic>()
            val themeState = themeLogic.state.collectAsState().value
            LaunchedEffect(Unit) {
                if (themeState == null) {
                    themeLogic.requestThemeState()
                }
            }
            if (themeState != null) {
                App.Theme.Composition(
                    onBackPressedDispatcher = onBackPressedDispatcher,
                    themeState = themeState,
                ) {
                    TestScreen()
                }
            }
        }
        /*
        setContent {
            BackHandler {
                finish()
            }
//            val themeViewModel = App.viewModel<ThemeViewModel>()
            val themeLogic = App.logic<ThemeLogic>()
            val themeState = themeLogic.state.collectAsState().value
            LaunchedEffect(Unit) {
                if (themeState == null) {
                    themeLogic.requestThemeState()
                }
            }
            if (themeState != null) {
                App.Theme.Composition(themeState = themeState) {
                    TestScreen()
                }
            }
        }
        */
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

    @Composable
    private fun Text(text: String) {
        val textStyle = TextStyle(
            color = App.Theme.colors.foreground,
            textAlign = TextAlign.Center,
        )
        BasicText(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .wrapContentHeight(),
            text = text,
            style = textStyle,
        )
    }

    @Composable
    private fun TestScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(App.Theme.colors.background),
        ) {
            val state = remember { mutableIntStateOf(0) }
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                Button(
                    text = "foo",
                    onClick = {
                        state.intValue = 1
                    },
                )
                Button(
                    text = "bar",
                    onClick = {
                        state.intValue = 2
                    },
                )
                Text("${BuildConfig.APPLICATION_ID}\n${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}")
            }
            when (state.intValue) {
                1 -> FooScreen(onBack = { state.intValue = 0 })
                2 -> BarScreen(onBack = { state.intValue = 0 })
            }
        }
    }

    @Composable
    private fun FooScreen(
        onBack: () -> Unit,
    ) {
        BackHandler {
            onBack()
        }
//        val themeViewModel = App.viewModel<ThemeViewModel>()
        val themeLogic = App.logic<ThemeLogic>()
        val themeState = themeLogic.state.collectAsState().value ?: TODO()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(App.Theme.colors.background),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                Button(
                    text = "color: ${themeState.colorsType.name}",
                    onClick = {
                        val colorsType = ColorsType.entries.getOrNull(themeState.colorsType.ordinal + 1)
                            ?: ColorsType.entries.firstOrNull()
                            ?: TODO()
                        themeLogic.setThemeState(themeState.copy(colorsType = colorsType))
                    },
                )
            }
        }
    }

    @Composable
    private fun BarScreen(
        onBack: () -> Unit,
    ) {
        BackHandler {
            onBack()
        }
//        val viewModel = App.viewModel<BarViewModel>()
        val logic = App.logic<BarLogic>()
        val clicks = logic.state.collectAsState().value.clicks
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(App.Theme.colors.background),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                Text("clicks: $clicks")
                Button(
                    text = "click me",
                    onClick = {
                        logic.click()
                    },
                )
            }
        }
    }
}
