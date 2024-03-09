package org.kepocnhh.thewolf.module.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kepocnhh.thewolf.App

@Composable
internal fun SettingsVersion(
    modifier: Modifier,
    versionName: String,
    versionCode: Int,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
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
                    text = "Version", // todo
                )
                BasicText(
                    modifier = Modifier.align(Alignment.BottomStart),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = App.Theme.colors.text,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    ),
                    text = versionName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            BasicText(
                modifier = Modifier.fillMaxHeight().wrapContentHeight(),
                style = TextStyle(
                    fontSize = 17.sp,
                    color = App.Theme.colors.text,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                ),
                text = versionCode.toString(),
            )
        }
    }
}
