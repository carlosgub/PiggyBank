package com.carlosgub.myfinances.components.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.carlosgub.myfinances.core.utils.toDayString
import com.carlosgub.myfinances.core.utils.toMonthString
import com.carlosgub.myfinances.theme.ColorPrimary
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.Fill
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDateTime

@Composable
fun FinanceLineChart(
    daySpent: ImmutableMap<LocalDateTime, Long>,
    withYChart: Boolean,
    modifier: Modifier = Modifier,
    lineColor: Color = ColorPrimary,
    contentColor: Color = Color.Black,
) {
    val days = remember(daySpent) { daySpent.keys.toList() }
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(daySpent) {
        modelProducer.runTransaction {
            lineModel { series(y = daySpent.values.map { it / 100.0 }) }
        }
    }

    val xFormatter = remember(days) {
        CartesianValueFormatter { _, value, _ ->
            days.getOrNull(value.toInt())
                ?.let { day -> "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}" }
                .orEmpty()
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
                        ),
                    ),
                ),
                startAxis = if (withYChart) {
                    VerticalAxis.rememberStart(
                        valueFormatter = CartesianValueFormatter.decimal(prefix = "$"),
                        label = rememberAxisLabelComponent(style = TextStyle(color = contentColor)),
                    )
                } else {
                    null
                },
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = xFormatter,
                    label = rememberAxisLabelComponent(style = TextStyle(color = contentColor)),
                ),
            ),
            modelProducer = modelProducer,
            // Fit the whole month into the available width -- this is a small decorative
            // sparkline, not an explorable chart, so scrolling to find the data is the wrong UX.
            scrollState = rememberVicoScrollState(scrollEnabled = false),
            zoomState = rememberVicoZoomState(
                zoomEnabled = false,
                initialZoom = Zoom.Content,
                minZoom = Zoom.Content,
                maxZoom = Zoom.Content,
            ),
        )
    }
}
