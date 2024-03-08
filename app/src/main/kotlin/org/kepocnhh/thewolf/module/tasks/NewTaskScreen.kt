package org.kepocnhh.thewolf.module.tasks

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.util.compose.BackHandler
import org.kepocnhh.thewolf.util.compose.plus
import sp.ax.jc.animations.style.LocalTweenStyle
import sp.ax.jc.animations.tween.fade.FadeVisibility
import sp.ax.jc.animations.tween.slide.SlideHVisibility
import sp.ax.jc.animations.tween.slide.SlideVisibility
import sp.ax.jc.keyboard.Keyboard
import java.lang.StringBuilder
import kotlin.time.Duration

@Composable
private fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    focusRequester: FocusRequester,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(App.Theme.colors.basement, RoundedCornerShape(32.dp))
            .fillMaxWidth(),
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .focusable()
                .onFocusChanged(onFocusChanged)
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp,
                ),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = App.Theme.colors.text,
                fontSize = 17.sp,
            ),
        )
    }
}

@Composable
private fun TextField(text: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(App.Theme.colors.basement, RoundedCornerShape(32.dp))
            .fillMaxWidth(),
    ) {
        BasicText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp,
                ),
            text = text,
            style = TextStyle(
                color = App.Theme.colors.text,
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
            ),
        )
    }
}

@Composable
private fun SlideVVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    label: String = "SlideHVisibility",
    duration: Duration = LocalTweenStyle.current.duration,
    delay: Duration = LocalTweenStyle.current.delay,
    easing: Easing = LocalTweenStyle.current.easing,
    initialOffsetY: (fullHeight: Int) -> Int = { it },
    targetOffsetY: (fullHeight: Int) -> Int = { it },
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    SlideVisibility(
        visible = visible,
        modifier = modifier,
        label = label,
        duration = duration,
        delay = delay,
        easing = easing,
        initialOffset = { IntOffset(x = 0, y = initialOffsetY(it.height)) },
        targetOffset = { IntOffset(x = 0, y = targetOffsetY(it.height)) },
        content = content,
    )
}

@Composable
internal fun NewTaskScreen(
    onBack: () -> Unit,
    onNewTask: (title: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        val insets = App.Theme.insets
        val textFieldValueState = remember { mutableStateOf(TextFieldValue()) }
        val isFocusedState = remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        BackHandler {
            if (isFocusedState.value) {
                focusRequester.freeFocus()
            } else {
                onBack()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = insets.calculateTopPadding() + 16.dp,
                )
        ) {
            TextField(
                value = textFieldValueState.value,
                onValueChange = {
                    textFieldValueState.value = it
                },
                onFocusChanged = {
                    isFocusedState.value = it.isFocused
                },
                focusRequester = focusRequester,
            )
            Spacer(modifier = Modifier.weight(1f))
            SlideVVisibility(visible = isFocusedState.value) {
                Keyboard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = insets.calculateBottomPadding() + 4.dp,
                        ),
                    onClick = { char ->
                        textFieldValueState.value += char
                    },
                )
            }
//            FadeVisibility(visible = isFocusedState.value) {
//                Keyboard(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    onClick = { char ->
//                        textFieldValueState.value += char
//                    },
//                )
//            }
        }
    }
}
