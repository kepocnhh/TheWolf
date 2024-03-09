package org.kepocnhh.thewolf.module.app

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class ColorsTypeProvider : PreviewParameterProvider<ColorsType> {
    override val values = sequenceOf(ColorsType.Dark, ColorsType.Light)
}
