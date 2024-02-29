@file:OptIn(ExperimentalFoundationApi::class)

package presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import model.CategoryEnum
import model.CategoryMonthDetailArgs
import model.EditArgs
import model.ExpenseScreenModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.CategoryMonthDetailViewModel
import theme.Gray600
import theme.Gray900
import theme.White
import theme.spacing_1
import theme.spacing_1_2
import theme.spacing_2
import theme.spacing_4
import theme.spacing_6
import utils.getCategoryEnumFromName
import utils.toMoneyFormat
import utils.views.DataZero
import utils.views.ExpenseDivider
import utils.views.Loading
import utils.views.Toolbar
import utils.views.chart.FinanceLineChart

@Composable
fun CategoryMonthDetailScreen(
    navigator: Navigator,
    args: CategoryMonthDetailArgs
) {
    val viewModel = koinViewModel(vmClass = CategoryMonthDetailViewModel::class)
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
                        uiState.data.daySpent
                    )
                },
                body = {
                    CategoryMonthDetailBody(
                        uiState.data.expenseScreenModel,
                        categoryEnum
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
    categoryEnum: CategoryEnum,
    expenseClicked: (ExpenseScreenModel) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = spacing_2)
            .fillMaxSize(),
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        if (list.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .background(White)
                    .fillMaxSize()
                    .padding(
                        top = spacing_6,
                        start = spacing_6,
                        end = spacing_6
                    )
            ) {
                itemsIndexed(list) { count, expense ->
                    Column {
                        if (count != 0) {
                            ExpenseDivider()
                        }
                        CategoryMonthExpenseItem(
                            expense = expense,
                            expenseClicked = expenseClicked,
                            modifier = Modifier.animateItemPlacement(
                                animationSpec = tween(600)
                            )
                        )
                    }
                }
            }
        } else {
            DataZero<Any>(
                title = "Ooops! It's Empty",
                message = "Looks like you don't have anything in this category",
                modifier = Modifier
                    .background(White)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoryMonthExpenseItem(
    expense: ExpenseScreenModel,
    expenseClicked: (ExpenseScreenModel) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .combinedClickable(
                onClick = {
                },
                onLongClick = {
                    expenseClicked(expense)
                }
            )
            .padding(vertical = spacing_4)
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = spacing_4)
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
                modifier = Modifier.padding(top = spacing_1),
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

@Composable
private fun CategoryMonthDetailHeader(
    monthTotal: Long,
    daySpent: Map<LocalDateTime, Long>
) {
    var overlayData by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Column {
                Spacer(modifier = Modifier.weight(0.2f))
                FinanceLineChart(
                    daySpent,
                    withYChart = false
                )
            }
            Column {
                Text(
                    text = (monthTotal / 100.0).toMoneyFormat(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(spacing_6)
                )
            }
            if (overlayData.isNotEmpty()) {
                Box(
                    Modifier
                        .padding(
                            horizontal = spacing_2,
                            vertical = spacing_1_2
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(Gray900)
                        .padding(
                            horizontal = spacing_2,
                            vertical = spacing_1_2
                        )
                        .align(Alignment.TopEnd)
                ) {
                    AnimatedContent(
                        targetState = overlayData,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
                                    fadeOut(animationSpec = tween(durationMillis = 300))
                        },
                        contentAlignment = Alignment.Center
                    ) { overlayData ->
                        Text(
                            text = overlayData,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
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
        title = category,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black
    )
}
