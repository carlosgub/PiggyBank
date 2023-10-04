package presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.coroutines.delay
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
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.HomeViewModel
import theme.ColorPrimary
import theme.Gray400
import theme.Gray600
import theme.MonthBudgetCardColor
import utils.toMoneyFormat
import utils.views.ExpenseDivider
import utils.views.Loading
import utils.views.Toolbar
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    navigator: Navigator,
    args: HomeArgs
) {
    val viewModel = koinViewModel(HomeViewModel::class)
    Scaffold(
        topBar = {
            HomeToolbar(
                isHome = args.isHome,
                onAddExpensePressed = {
                    navigator.navigate(
                        Screen.CreateScreen.createRoute(
                            CreateArgs(
                                FinanceEnum.EXPENSE
                            )
                        )
                    )
                },
                onAddIncomePressed = {
                    navigator.navigate(
                        Screen.CreateScreen.createRoute(
                            CreateArgs(
                                FinanceEnum.INCOME
                            )
                        )
                    )
                },
                onSeeMonths = {
                    navigator.navigate(Screen.MonthsScreen.route)
                },
                onRefresh = {
                    viewModel.getFinanceStatus(args.monthKey)
                },
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding()
                )
                .fillMaxSize()
                .background(ColorPrimary)
        ) {
            HomeObserver(
                viewModel = viewModel,
                navigator = navigator,
                monthKey = args.monthKey
            )
        }
    }
}

@Composable
private fun HomeObserver(
    viewModel: HomeViewModel,
    navigator: Navigator,
    monthKey: String
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
                        navigator.navigate(
                            Screen.CategoryMonthDetailScreen.createRoute(
                                CategoryMonthDetailArgs(
                                    category = it.category.name,
                                    month = monthKey
                                )
                            )
                        )
                    }
                )
            }

            GenericState.Loading -> {
                Loading(modifier = Modifier.background(ColorPrimary))
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
    onCategoryClick: (FinanceScreenExpenses) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPrimary)
        ) {
            HomeBodyMonthExpense(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize(),
                bodyContent = {
                    HomeBodyContent(
                        monthAmount = data.expenseAmount,
                        month = data.localDateTime.month
                    )
                }
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
}

@Composable
private fun HomeBodyMonthExpense(
    modifier: Modifier,
    bodyContent: @Composable () -> Unit
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        bodyContent()
    }
}

@Composable
private fun HomeBodyContent(monthAmount: Int, month: Month) {
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
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
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
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
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
                        selectedTabIndex = tabs.indexOf(tabIndex),
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
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    Toolbar(
        hasNavigationIcon = !isHome,
        navigation = onBack,
        title = "My Finances",
        dropDownMenu = true,
        leftIcon = Icons.Filled.Refresh,
        onLeftIconPressed = onRefresh,
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
        ).apply {
            if (isHome) {
                add(
                    MenuItem(
                        name = "Months",
                        icon = Icons.Filled.CalendarMonth,
                        onItemClicked = onSeeMonths
                    )
                )
            }
        }
    )
}
