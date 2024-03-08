package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import org.kepocnhh.thewolf.util.compose.plus
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
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                delay(1.seconds)
            }
            if (!isFocusedState.value) {
                focusRequester.requestFocus()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = insets.calculateTopPadding() + 16.dp,
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
            val maxLength = 24
            TextField(
                value = textFieldValueState.value,
                maxLength = maxLength,
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
                        if (textFieldValueState.value.text.length < maxLength) {
                            textFieldValueState.value += char
                        }
                    },
                )
            }
        }
    }
}
