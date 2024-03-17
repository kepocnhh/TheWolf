package org.kepocnhh.thewolf.util.compose

import androidx.compose.runtime.MutableState
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

internal fun MutableState<TextFieldValue>.clear() {
    value = value.copy(text = "")
}

internal fun TextFieldValue.removeRange(
    startIndex: Int = selection.start,
    endIndex: Int = selection.end,
): TextFieldValue {
    return copy(
        text = text.removeRange(startIndex, endIndex),
        selection = TextRange(startIndex, startIndex),
    )
}

internal fun MutableState<TextFieldValue>.backspace() {
    val selection = value.selection
    if (selection.start == selection.end) {
        if (selection.start > 0) {
            value = value.removeRange(startIndex = selection.start - 1)
        }
    } else {
        value = value.removeRange()
    }
}
