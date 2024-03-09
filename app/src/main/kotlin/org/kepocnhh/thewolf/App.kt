package org.kepocnhh.thewolf

import android.app.Application
import android.content.res.Configuration
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.entity.Task
import org.kepocnhh.thewolf.module.app.Colors
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.Strings
import org.kepocnhh.thewolf.module.app.StringsType
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.provider.Contexts
import org.kepocnhh.thewolf.provider.LocalDataProvider
import org.kepocnhh.thewolf.util.compose.LocalOnBackPressedDispatcher
import org.kepocnhh.thewolf.util.compose.toPaddings
import sp.ax.jc.animations.style.LocalTweenStyle
import sp.ax.jc.animations.style.TweenStyle
import sp.ax.jc.keyboard.KeyboardColors
import sp.ax.jc.keyboard.KeyboardStyle
import sp.ax.jc.keyboard.LocalKeyboardStyle
import sp.ax.jc.squares.LocalSquaresStyle
import sp.ax.jc.squares.SquaresStyle
import sp.kx.logics.Logics
import sp.kx.logics.LogicsFactory
import sp.kx.logics.LogicsProvider
import sp.kx.logics.contains
import sp.kx.logics.get
import sp.kx.logics.remove
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

internal class App : Application() {
    object Theme {
        private val LocalInsets = staticCompositionLocalOf<PaddingValues> { error("No insets!") }
        private val LocalColors = staticCompositionLocalOf<Colors> { error("No colors!") }
        private val LocalStrings = staticCompositionLocalOf<Strings> { error("No strings!") }

        val insets: PaddingValues
            @Composable
            @ReadOnlyComposable
            get() = LocalInsets.current

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        val strings: Strings
            @Composable
            @ReadOnlyComposable
            get() = LocalStrings.current

        @Composable
        fun getColors(colorsType: ColorsType): Colors {
            return when (colorsType) {
                ColorsType.Auto -> if (isSystemInDarkTheme()) Colors.Dark else Colors.Light
                ColorsType.Dark -> Colors.Dark
                ColorsType.Light -> Colors.Light
            }
        }

        @Composable
        fun getStrings(stringsType: StringsType): Strings {
            return getStrings(
                configuration = LocalConfiguration.current,
                map = getStrings(),
                stringsType = stringsType,
            )
        }

        private fun getStrings(): Map<String, Strings> {
            return mapOf(
                "ru" to Strings(
                    auto = "Автоматически",
                    language = "ru",
                    languageName = "Русский",
                    settingsLanguage = "Язык",
                ),
                "en" to Strings(
                    auto = "Auto",
                    language = "en",
                    languageName = "English",
                    settingsLanguage = "Language",
                ),
            ) // todo
        }

        private fun getDefaultStrings(map: Map<String, Strings>): Strings {
            return map["en"] ?: error("No default strings!")
        }

        private fun getStrings(
            configuration: Configuration,
            map: Map<String, Strings>,
            stringsType: StringsType,
        ): Strings {
            return when (stringsType) {
                StringsType.Auto -> {
                    configuration
                        .locales
                        .get(0)
                        ?.language
                        ?.let(map::get)
                        ?: getDefaultStrings(map)
                }
                is StringsType.Locale -> {
                    map[stringsType.language] ?: error("No strings by \"${stringsType.language}\"!")
                }
            }
        }

        @Composable
        fun Composition(
            contexts: Contexts,
            onBackPressedDispatcher: OnBackPressedDispatcher,
            themeState: ThemeState,
            content: @Composable () -> Unit,
        ) {
            val insets = LocalView.current.rootWindowInsets.toPaddings()
            val colors = getColors(themeState.colorsType)
            val map = getStrings()
            _locales = map.keys
            val strings = getStrings(
                configuration = LocalConfiguration.current,
                map = map,
                stringsType = themeState.stringsType,
            )
            CompositionLocalProvider(
                LocalTextInputService provides null,
                LocalOnBackPressedDispatcher provides onBackPressedDispatcher,
                LocalInsets provides insets,
                LocalColors provides colors,
                LocalStrings provides strings,
                LocalSquaresStyle provides SquaresStyle(
                    color = colors.foreground,
                    squareSize = DpSize(width = 32.dp, height = 32.dp),
                    paddingOffset = DpOffset(x = 16.dp, y = 16.dp),
                    cornerRadius = 8.dp,
                    backgroundContext = contexts.default,
                ),
                LocalTweenStyle provides LocalTweenStyle.current.copy(
                    duration = 0.5.seconds,
                ),
                LocalKeyboardStyle provides KeyboardStyle(
                    fontSize = 15.sp,
                    colors = KeyboardColors(
                        text = colors.text,
                        pressed = colors.text.copy(alpha = 0.25f),
                    ),
                    corners = 16.dp,
                    backspaceIconId = R.drawable.backspace,
                ),
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
                themeState = ThemeState(
                    colorsType = ColorsType.Auto,
                    stringsType = StringsType.Auto,
                ),
//                tasks = (1..30).map { index ->
//                    Task(
//                        id = UUID.randomUUID(),
//                        title = "task #$index",
//                        isChecked = true,
//                        date = System.currentTimeMillis().milliseconds - 48.hours + (index * 6).hours,
//                    )
//                },
                tasks = (1..4).map { number ->
                    Task(
                        id = UUID.randomUUID(),
                        title = "task #$number",
                        created = System.currentTimeMillis().milliseconds + number.hours,
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

        val contexts: Contexts
            get() = checkNotNull(_injection) { "No injection!" }.contexts

        private var _locales: Set<String>? = null
        val locales: Set<String>
            get() = checkNotNull(_locales) { "No locales!" }

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
