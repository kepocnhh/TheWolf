package org.kepocnhh.thewolf.module.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.util.compose.BackHandler

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
        // todo
    }
}
