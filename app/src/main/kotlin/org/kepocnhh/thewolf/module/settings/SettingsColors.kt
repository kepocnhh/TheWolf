package org.kepocnhh.thewolf.module.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.kepocnhh.thewolf.App
import org.kepocnhh.thewolf.R
import org.kepocnhh.thewolf.module.app.ColorsType
import org.kepocnhh.thewolf.util.compose.ColorIndication

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
private fun SettingsColorRow(
    modifier: Modifier,
    colorsType: ColorsType,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val colors = App.Theme.getColors(colorsType)
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ColorIndication.create(colors.foreground),
                    onClick = onClick,
                )
                .padding(horizontal = 16.dp),
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = getIcon(colorsType)),
                contentDescription = "colors:row:icon",
                colorFilter = ColorFilter.tint(colors.foreground),
            )
            BasicText(
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    color = colors.foreground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                ),
                text = getText(colorsType),
            )
            if (isSelected) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "colors:row:check",
                    colorFilter = ColorFilter.tint(colors.foreground),
                )
            }
        }
    }
}

@Composable
internal fun SettingsColorsDialog(
    selected: ColorsType,
    onSelect: (ColorsType) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(App.Theme.colors.background, RoundedCornerShape(32.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            setOf(
                ColorsType.Light,
                ColorsType.Dark,
                ColorsType.Auto,
            ).forEach { colorsType ->
                SettingsColorRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colorsType = colorsType,
                    isSelected = selected == colorsType,
                    onClick = {
                        onSelect(colorsType)
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Composable
internal fun SettingsColors(
    modifier: Modifier,
    colorsType: ColorsType,
    onSelect: (ColorsType) -> Unit,
) {
    val dialogState = remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(App.Theme.colors.secondary, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    dialogState.value = true
                }
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
    if (dialogState.value) {
        SettingsColorsDialog(
            selected = colorsType,
            onSelect = onSelect,
            onDismiss = {
                dialogState.value = false
            },
        )
    }
}
