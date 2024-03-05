package org.kepocnhh.thewolf.util.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

data class SquaresData(
    val color: Color,
    val width: Dp,
    val padding: Dp,
    val radius: Dp,
    val backgroundContext: CoroutineContext,
)

val LocalSquaresData = staticCompositionLocalOf {
    SquaresData(
        color = Color.Black,
        width = 32.dp,
        padding = 16.dp,
        radius = 8.dp,
        backgroundContext = Dispatchers.Default,
    )
}

private fun Float.ct(k: Float): Float {
    return (this % k + k) % k
}

@Composable
fun Squares(
    modifier: Modifier = Modifier,
    color: Color = LocalSquaresData.current.color,
    width: Dp = LocalSquaresData.current.width,
    padding: Dp = LocalSquaresData.current.padding,
    radius: Dp = LocalSquaresData.current.radius,
    backgroundContext: CoroutineContext = LocalSquaresData.current.backgroundContext,
) {
    Box(modifier = modifier) {
        val alphaState = remember { mutableFloatStateOf(1f) }
        val size = DpSize(width = width + padding + width, height = width + padding + width)
        Canvas(
            modifier = Modifier.size(size),
        ) {
            val squareSize = Size(width.toPx(), width.toPx())
            val cornerRadius = CornerRadius(x = radius.toPx(), y = radius.toPx())
            val paddingOffset = Offset(x = padding.toPx(), y = padding.toPx())
            drawRoundRect(
                color = color.copy(alpha = (alphaState.floatValue - 0.00f).ct(1f)),
                topLeft = Offset.Zero,
                size = squareSize,
                cornerRadius = cornerRadius,
            )
            drawRoundRect(
                color = color.copy(alpha = (alphaState.floatValue - 0.25f).ct(1f)),
                topLeft = Offset.Zero.copy(x = squareSize.width + paddingOffset.x),
                size = squareSize,
                cornerRadius = cornerRadius,
            )
            drawRoundRect(
                color = color.copy(alpha = (alphaState.floatValue - 0.75f).ct(1f)),
                topLeft = Offset.Zero.copy(y = squareSize.height + paddingOffset.y),
                size = squareSize,
                cornerRadius = cornerRadius,
            )
            drawRoundRect(
                color = color.copy(alpha = (alphaState.floatValue - 0.50f).ct(1f)),
                topLeft = Offset(
                    x = squareSize.width + paddingOffset.x,
                    y = squareSize.height + paddingOffset.y,
                ),
                size = squareSize,
                cornerRadius = cornerRadius,
            )
        }
        LaunchedEffect(alphaState.floatValue) {
            withContext(backgroundContext) {
                delay(16)
            }
            alphaState.floatValue = (alphaState.floatValue + 0.025f).ct(1f)
        }
    }
}
