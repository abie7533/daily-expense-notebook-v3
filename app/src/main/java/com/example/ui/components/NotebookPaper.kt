package com.example.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.notebookPaper(
    lineColor: Color,
    marginColor: Color,
    lineSpacing: Dp = 44.dp,
    marginWidth: Dp = 38.dp,
    enabled: Boolean = true
): Modifier = if (!enabled) this else this.drawBehind {
    val lineSpacingPx = lineSpacing.toPx()
    val marginPx = marginWidth.toPx()
    
    // Draw horizontal paper ruled lines
    var y = lineSpacingPx
    while (y < size.height) {
        drawLine(
            color = lineColor.copy(alpha = 0.5f),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.dp.toPx()
        )
        y += lineSpacingPx
    }
    
    // Draw a double vertical binding margin line (classic notebook style)
    drawLine(
        color = marginColor.copy(alpha = 0.6f),
        start = Offset(marginPx, 0f),
        end = Offset(marginPx, size.height),
        strokeWidth = 1.5.dp.toPx()
    )
    
    drawLine(
        color = marginColor.copy(alpha = 0.3f),
        start = Offset(marginPx + 4.dp.toPx(), 0f),
        end = Offset(marginPx + 4.dp.toPx(), size.height),
        strokeWidth = 0.8.dp.toPx()
    )
}
