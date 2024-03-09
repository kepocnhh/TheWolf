package org.kepocnhh.thewolf.module.app

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class StringsTypeProvider : PreviewParameterProvider<StringsType> {
    override val values = sequenceOf(StringsType.Locale("en"), StringsType.Locale("ru"))
}
