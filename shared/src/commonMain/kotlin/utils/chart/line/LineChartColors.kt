package utils.chart.line

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import utils.chart.theme.ChartColors

@Immutable
data class LineChartColors(
    val grid: Color,
    val surface: Color,
    val overlayLine: Color,
)

val ChartColors.lineChartColors
    get() = LineChartColors(
        grid = grid,
        surface = surface,
        overlayLine = overlayLine,
    )
