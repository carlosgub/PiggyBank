@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)

package presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import model.CategoryMonthDetailArgs
import model.CreateArgs
import model.FinanceEnum
import model.FinanceScreenExpenses
import model.FinanceScreenModel
import model.HomeArgs
import model.MenuItem
import model.MonthExpense
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import presentation.navigation.Screen
import presentation.viewmodel.HomeViewModel
import theme.ColorPrimary
import theme.Gray400
import theme.Gray600
import theme.Gray900
import theme.MonthBudgetCardColor
import utils.get
import utils.toMoneyFormat
import utils.views.ExpenseDivider
import utils.views.Loading
import utils.views.Toolbar
import utils.views.chart.FinanceBarChart
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    navigator: Navigator,
    args: HomeArgs
) {
    val viewModel = viewModel(HomeViewModel::class) {
        HomeViewModel(
            getFinanceUseCase = get()
        )
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = {
            viewModel.getFinanceStatus(args.monthKey)
        }
    )
    val coroutine = rememberCoroutineScope()
    Scaffold(
        topBar = {
            HomeToolbar(
                isHome = args.isHome,
                onAddExpensePressed = {
                    coroutine.launch {
                        val result = navigator.navigateForResult(
                            Screen.CreateScreen.createRoute(
                                CreateArgs(
                                    FinanceEnum.EXPENSE
                                )
                            )
                        )
                        if (result as Boolean) {
                            viewModel.getFinanceStatus(args.monthKey)
                        }
                    }
                },
                onAddIncomePressed = {
                    coroutine.launch {
                        val result = navigator.navigateForResult(
                            Screen.CreateScreen.createRoute(
                                CreateArgs(
                                    FinanceEnum.INCOME
                                )
                            )
                        )
                        if (result as Boolean) {
                            viewModel.getFinanceStatus(args.monthKey)
                        }
                    }
                },
                onSeeMonths = {
                    navigator.navigate(Screen.MonthsScreen.route)
                },
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
                .fillMaxSize()
                .background(ColorPrimary)
        ) {
            BoxWithConstraints {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                        .verticalScroll(scrollState)
                ) {
                    HomeObserver(
                        viewModel = viewModel,
                        navigator = navigator,
                        monthKey = args.monthKey,
                        modifier = Modifier
                            .height(this@BoxWithConstraints.maxHeight),
                        coroutine = coroutine
                    )
                    PullRefreshIndicator(
                        refreshing = viewModel.isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeObserver(
    viewModel: HomeViewModel,
    navigator: Navigator,
    monthKey: String,
    modifier: Modifier,
    coroutine: CoroutineScope
) {
    AnimatedContent(
        targetState = viewModel.uiState.collectAsStateWithLifecycle().value,
        transitionSpec = {
            (
                    fadeIn(animationSpec = tween(delayMillis = 90)) +
                            slideIntoContainer(
                                animationSpec = tween(delayMillis = 90),
                                towards = AnimatedContentTransitionScope.SlideDirection.Left
                            )
                    )
                .togetherWith(fadeOut(animationSpec = tween(90)))
        }
    ) { targetState ->
        when (targetState) {
            is GenericState.Success -> {
                HomeContent(
                    data = targetState.data,
                    onCategoryClick = {
                        coroutine.launch {
                            val result = navigator.navigateForResult(
                                Screen.CategoryMonthDetailScreen.createRoute(
                                    CategoryMonthDetailArgs(
                                        category = it.category.name,
                                        month = monthKey
                                    )
                                )
                            )
                            if (result as Boolean) {
                                viewModel.getFinanceStatus(monthKey)
                            }
                        }
                    },
                    modifier = modifier
                )
            }

            GenericState.Loading -> {
                Loading(modifier = modifier.background(ColorPrimary))
            }

            GenericState.Initial -> {
                viewModel.getFinanceStatus(monthKey)
            }

            else -> Unit
        }
    }
}

@Composable
private fun HomeContent(
    data: FinanceScreenModel,
    onCategoryClick: (FinanceScreenExpenses) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorPrimary)
    ) {
        HomeBodyMonthExpense(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxSize(),
            financeScreenModel = data
        )
        CardExpenses(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxSize(),
            expenses = data.expenses,
            income = data.income,
            monthExpense = data.monthExpense,
            onCategoryClick = onCategoryClick
        )
    }
}

@Composable
private fun HomeBodyMonthExpense(
    modifier: Modifier,
    financeScreenModel: FinanceScreenModel
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HomeBodyContent(
            monthAmount = financeScreenModel.expenseAmount,
            month = financeScreenModel.localDateTime.month,
            daySpent = financeScreenModel.daySpent
        )
    }
}

@Composable
private fun HomeBodyContent(monthAmount: Long, month: Month, daySpent: Map<LocalDateTime, Long>) {
    val pageCount = 2
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val coroutine = rememberCoroutineScope()
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false
    ) { page ->

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            HomeBodyLeftIcon(pagerState, coroutine)
            if (page == 0) {
                HomeBodySecondPageContent(
                    month = month,
                    monthAmount = monthAmount,
                    modifier = Modifier
                        .weight(1f)
                )
            } else {
                HomeBodySecondPageContent(
                    daySpent = daySpent,
                    modifier = Modifier
                        .weight(1f)
                )
            }
            HomeBodyRightContent(
                pagerState = pagerState,
                pageCount = pageCount,
                coroutine = coroutine,
                monthAmount = monthAmount
            )
        }
    }
}

@Composable
private fun HomeBodyRightContent(
    pagerState: PagerState,
    pageCount: Int,
    coroutine: CoroutineScope,
    monthAmount: Long
) {
    val isEnabled = pagerState.currentPage + 1 < pageCount && monthAmount > 0
    IconButton(
        onClick = {
            if (isEnabled) {
                coroutine.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        },
        content = {
            Icon(
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White
                        ),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        },
        modifier = Modifier
            .alpha(
                if (isEnabled) {
                    1f
                } else {
                    0f
                }
            )
    )
}

@Composable
private fun HomeBodyLeftIcon(
    pagerState: PagerState,
    coroutine: CoroutineScope
) {
    IconButton(
        onClick = {
            if (pagerState.currentPage > 0) {
                coroutine.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        },
        content = {
            Icon(
                imageVector = Icons.Filled.ArrowLeft,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.White
                        ),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        },
        modifier = Modifier
            .alpha(
                if (pagerState.currentPage > 0) {
                    1f
                } else {
                    0f
                }
            )
    )
}

@Composable
private fun HomeBodySecondPageContent(
    month: Month,
    monthAmount: Long,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month.name,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        Text(
            text = (monthAmount / 100.0).toMoneyFormat(),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun HomeBodySecondPageContent(
    daySpent: Map<LocalDateTime, Long>,
    modifier: Modifier
) {
    var overlayData by remember { mutableStateOf("") }
    Box(
        modifier = modifier
    ) {
        if (overlayData.isNotEmpty()) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Gray900)
                    .padding(
                        horizontal = 8.dp,
                        vertical = 6.dp
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
        FinanceBarChart(
            daySpent = daySpent,
            lineColor = Color.White,
            withYChart = true,
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxSize(),
            onOverlayData = {
                overlayData = it
            }
        )
    }
}

@Composable
private fun CardExpenses(
    modifier: Modifier,
    monthExpense: MonthExpense,
    expenses: List<FinanceScreenExpenses>,
    income: List<FinanceScreenExpenses>,
    onCategoryClick: (FinanceScreenExpenses) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        colors = CardDefaults.cardColors(
            containerColor = MonthBudgetCardColor
        )
    ) {
        CardMonthExpenseContent(
            monthExpense = monthExpense
        )
        Column {
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                val tabs = FinanceEnum.entries.toList()
                val firstTimeDelayAnimation = rememberSaveable { mutableStateOf(true) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                ) {
                    var tabIndex by remember { mutableStateOf(FinanceEnum.EXPENSE) }
                    TabRow(
                        selectedTabIndex = tabs.binarySearch(tabIndex),
                        containerColor = Color.White,
                        divider = {}
                    ) {
                        tabs.forEach { financeEnum ->
                            Tab(
                                text = {
                                    Text(
                                        text = financeEnum.financeName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black
                                    )
                                },
                                selected = tabIndex == financeEnum,
                                onClick = { tabIndex = financeEnum }
                            )
                        }
                    }
                    when (tabIndex) {
                        FinanceEnum.EXPENSE -> {
                            HomeFooterContent(
                                expenses = expenses,
                                onCategoryClick = onCategoryClick,
                                firstTimeDelayAnimation = firstTimeDelayAnimation.value
                            )
                            firstTimeDelayAnimation.value = false
                        }

                        FinanceEnum.INCOME ->
                            HomeFooterContent(
                                expenses = income,
                                onCategoryClick = onCategoryClick
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun CardMonthExpenseContent(
    monthExpense: MonthExpense
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 32.dp
            )
    ) {
        Row {
            Text(
                text = "Month Budget",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                monthExpense.incomeTotal.toMoneyFormat(),
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Gray600,
                fontWeight = FontWeight.Normal
            )
            Box(Modifier.weight(1f))
            Text(
                text = "${monthExpense.percentage}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        val percentage = (monthExpense.percentage / 100.00).toFloat()
        var progress by rememberSaveable { mutableStateOf(0f) }
        val progressAnimation by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = progressAnimation,
            color = ColorPrimary,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp)
                .height(6.dp),
            strokeCap = StrokeCap.Round
        )
        LaunchedEffect(percentage) {
            delay(AnimationConstants.DefaultDurationMillis.milliseconds)
            progress = percentage
        }
    }
}

@Composable
fun HomeFooterContent(
    expenses: List<FinanceScreenExpenses>,
    onCategoryClick: (FinanceScreenExpenses) -> Unit = {},
    firstTimeDelayAnimation: Boolean = false
) {
    if (expenses.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                itemsIndexed(expenses) { count, expense ->
                    Column {
                        if (count != 0) {
                            ExpenseDivider(
                                modifier = Modifier.padding(
                                    start = 64.dp
                                )
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    onCategoryClick(expense)
                                }
                                .padding(vertical = 12.dp)

                        ) {
                            val animationDelay =
                                if (expense.category.type == FinanceEnum.EXPENSE &&
                                    firstTimeDelayAnimation
                                ) {
                                    AnimationConstants.DefaultDurationMillis
                                } else {
                                    0
                                }
                            ExpenseIconProgress(
                                expense = expense,
                                animationDelay = animationDelay
                            )
                            Column(
                                modifier = Modifier.weight(1f).padding(start = 16.dp)
                            ) {
                                Text(
                                    text = expense.category.categoryName,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${expense.percentage}% of budget",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = Gray600,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            Column(
                                modifier = Modifier.padding(start = 16.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = (expense.amount / 100.0).toMoneyFormat(),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${expense.count} transactions",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(top = 4.dp),
                                    color = Gray600,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // TODO
    }
}

@Composable
fun ExpenseIconProgress(
    expense: FinanceScreenExpenses,
    animationDelay: Int
) {
    val percentage = (expense.percentage / 100.00).toFloat()
    Box(contentAlignment = Alignment.Center) {
        var progress by rememberSaveable { mutableStateOf(0f) }
        val progressAnimation by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.size(56.dp),
            strokeWidth = 3.dp,
            color = Gray400
        )
        CircularProgressIndicator(
            progress = progressAnimation,
            modifier = Modifier.size(56.dp),
            strokeWidth = 3.dp,
            color = expense.category.color
        )
        LaunchedEffect(Unit) {
            delay(animationDelay.milliseconds)
            progress = percentage
        }
        Icon(
            imageVector = expense.category.icon,
            contentDescription = null,
            tint = Gray600,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun HomeToolbar(
    isHome: Boolean,
    onAddExpensePressed: () -> Unit,
    onAddIncomePressed: () -> Unit,
    onSeeMonths: () -> Unit,
    onBack: () -> Unit
) {
    val leftIcon = if (isHome) Icons.Filled.CalendarMonth else null
    Toolbar(
        hasNavigationIcon = !isHome,
        navigation = onBack,
        title = "My Finances",
        dropDownIcon = Icons.Filled.Add,
        dropDownMenu = true,
        leftIcon = leftIcon,
        onLeftIconPressed = onSeeMonths,
        dropDownItems = mutableListOf(
            MenuItem(
                name = "Add Expense",
                icon = Icons.Filled.MoneyOff,
                onItemClicked = onAddExpensePressed
            ),
            MenuItem(
                name = "Add Income",
                icon = Icons.Filled.AttachMoney,
                onItemClicked = onAddIncomePressed
            ),
            MenuItem(
                name = "Add Wish",
                icon = Icons.Filled.Favorite,
                onItemClicked = onAddExpensePressed
            )
        )
    )
}
