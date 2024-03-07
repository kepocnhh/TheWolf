package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.entity.Task
import sp.ax.jc.animations.tween.fade.FadeVisibility
import sp.ax.jc.animations.tween.slide.SlideHVisibility
import sp.ax.jc.squares.Squares
import java.util.Calendar

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
            val calendar = remember { Calendar.getInstance() }
            calendar.timeInMillis = item.created.inWholeMilliseconds
            val dateText = "${calendar[Calendar.YEAR]}.${calendar[Calendar.MONTH] + 1}.${calendar[Calendar.DAY_OF_MONTH]}"
            val timeText = "${calendar[Calendar.HOUR]}:${calendar[Calendar.MINUTE]}:${calendar[Calendar.SECOND]}"
            BasicText(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                text = item.title + " $dateText/$timeText", // todo date/time
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
private fun TasksScreen(
    state: TasksLogics.State,
    onNewTask: () -> Unit,
) {
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
            items(
                items = state.tasks,
                key = Task::id,
            ) { task ->
                TaskItem(item = task)
            }
        }
        BasicText(
            modifier = Modifier
                .padding(
                    bottom = insets.calculateBottomPadding() + 8.dp,
                    end = insets.calculateEndPadding(LayoutDirection.Ltr) + 8.dp
                )
                .background(Color.Black)
                .align(Alignment.BottomEnd)
                .clickable(onClick = onNewTask)
                .padding(8.dp),
            text = "new task",
            style = TextStyle(
                color = Color.White,
            ),
        ) // todo
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
        val newTaskState = remember { mutableStateOf(false) }
        if (state != null) {
            TasksScreen(
                state = state,
                onNewTask = {
                    newTaskState.value = true
                },
            )
            FadeVisibility(
                visible = newTaskState.value,
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.75f)))
            }
            SlideHVisibility(
                visible = newTaskState.value,
            ) {
                NewTaskScreen(
                    onBack = {
                        newTaskState.value = false
                    },
                    onNewTask = { title: String ->
                        logics.addTask(title = title)
                        newTaskState.value = false
                    },
                )
            }
        }
        FadeVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            visible = state == null,
        ) {
            Squares()
        }
    }
}
