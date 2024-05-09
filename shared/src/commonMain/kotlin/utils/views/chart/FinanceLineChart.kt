@file:OptIn(ExperimentalResourceApi::class)

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
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.finance_line_chart_overlay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import theme.ColorPrimary
import utils.createLocalDateTime
import utils.toDayString
import utils.toLocalDate
import utils.toMoneyFormat
import utils.toMonthString

@Composable
fun FinanceLineChart(
    daySpent: ImmutableMap<LocalDateTime, Long>,
    withYChart: Boolean,
    modifier: Modifier = Modifier,
    lineColor: Color = ColorPrimary,
    contentColor: Color = Color.Black
) {
    val lineData =
        remember {
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
            modifier = modifier
                .fillMaxWidth(),
            xAxisLabel = {
                XAxisLabel(it, contentColor)
            },
            yAxisLabel = {
                Text(
                    fontSize = 12.sp,
                    text = (it as Float).toMoneyFormat(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(x = 20.dp),
                    color = contentColor
                )
            },
            overlayHeaderLabel = { localDate ->
                OverlayHeaderLabel(
                    localDate = localDate as Long,
                    contentColor = contentColor,
                    daySpent = daySpent
                )
            },
            overlayDataEntryLabel = { _, _ ->
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
                OverlayHeaderLabel(
                    localDate = localDate as Long,
                    contentColor = contentColor,
                    daySpent = daySpent
                )
            },
            overlayDataEntryLabel = { _, _ ->
            },
            animation = ChartAnimation.Sequenced()
        )
    }
}

@Composable
private fun OverlayHeaderLabel(
    localDate: Long,
    contentColor: Color,
    daySpent: ImmutableMap<LocalDateTime, Long>
) {
    val day: LocalDate = localDate.toLocalDate()
    val localDateTime =
        createLocalDateTime(
            year = day.year,
            monthNumber = day.monthNumber,
            dayOfMonth = day.dayOfMonth
        )
    val moneySpent = ((daySpent[localDateTime] ?: 0) / 100.0).toFloat().toMoneyFormat()
    Text(
        text = stringResource(
            Res.string.finance_line_chart_overlay,
            day.dayOfMonth,
            day.month,
            moneySpent
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = contentColor
    )
}

@Composable
private fun XAxisLabel(
    it: Any,
    contentColor: Color
) {
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
