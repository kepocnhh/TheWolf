package org.kepocnhh.thewolf.util.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kepocnhh.thewolf.module.app.Injection

internal open class AbstractViewModel : ViewModel() {
    protected fun Injection.launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(contexts.main, block = block)
    }
}
