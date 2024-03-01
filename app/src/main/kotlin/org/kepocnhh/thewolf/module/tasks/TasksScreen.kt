package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.kepocnhh.thewolf.App

@Composable
private fun Text(text: String) {
    val textStyle = TextStyle(
        color = App.Theme.colors.foreground,
        textAlign = TextAlign.Start,
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
private fun TasksScreen(state: TasksLogics.State) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val insets = App.Theme.insets
        val contentPadding = PaddingValues(
            top = insets.calculateTopPadding(),
            bottom = insets.calculateBottomPadding(),
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = contentPadding,
        ) {
            items(
                items = state.tasks,
                key = { it.id },
            ) { task ->
                Text(text = task.title)
            }
        }
    }
}

@Composable
internal fun TasksScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(App.Theme.colors.background),
    ) {
        val logics = App.logics<TasksLogics>()
        val state = logics.state.collectAsState().value
        LaunchedEffect(Unit) {
            if (state == null) logics.requestState()
        }
        if (state != null) {
            TasksScreen(state)
        }
    }
}
