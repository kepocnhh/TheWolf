package org.kepocnhh.thewolf.module.app

import androidx.compose.ui.graphics.Color

internal class Colors private constructor(
    val background: Color,
    val foreground: Color,
) {
    companion object {
        val Dark = Colors(
            background = Color(0xff000000),
            foreground = Color(0xffffffff),
        )
    }
}
