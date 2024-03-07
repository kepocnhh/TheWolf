package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.util.compose.BackHandler
import sp.ax.jc.keyboard.Keyboard

@Composable
private fun TextField(text: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(App.Theme.colors.secondary, RoundedCornerShape(32.dp))
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
        BackHandler {
            onBack()
        }
        val insets = App.Theme.insets
        val titleState = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = insets.calculateTopPadding() + 16.dp,
                    bottom = insets.calculateBottomPadding() + 4.dp,
                )
        ) {
            TextField(text = titleState.value)
            Spacer(modifier = Modifier.weight(1f))
            Keyboard(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { char ->
                    titleState.value = titleState.value + char
                },
            )
        }
    }
}
