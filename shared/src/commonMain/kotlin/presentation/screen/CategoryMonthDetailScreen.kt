@file:OptIn(ExperimentalMaterialApi::class, ExperimentalKoalaPlotApi::class)

package presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.sealed.GenericState
import io.github.koalaplot.core.line.DefaultPoint
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xychart.CategoryAxisModel
import io.github.koalaplot.core.xychart.LinearAxisModel
import io.github.koalaplot.core.xychart.XYChart
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import model.CategoryEnum
import model.CategoryMonthDetailArgs
import model.ExpenseScreenModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.CategoryMonthDetailViewModel
import theme.ColorPrimary
import utils.chart.ChartAnimation
import utils.chart.line.LineChart
import utils.chart.line.LineChartData
import utils.chart.line.LineChartPoint
import utils.chart.line.LineChartSeries
import utils.getCategoryEnumFromName
import utils.toPrecision
import utils.views.Loading
import utils.views.Toolbar
import kotlin.math.ceil

@Composable
fun CategoryMonthDetailScreen(
    viewModel: CategoryMonthDetailViewModel = koinInject(),
    navigator: Navigator,
    args: CategoryMonthDetailArgs
) {
    ExpenseMonthDetailContainer(
        args, navigator, viewModel
    )
}

@Composable
private fun ExpenseMonthDetailContainer(
    args: CategoryMonthDetailArgs,
    navigator: Navigator,
    viewModel: CategoryMonthDetailViewModel
) {
    val categoryEnum = getCategoryEnumFromName(args.category)
    Scaffold(
        topBar = {
            ExpenseMonthDetailToolbar(
                category = categoryEnum.categoryName,
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) { paddingValues ->
        CategoryMonthDetailObserver(viewModel, categoryEnum, args.month)
    }
}

@Composable
private fun CategoryMonthDetailObserver(
    viewModel: CategoryMonthDetailViewModel,
    categoryEnum: CategoryEnum,
    monthKey: String
) {
    when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is GenericState.Success -> {
            CategoryMonthDetailContent(
                header = {
                    CategoryMonthDetailHeader(
                        monthKey,
                        uiState.data.monthAmount,
                        categoryEnum.categoryName,
                        uiState.data.daySpent
                    )
                },
                body = {
                    CategoryMonthDetailBody(
                        uiState.data.expenseScreenModel
                    )
                }
            )
        }

        GenericState.Loading -> {
            CategoryMonthDetailContent(
                header = {
                    Loading()
                },
                body = {
                    Loading()
                }
            )
        }

        GenericState.Initial -> {
            viewModel.getMonthDetail(
                categoryEnum,
                monthKey
            )
        }

        else -> Unit
    }

}

@Composable
fun CategoryMonthDetailContent(
    header: @Composable () -> Unit,
    body: @Composable () -> Unit
) {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.35f)
        ) {
            header()
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.65f)
        ) {
            body()
        }
    }
}

@Composable
fun CategoryMonthDetailBody(
    list: List<ExpenseScreenModel>
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        elevation = 8.dp,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            items(list) {
                Box() {
                    Text(
                        it.note
                    )
                    Column {

                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryMonthDetailHeader(
    monthKey: String,
    monthTotal: Int,
    category: String,
    daySpent: Map<String, Int>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Box(
        ) {

            val lineData = remember {
                LineChartData(
                    series = listOf(
                        LineChartSeries(
                            dataName = "data gastos",
                            lineColor = ColorPrimary,
                            listOfPoints = daySpent.map { point ->
                                val kotlinReleaseDateTime = LocalDateTime(
                                    date = LocalDate(
                                        year = 2023,
                                        month = Month.AUGUST,
                                        dayOfMonth = point.key.substring(0, 2).trimStart('0')
                                            .toInt()
                                    ),
                                    time = LocalTime(0, 0, 0, 0)
                                )
                                LineChartPoint(
                                    x = kotlinReleaseDateTime.toInstant(TimeZone.currentSystemDefault())
                                        .toEpochMilliseconds(),
                                    y = point.value.toFloat(),
                                )
                            }
                        )
                    )
                )
            }
            LineChart(
                lineChartData = lineData,
                modifier = Modifier.fillMaxSize(),
                xAxisLabel = {
                    Text(
                        fontSize = 12.sp,
                        text = "Gaaa",
                        textAlign = TextAlign.Center
                    )
                },
                yAxisLabel = {
                    Text(
                        fontSize = 12.sp,
                        text = if (it == 0) {
                            0.0
                        } else {
                            ((it as Float) / 100.0)
                        }.toPrecision(2),
                        textAlign = TextAlign.Center
                    )
                },
                overlayHeaderLabel = {
                    Text(
                        text = "UWUU",
                        style = MaterialTheme.typography.overline
                    )
                },
                animation = ChartAnimation.Sequenced()
            )
            /*Column {
                Text(
                    text = (monthTotal / 100.0).toMoneyFormat(),
                    style = MaterialTheme.typography.h3,
                    color = Gray900,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }*/
        }
    }
}


@Composable
private fun ExpenseMonthDetailToolbar(
    category: String,
    onBack: () -> Unit
) {
    Toolbar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        title = category,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black
    )
}
