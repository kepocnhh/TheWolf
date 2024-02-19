package org.kepocnhh.thewolf

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

internal class App : Application() {
    object Theme {
        @Composable
        fun Composition(
            content: @Composable () -> Unit,
        ) {
            // todo
            CompositionLocalProvider(
                content = content,
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        // todo
    }
}
