package com.carlosgub.myfinances.components.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.carlosgub.myfinances.core.utils.toDayString
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.core.utils.toMonthString
import com.carlosgub.myfinances.theme.ColorPrimary
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerController
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDateTime

@Composable
fun FinanceBarChart(
    daySpent: ImmutableMap<LocalDateTime, Long>,
    withYChart: Boolean,
    modifier: Modifier = Modifier,
    onOverlayData: (String) -> Unit = {},
    barColor: Color = ColorPrimary,
    contentColor: Color = Color.Black,
) {
    val days = remember(daySpent) { daySpent.keys.toList() }
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(daySpent) {
        modelProducer.runTransaction {
            columnModel { series(y = daySpent.values.map { it / 100.0 }) }
        }
    }

    val xFormatter = remember(days) {
        CartesianValueFormatter { _, value, _ ->
            days.getOrNull(value.toInt())
                ?.let { day -> "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}" }
                .orEmpty()
        }
    }

    // A marker with no visual footprint: it only exists to make the tap gesture
    // and visibility listener fire, since Vico's own on-chart label isn't used here --
    // onOverlayData drives this app's own floating overlay instead.
    val marker = remember { object : CartesianMarker {} }

    val visibilityListener = remember(days, onOverlayData) {
        object : CartesianMarkerVisibilityListener {
            private fun notify(targets: List<CartesianMarker.Target>) {
                val target = targets.firstOrNull() as? ColumnCartesianLayerMarkerTarget ?: return
                val column = target.columns.firstOrNull() ?: return
                val day = days.getOrNull(column.entry.x.toInt()) ?: return
                val label = "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}"
                onOverlayData("$label\n${column.entry.y.toFloat().toMoneyFormat()}")
            }

            override fun onShown(marker: CartesianMarker, targets: List<CartesianMarker.Target>) =
                notify(targets)

            override fun onUpdated(marker: CartesianMarker, targets: List<CartesianMarker.Target>) =
                notify(targets)

            override fun onHidden(marker: CartesianMarker) = onOverlayData("")
        }
    }

    Box(modifier = modifier) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(fill = Fill(barColor), thickness = 3.dp),
                    ),
                    columnCollectionSpacing = 1.dp,
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
                marker = marker,
                markerVisibilityListener = visibilityListener,
                markerController = CartesianMarkerController.rememberToggleOnTap(),
            ),
            modelProducer = modelProducer,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
