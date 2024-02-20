package org.kepocnhh.thewolf.module.app

import org.kepocnhh.thewolf.provider.Contexts
import org.kepocnhh.thewolf.provider.LocalDataProvider

internal data class Injection(
    val contexts: Contexts,
    val locals: LocalDataProvider,
)
