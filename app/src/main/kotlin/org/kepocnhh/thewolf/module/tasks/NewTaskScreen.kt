package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.util.compose.BackHandler
import org.kepocnhh.thewolf.util.compose.backspace
import org.kepocnhh.thewolf.util.compose.clear
import org.kepocnhh.thewolf.util.compose.plus
import org.kepocnhh.thewolf.util.compose.toPx
import sp.ax.jc.animations.tween.slide.vertical.SlideVVisibility
import sp.ax.jc.keyboard.Keyboard
import kotlin.time.Duration.Companion.seconds

@Composable
private fun TextField(
    value: TextFieldValue,
    maxLength: Int,
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
        if (value.text.isEmpty()) {
            BasicText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp,
                    ),
                text = "My new task",
                style = TextStyle(
                    color = App.Theme.colors.secondary,
                    fontSize = 17.sp,
                ),
            )
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusable()
                .focusRequester(focusRequester)
                .onFocusChanged(onFocusChanged)
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp,
                ),
            value = value,
            onValueChange = {
                if (it.text.length <= maxLength) {
                    onValueChange(it)
                }
            },
            textStyle = TextStyle(
                color = App.Theme.colors.text,
                fontSize = 17.sp,
            ),
            singleLine = true,
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

private fun isReady(title: String): Boolean {
    return title.isNotBlank()
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
        val titleState = remember { mutableStateOf(TextFieldValue()) }
        val isFocusedState = remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        BackHandler {
            if (isFocusedState.value) {
                focusRequester.freeFocus()
            } else {
                onBack()
            }
        }
        LaunchedEffect(Unit) {
            withContext(App.contexts.default) {
                delay(0.5.seconds)
            }
            if (!isFocusedState.value) {
                focusRequester.requestFocus()
            }
        }
        val maxLength = 24
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = insets.calculateTopPadding() + 16.dp,
                    bottom = insets.calculateBottomPadding(),
                )
        ) {
            BasicText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp + 24.dp,
                        bottom = 8.dp,
                    ),
                text = "Title", // todo
                style = TextStyle(
                    color = App.Theme.colors.text,
                    fontSize = 15.sp,
                ),
            )
            TextField(
                value = titleState.value,
                maxLength = maxLength,
                onValueChange = {
                    titleState.value = it
                },
                onFocusChanged = {
                    isFocusedState.value = it.isFocused
                },
                focusRequester = focusRequester,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                val isReady = isReady(title = titleState.value.text)
                BasicText(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(enabled = isReady) {
                            onNewTask(titleState.value.text)
                        }
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        )
                        .wrapContentHeight(),
                    text = "done", // todo
                    style = TextStyle(
                        color = if (isReady) App.Theme.colors.primary else App.Theme.colors.secondary,
                        fontSize = 15.sp,
                    ),
                )
            }
            val bottomPx = insets.calculateBottomPadding().toPx().toInt()
            SlideVVisibility(
                visible = isFocusedState.value,
                initialOffsetY = { it + bottomPx },
                targetOffsetY = { it + bottomPx },
            ) {
                Keyboard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { char ->
                        if (titleState.value.text.length < maxLength) {
                            titleState.value += char
                        }
                    },
                    onBackspace = { isLongClick: Boolean ->
                        if (isLongClick) {
                            titleState.clear()
                        } else {
                            titleState.backspace()
                        }
                    },
                )
            }
        }
    }
}
