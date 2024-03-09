package org.kepocnhh.thewolf.module.tasks

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.R
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.Colors
import org.kepocnhh.thewolf.module.settings.SettingsScreen
import org.kepocnhh.thewolf.util.compose.ColorIndication
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
    onSettings: () -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = insets.calculateBottomPadding())
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomEnd),
        ) {
            CircleButton(
                color = App.Theme.colors.secondary,
                iconColor = App.Theme.colors.icon,
                iconId = R.drawable.gear,
                contentDescription = "TasksScreen:settings",
                onClick = onSettings,
            )
            Spacer(modifier = Modifier.weight(1f))
            CircleButton(
                indication = remember { ColorIndication.create(Colors.White) },
                color = App.Theme.colors.primary,
                iconColor = Colors.White,
                iconId = R.drawable.plus,
                contentDescription = "TasksScreen:new:task",
                onClick = onNewTask,
            )
        }
    }
}

@Composable
private fun CircleButton(
    enabled: Boolean = true,
    indication: Indication = LocalIndication.current,
    elevation: Dp = 8.dp,
    size: Dp = 64.dp,
    color: Color,
    iconSize: Dp = 24.dp,
    iconColor: Color,
    @DrawableRes iconId: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val onClickState = rememberUpdatedState(onClick)
    val elevationTarget = remember { mutableFloatStateOf(elevation.value) }
    val elevationActual = animateFloatAsState(
        targetValue = elevationTarget.floatValue,
        label = "CircleButton:$contentDescription",
    ).value.dp
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = elevationActual,
                shape = RoundedCornerShape(size / 2),
            )
            .background(color, RoundedCornerShape(size / 2))
            .indication(interactionSource, indication)
            .pointerInput(interactionSource, enabled) {
                detectTapGestures(
                    onPress = { offset ->
                        val press = PressInteraction.Press(offset)
                        interactionSource.emit(press)
                        elevationTarget.floatValue = elevation.value / 2
                        if (tryAwaitRelease()) {
                            interactionSource.emit(PressInteraction.Release(press))
                            if (enabled) onClickState.value()
                        } else {
                            interactionSource.emit(PressInteraction.Cancel(press))
                        }
                        elevationTarget.floatValue = elevation.value
                    },
                )
            },
    ) {
        Image(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center),
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(iconColor),
        )
    }
}

private object TasksScreen {
    enum class Menu {
        NEW_TASK,
        SETTINGS,
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
        val menuState = remember { mutableStateOf<TasksScreen.Menu?>(null) }
        if (state != null) {
            TasksScreen(
                state = state,
                onNewTask = {
                    menuState.value = TasksScreen.Menu.NEW_TASK
                },
                onSettings = {
                    menuState.value = TasksScreen.Menu.SETTINGS
                },
            )
            FadeVisibility(
                visible = menuState.value != null,
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)))
            }
            SlideHVisibility(
                visible = menuState.value == TasksScreen.Menu.NEW_TASK,
            ) {
                NewTaskScreen(
                    onBack = {
                        menuState.value = null
                    },
                    onNewTask = { title: String ->
                        logics.addTask(title = title)
                        menuState.value = null
                    },
                )
            }
            SlideHVisibility(
                visible = menuState.value == TasksScreen.Menu.SETTINGS,
            ) {
                SettingsScreen(
                    onBack = {
                        menuState.value = null
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
