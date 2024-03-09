package org.kepocnhh.thewolf.module.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import org.kepocnhh.thewolf.module.app.StringsType

@Composable
private fun getText(stringsType: StringsType): String {
    val strings = App.Theme.getStrings(stringsType)
    return when (stringsType) {
        StringsType.Auto -> strings.auto
        is StringsType.Locale -> strings.languageName
    }
}

@Composable
private fun SettingsStringsRow(
    modifier: Modifier,
    stringsType: StringsType,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
        ) {
            // todo icon
            BasicText(
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    color = App.Theme.colors.foreground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                ),
                text = getText(stringsType),
            )
            if (isSelected) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = "strings:row:check",
                    colorFilter = ColorFilter.tint(App.Theme.colors.foreground),
                )
            }
        }
    }
}

@Composable
internal fun SettingsStringsDialog(
    selected: StringsType,
    list: List<StringsType>,
    onSelect: (StringsType) -> Unit,
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
            list.forEach { stringsType ->
                SettingsStringsRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    stringsType = stringsType,
                    isSelected = selected == stringsType,
                    onClick = {
                        onSelect(stringsType)
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Composable
internal fun SettingsStrings(
    modifier: Modifier,
    stringsType: StringsType,
    onSelect: (StringsType) -> Unit,
) {
    val dialogState = remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
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
                    text = App.Theme.strings.settingsLanguage,
                )
                BasicText(
                    modifier = Modifier.align(Alignment.BottomStart),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = App.Theme.colors.text,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    ),
                    text = getText(stringsType),
                )
            }
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight(),
//            ) {
//                Image(
//                    modifier = Modifier
//                        .padding(start = 16.dp)
//                        .size(24.dp)
//                        .align(Alignment.Center),
//                    painter = painterResource(id = getIcon(colorsType)),
//                    contentDescription = "colors:icon",
//                    colorFilter = ColorFilter.tint(App.Theme.colors.foreground),
//                )
//            }
        }
        if (dialogState.value) {
            SettingsStringsDialog(
                selected = stringsType,
                onSelect = onSelect,
                list = App.locales.map(StringsType::Locale) + StringsType.Auto,
                onDismiss = {
                    dialogState.value = false
                },
            )
        }
    }
}
