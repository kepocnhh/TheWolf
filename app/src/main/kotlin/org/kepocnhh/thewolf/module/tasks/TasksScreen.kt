package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.entity.YMD

@Composable
private fun Text(text: String) {
    val textStyle = TextStyle(
        color = App.Theme.colors.text,
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
private fun TaskItem(item: Task) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(App.Theme.colors.secondary, RoundedCornerShape(32.dp))
            .fillMaxWidth()
            .height(64.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {
            BasicText(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                text = item.title,
                style = TextStyle(
                    color = App.Theme.colors.text,
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                ),
                overflow = TextOverflow.Ellipsis,
                minLines = 1,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun DateItem(item: YMD) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 16.dp),
    ) {
        BasicText(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            text = String.format("%02d.%02d.%02d", item.year, item.month + 1, item.day),
            style = TextStyle(
                color = App.Theme.colors.text,
                textAlign = TextAlign.Start,
                fontSize = 15.sp,
            ),
        )
    }
}

@Composable
private fun TasksScreen(state: TasksLogics.State) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val insets = App.Theme.insets
        val verticalPadding = 16.dp
        val contentPadding = PaddingValues(
            top = insets.calculateTopPadding() + verticalPadding,
            bottom = insets.calculateBottomPadding() + verticalPadding,
        )
        val itemsPadding = 16.dp
        val itemsAlign = Alignment.CenterVertically
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(itemsPadding, itemsAlign),
        ) {
            val dates = state.groups.keys
            for (date in dates) {
                item(
                    key = date.toString(),
                ) {
                    DateItem(item = date)
                }
                items(
                    items = state.groups[date].orEmpty(),
                    key = { it.id },
                ) { task ->
                    TaskItem(item = task)
                }
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
