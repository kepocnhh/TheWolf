package org.kepocnhh.thewolf.module.app

import androidx.compose.ui.graphics.Color

internal class Colors private constructor(
    val basement: Color,
    val background: Color,
    val secondary: Color,
    val foreground: Color,
    val text: Color,
) {
    companion object {
        val Dark = Colors(
            basement = Color(0xff0f0f0f),
            background = Color(0xff1f1f1f),
            secondary = Color(0xff2f2f2f),
            foreground = Color(0xffefefef),
            text = Color(0xffffffff),
        )
        val Light = Colors(
            basement = Color(0xffffffff),
            background = Color(0xffefefef),
            secondary = Color(0xffdfdfdf),
            foreground = Color(0xff1f1f1f),
            text = Color(0xff000000),
        )
    }
}
