package utils.chart.bar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import utils.chart.theme.ChartTheme
import utils.chart.ChartAnimation
import utils.chart.grid.GridDefaults
import utils.chart.line.ChartLegend

/**
 * This bar chart shows data organised in categories together with a legend.
 *
 * @param legendItemLabel Composable to show for each item in the legend. Square representing the
 * color of the item, drawn to the left of it, is not customizable.
 *
 * @see BarChart
 */
@Composable
fun BarChartWithLegend(
    data: BarChartData,
    modifier: Modifier = Modifier,
    animation: ChartAnimation = ChartAnimation.Simple(),
    colors: BarChartColors = ChartTheme.colors.barChartColors,
    config: BarChartConfig = BarChartConfig(),
    xAxisLabel: @Composable (value: Any) -> Unit = GridDefaults.XAxisLabel,
    yAxisLabel: @Composable (value: Any) -> Unit = GridDefaults.YAxisLabel,
    legendItemLabel: @Composable (String) -> Unit = GridDefaults.LegendItemLabel,
) {
    Column(modifier) {
        BarChart(
            modifier = Modifier.weight(1f),
            data = data,
            animation = animation,
            colors = colors,
            config = config,
            xAxisLabel = xAxisLabel,
            yAxisLabel = yAxisLabel,
        )
        ChartLegend(
            legendData = data.legendData,
            animation = animation,
            config = config,
            legendItemLabel = legendItemLabel,
        )
    }
}
