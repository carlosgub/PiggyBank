package utils.views.chart

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosgub.kotlinm.charts.ChartAnimation
import com.carlosgub.kotlinm.charts.bar.BarChart
import com.carlosgub.kotlinm.charts.bar.BarChartCategory
import com.carlosgub.kotlinm.charts.bar.BarChartData
import com.carlosgub.kotlinm.charts.bar.BarChartEntry
import kotlinx.datetime.LocalDateTime
import theme.ColorPrimary
import utils.toDayString
import utils.toMoneyFormat
import utils.toMonthString

@Composable
fun FinanceBarChart(
    daySpent: Map<LocalDateTime, Long>,
    lineColor: Color = ColorPrimary,
    withYChart: Boolean,
    contentColor: Color = Color.Black,
    onOverlayData: (String) -> Unit,
    modifier: Modifier
) {
    val lineData = remember {
        BarChartData(
            categories = listOf(
                BarChartCategory(
                    name = "",
                    entries = daySpent.map { day ->
                        BarChartEntry(
                            x = "${day.key.dayOfMonth.toDayString()}/${day.key.month.toMonthString()}",
                            y = (day.value / 100.0).toFloat(),
                            color = lineColor
                        )
                    }
                )
            )
        )
    }
    BarChart(
        data = lineData,
        modifier = modifier,
        xAxisLabel = {
            XAxisLabel(it, contentColor)
        },
        yAxisLabel = {
            if (withYChart) {
                val text = if (it is Int) {
                    "0"
                } else {
                    (it as Float).toMoneyFormat()
                }
                Text(
                    fontSize = 12.sp,
                    text = text,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(x = 20.dp),
                    color = contentColor
                )
            }
        },
        overlayDataEntryLabel = { date, value ->
            onOverlayData("$date\n${(value as Float).toMoneyFormat()}")
        },
        animation = ChartAnimation.Sequenced()
    )
}

@Composable
private fun XAxisLabel(it: Any, contentColor: Color) {
    Text(
        fontSize = 12.sp,
        text = it as String,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .offset(x = 20.dp),
        color = contentColor
    )
}