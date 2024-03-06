package org.kepocnhh.thewolf

import android.app.Application
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.Colors
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.provider.Contexts
import org.kepocnhh.thewolf.provider.LocalDataProvider
import org.kepocnhh.thewolf.util.compose.LocalOnBackPressedDispatcher
import org.kepocnhh.thewolf.util.compose.toPaddings
import sp.ax.jc.squares.LocalSquaresStyle
import sp.ax.jc.squares.SquaresStyle
import sp.kx.logics.Logics
import sp.kx.logics.LogicsFactory
import sp.kx.logics.LogicsProvider
import sp.kx.logics.contains
import sp.kx.logics.get
import sp.kx.logics.remove
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("No colors!") }
        private val LocalInsets = staticCompositionLocalOf<PaddingValues> { error("No insets!") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        val insets: PaddingValues
            @Composable
            @ReadOnlyComposable
            get() = LocalInsets.current

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
            val insets = LocalView.current.rootWindowInsets.toPaddings()
            val injection = checkNotNull(_injection) { "No injection!" }
            CompositionLocalProvider(
                LocalColors provides colors,
                LocalInsets provides insets,
                LocalSquaresStyle provides SquaresStyle(
                    color = colors.foreground,
                    squareSize = DpSize(width = 32.dp, height = 32.dp),
                    paddingOffset = DpOffset(x = 16.dp, y = 16.dp),
                    cornerRadius = 8.dp,
                    backgroundContext = injection.contexts.default,
                ),
                LocalOnBackPressedDispatcher provides onBackPressedDispatcher,
                content = content,
            )
        }
    }

    // todo
    private class MockLocalDataProvider(
        override var themeState: ThemeState,
        override var tasks: List<Task>,
    ) : LocalDataProvider

    override fun onCreate() {
        super.onCreate()
        _injection = Injection(
            contexts = Contexts(
                main = Dispatchers.Main,
                default = Dispatchers.Default,
            ),
            locals = MockLocalDataProvider(
                themeState = ThemeState(colorsType = ColorsType.AUTO),
                tasks = (1..30).map { index ->
                    Task(
                        id = UUID.randomUUID(),
                        title = "task #$index",
                        isChecked = true,
                        date = System.currentTimeMillis().milliseconds - 48.hours + (index * 6).hours,
                    )
                },
            )
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
