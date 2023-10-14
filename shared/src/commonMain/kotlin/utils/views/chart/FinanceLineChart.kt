package utils.views.chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosgub.kotlinm.charts.ChartAnimation
import com.carlosgub.kotlinm.charts.line.LineChart
import com.carlosgub.kotlinm.charts.line.LineChartData
import com.carlosgub.kotlinm.charts.line.LineChartPoint
import com.carlosgub.kotlinm.charts.line.LineChartSeries
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import theme.ColorPrimary
import utils.toDayString
import utils.toLocalDate
import utils.toMoneyFormat
import utils.toMonthString

@Composable
fun FinanceLineChart(
    daySpent: Map<LocalDateTime, Long>,
    lineColor: Color = ColorPrimary,
    withYChart: Boolean,
    contentColor: Color = Color.Black
) {
    val lineData = remember {
        LineChartData(
            series = listOf(
                LineChartSeries(
                    dataName = "Expense",
                    lineColor = lineColor,
                    listOfPoints = daySpent.map { day ->
                        LineChartPoint(
                            x = day.key.toInstant(TimeZone.currentSystemDefault())
                                .toEpochMilliseconds(),
                            y = (day.value / 100.0).toFloat()
                        )
                    }
                )
            )
        )
    }
    if (withYChart) {
        LineChart(
            lineChartData = lineData,
            modifier = Modifier
                .fillMaxWidth(),
            xAxisLabel = {
                XAxisLabel(it, contentColor)
            },
            yAxisLabel = {
                Text(
                    fontSize = 12.sp,
                    text = "${(it as Float).toMoneyFormat()}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(x = 20.dp),
                    color = contentColor
                )
            },
            overlayHeaderLabel = { localDate ->
                OverlayHeaderLabel(localDate as Long, contentColor)
            },
            overlayDataEntryLabel = { _, value ->
                OverlayDataEntryLabel(value, contentColor)
            },
            animation = ChartAnimation.Sequenced()
        )
    } else {
        LineChart(
            lineChartData = lineData,
            modifier = Modifier
                .fillMaxWidth(),
            xAxisLabel = {
                XAxisLabel(it, contentColor)
            },
            overlayHeaderLabel = { localDate ->
                OverlayHeaderLabel(localDate as Long, contentColor)
            },
            overlayDataEntryLabel = { _, value ->
                OverlayDataEntryLabel(value, contentColor)
            },
            animation = ChartAnimation.Sequenced()
        )
    }
}

@Composable
private fun OverlayDataEntryLabel(value: Any, contentColor: Color) {
    Text(
        text = "$value",
        color = contentColor
    )
}

@Composable
private fun OverlayHeaderLabel(
    localDate: Long,
    contentColor: Color
) {
    val day: LocalDate = localDate.toLocalDate()
    Text(
        text = "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}",
        style = MaterialTheme.typography.labelSmall,
        color = contentColor
    )
}

@Composable
private fun XAxisLabel(it: Any, contentColor: Color) {
    val day: LocalDate = (it as Long).toLocalDate()
    Text(
        fontSize = 12.sp,
        text = "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .offset(x = 20.dp),
        color = contentColor
    )
}