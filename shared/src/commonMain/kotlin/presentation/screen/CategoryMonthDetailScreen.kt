@file:OptIn(ExperimentalFoundationApi::class)

package presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosgub.kotlinm.charts.ChartAnimation
import com.carlosgub.kotlinm.charts.line.LineChart
import com.carlosgub.kotlinm.charts.line.LineChartData
import com.carlosgub.kotlinm.charts.line.LineChartPoint
import com.carlosgub.kotlinm.charts.line.LineChartSeries
import core.sealed.GenericState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import model.CategoryEnum
import model.CategoryMonthDetailArgs
import model.EditArgs
import model.ExpenseScreenModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.CategoryMonthDetailViewModel
import theme.ColorPrimary
import theme.Gray600
import theme.Gray900
import theme.White
import utils.getCategoryEnumFromName
import utils.toDayString
import utils.toLocalDate
import utils.toMoneyFormat
import utils.toMonthString
import utils.views.ExpenseDivider
import utils.views.Loading
import utils.views.Toolbar

@Composable
fun CategoryMonthDetailScreen(
    navigator: Navigator,
    args: CategoryMonthDetailArgs
) {
    val viewModel = koinViewModel(CategoryMonthDetailViewModel::class)
    ExpenseMonthDetailContainer(
        args = args,
        navigator = navigator,
        viewModel = viewModel
    )
}

@Composable
private fun ExpenseMonthDetailContainer(
    args: CategoryMonthDetailArgs,
    navigator: Navigator,
    viewModel: CategoryMonthDetailViewModel
) {
    val updateBackScreen = rememberSaveable { mutableStateOf(false) }
    val categoryEnum = getCategoryEnumFromName(args.category)
    Scaffold(
        topBar = {
            ExpenseMonthDetailToolbar(
                category = categoryEnum.categoryName,
                onBack = {
                    navigator.goBackWith(updateBackScreen.value)
                }
            )
        }
    ) { paddingValues ->
        CategoryMonthDetailObserver(
            viewModel = viewModel,
            categoryEnum = categoryEnum,
            monthKey = args.month,
            paddingValues = paddingValues,
            navigator = navigator,
            updateBackScreen = {
                updateBackScreen.value = true
            }
        )
    }
}

@Composable
private fun CategoryMonthDetailObserver(
    viewModel: CategoryMonthDetailViewModel,
    categoryEnum: CategoryEnum,
    monthKey: String,
    paddingValues: PaddingValues,
    navigator: Navigator,
    updateBackScreen: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is GenericState.Success -> {
            CategoryMonthDetailContent(
                paddingValues,
                header = {
                    CategoryMonthDetailHeader(
                        uiState.data.monthAmount,
                        uiState.data.daySpent,
                        categoryEnum.categoryName
                    )
                },
                body = {
                    CategoryMonthDetailBody(
                        uiState.data.expenseScreenModel
                    ) { expenseScreenModel ->
                        coroutine.launch {
                            val result = navigator.navigateForResult(
                                Screen.EditScreen.createRoute(
                                    EditArgs(
                                        financeEnum = getCategoryEnumFromName(expenseScreenModel.category).type,
                                        expenseScreenModel = expenseScreenModel
                                    )
                                )
                            )
                            if (result as Boolean) {
                                updateBackScreen()
                                viewModel.getMonthDetail(
                                    categoryEnum,
                                    monthKey
                                )
                            }
                        }
                    }
                }
            )
        }

        GenericState.Loading -> {
            CategoryMonthDetailContent(
                paddingValues,
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
    paddingValues: PaddingValues,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(White)
            .padding(
                top = paddingValues.calculateTopPadding()
            )
    ) {
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
    list: List<ExpenseScreenModel>,
    expenseClicked: (ExpenseScreenModel) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        LazyColumn(
            modifier = Modifier
                .background(White)
                .fillMaxSize()
                .padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            itemsIndexed(list) { count, expense ->
                Column {
                    if (count != 0) {
                        ExpenseDivider()
                    }
                    Row(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                },
                                onLongClick = {
                                    expenseClicked(expense)
                                }
                            )
                            .padding(vertical = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        ) {
                            Text(
                                text = expense.note,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = Gray900
                            )
                            Text(
                                text = expense.date,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(top = 4.dp),
                                color = Gray600,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = (expense.amount / 100.0).toMoneyFormat(),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End,
                                color = Gray900
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryMonthDetailHeader(
    monthTotal: Int,
    daySpent: Map<LocalDateTime, Int>,
    category: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Column {
                Spacer(modifier = Modifier.weight(0.2f))
                ChartCategoryMonth(daySpent)
            }
            Column {
                Text(
                    text = (monthTotal / 100.0).toMoneyFormat(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Composable
private fun ChartCategoryMonth(daySpent: Map<LocalDateTime, Int>) {
    val lineData = remember {
        LineChartData(
            series = listOf(
                LineChartSeries(
                    dataName = "Expense",
                    lineColor = ColorPrimary,
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
    LineChart(
        lineChartData = lineData,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        xAxisLabel = {
            val day: LocalDate = (it as Long).toLocalDate()
            Text(
                fontSize = 12.sp,
                text = "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(x = 20.dp)
            )
        },
        overlayHeaderLabel = {
            val day: LocalDate = (it as Long).toLocalDate()
            Text(
                text = "${day.dayOfMonth.toDayString()}/${day.month.toMonthString()}",
                style = MaterialTheme.typography.labelSmall
            )
        },
        overlayDataEntryLabel = { _, value ->
            Text(
                text = "$value"
            )
        },
        animation = ChartAnimation.Sequenced()
    )
}

@Composable
private fun ExpenseMonthDetailToolbar(
    category: String,
    onBack: () -> Unit
) {
    Toolbar(
        backgroundColor = Color.White,
        title = category,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black
    )
}
