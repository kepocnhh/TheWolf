package org.kepocnhh.thewolf.module.app

import androidx.compose.ui.graphics.Color

internal class Colors private constructor(
    val basement: Color,
    val background: Color,
    val secondary: Color,
    val foreground: Color,
    val text: Color,
    val primary: Color,
    val icon: Color,
) {
    companion object {
        val White = Color(0xffffffff)

        val Dark = Colors(
            basement = Color(0xff0f0f0f),
            background = Color(0xff1f1f1f),
            secondary = Color(0xff2f2f2f),
            foreground = Color(0xffefefef),
            text = White,
            primary = Color(0xff1E88E5),
            icon = White,
        )
        val Light = Colors(
            basement = White,
            background = Color(0xffefefef),
            secondary = Color(0xffdfdfdf),
            foreground = Color(0xff1f1f1f),
            text = Color(0xff000000),
            primary = Color(0xff1E88E5),
            icon = Color(0xff000000),
        )
    }
}
