package org.kepocnhh.thewolf.util.compose

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

internal operator fun TextFieldValue.plus(char: Char): TextFieldValue {
    val text = StringBuilder(text)
        .replace(selection.start, selection.end, char.toString())
        .toString()
    return copy(
        text = text,
        selection = TextRange(selection.start + 1, selection.start + 1),
    )
}
