package org.kepocnhh.thewolf.module.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.R
import org.kepocnhh.thewolf.module.app.ColorsType

private fun getText(colorsType: ColorsType): String {
    return colorsType.name // todo
}

@DrawableRes
@Composable
private fun getIcon(colorsType: ColorsType): Int {
    return when (colorsType) {
        ColorsType.Auto -> if (isSystemInDarkTheme()) R.drawable.moon else R.drawable.sun
        ColorsType.Dark -> R.drawable.moon
        ColorsType.Light -> R.drawable.sun
    }
}

@Composable
internal fun SettingsColors(
    modifier: Modifier,
    colorsType: ColorsType,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(App.Theme.colors.secondary, RoundedCornerShape(16.dp))
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
            ) {
                BasicText(
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = App.Theme.colors.text,
                    ),
                    text = "Colors", // todo
                )
                BasicText(
                    modifier = Modifier.align(Alignment.BottomStart),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = App.Theme.colors.text,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    ),
                    text = getText(colorsType),
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = getIcon(colorsType)),
                    contentDescription = "colors:icon",
                    colorFilter = ColorFilter.tint(App.Theme.colors.foreground),
                )
            }
        }
    }
}
