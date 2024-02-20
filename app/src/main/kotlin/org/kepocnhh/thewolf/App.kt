package org.kepocnhh.thewolf

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlinx.coroutines.Dispatchers
import org.kepocnhh.thewolf.module.app.Colors
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.module.app.Injection
import org.kepocnhh.thewolf.module.app.ThemeState
import org.kepocnhh.thewolf.provider.Contexts
import org.kepocnhh.thewolf.provider.LocalDataProvider
import org.kepocnhh.thewolf.util.contains
import org.kepocnhh.thewolf.util.get
import org.kepocnhh.thewolf.util.remove
import org.kepocnhh.thewolf.util.Logic
import org.kepocnhh.thewolf.util.LogicFactory
import org.kepocnhh.thewolf.util.LogicProvider
import org.kepocnhh.thewolf.util.lifecycle.AbstractViewModel
import kotlin.reflect.KClass

internal class App : Application() {
    object Theme {
        private val LocalColors = staticCompositionLocalOf<Colors> { error("No colors!") }

        val colors: Colors
            @Composable
            @ReadOnlyComposable
            get() = LocalColors.current

        @Composable
        fun Composition(
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
        //
        private val _logicProvider = LogicProvider(
            factory = object : LogicFactory {
                override fun <T : Logic> create(type: Class<T>): T {
                    val injection = checkNotNull(_injection) { "No injection!" }
                    return type
                        .getConstructor(Injection::class.java)
                        .newInstance(injection)
                }
            },
        )
        @Composable
        inline fun <reified T : Logic> logic(label: String = T::class.java.name): T {
            val (contains, logic) = synchronized(Logic::class.java) {
                remember { _logicProvider.contains<T>(label = label) } to _logicProvider.get<T>(label = label)
            }
            DisposableEffect(Unit) {
                onDispose {
                    synchronized(Logic::class.java) {
                        if (!contains) {
                            _logicProvider.remove<T>(label = label)
                        }
                    }
                }
            }
            return logic
        }
        //
        private val _viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val injection = checkNotNull(_injection) { "No injection!" }
                return modelClass
                    .getConstructor(Injection::class.java)
                    .newInstance(injection)
            }
        }
        private val vmStores = mutableMapOf<String, ViewModelStore>()

        @Composable
        inline fun <reified T : AbstractViewModel> viewModel(): T {
            val key = T::class.java.name
            val (dispose, store) = synchronized(App::class.java) {
                remember { !vmStores.containsKey(key) } to vmStores.getOrPut(key, ::ViewModelStore)
            }
            DisposableEffect(Unit) {
                onDispose {
                    synchronized(App::class.java) {
                        if (dispose) {
                            vmStores.remove(key)
                            store.clear()
                        }
                    }
                }
            }
            return ViewModelProvider(store, _viewModelFactory)[T::class.java]
        }
    }
}
