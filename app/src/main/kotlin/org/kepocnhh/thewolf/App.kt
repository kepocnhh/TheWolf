package org.kepocnhh.thewolf

import android.app.Application
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.module.app.Colors
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.provider.Contexts
import org.kepocnhh.thewolf.provider.LocalDataProvider
import sp.kx.logics.Logics
import sp.kx.logics.LogicsFactory
import sp.kx.logics.LogicsProvider
import sp.kx.logics.contains
import sp.kx.logics.get
import sp.kx.logics.remove

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("No colors!") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        @Composable
        fun Composition(
            onBackPressedDispatcher: OnBackPressedDispatcher,
            themeState: ThemeState,
            content: @Composable () -> Unit,
        ) {
            val colors = when (themeState.colorsType) {
                ColorsType.AUTO -> if (isSystemInDarkTheme()) Colors.Dark else Colors.Light
                ColorsType.DARK -> Colors.Dark
                ColorsType.LIGHT -> Colors.Light
            }
            CompositionLocalProvider(
                LocalColors provides colors,
                LocalOnBackPressedDispatcher provides onBackPressedDispatcher,
                content = content,
            )
        }
    }

    // todo
    private class MockLocalDataProvider(
        override var themeState: ThemeState,
    ) : LocalDataProvider

    override fun onCreate() {
        super.onCreate()
        _injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                default = Dispatchers.Default,
            ),
            locals = MockLocalDataProvider(themeState = ThemeState(colorsType = ColorsType.AUTO))
        )
    }

    companion object {
        private var _injection: Injection? = null
        private val _logicsProvider = LogicsProvider(
            factory = object : LogicsFactory {
                override fun <T : Logics> create(type: Class<T>): T {
                    val injection = checkNotNull(_injection) { "No injection!" }
                    return type
                        .getConstructor(Injection::class.java)
                        .newInstance(injection)
                }
            },
        )
        @Composable
        inline fun <reified T : Logics> logics(label: String = T::class.java.name): T {
            val (contains, logic) = synchronized(App::class.java) {
                remember { _logicsProvider.contains<T>(label = label) } to _logicsProvider.get<T>(label = label)
            }
            DisposableEffect(Unit) {
                onDispose {
                    synchronized(App::class.java) {
                        if (!contains) {
                            _logicsProvider.remove<T>(label = label)
                        }
                    }
                }
            }
            return logic
        }
    }
}
