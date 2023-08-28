@file:OptIn(ExperimentalMaterialApi::class)

package presentation.screen

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import model.CategoryMonthDetailArgs
import model.FinanceEnum
import model.FinanceScreenExpenses
import model.MenuItem
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.navigation.Screen
import presentation.viewmodel.HomeViewModel
import theme.ColorPrimary
import theme.ColorSeparator
import theme.Gray400
import theme.Gray600
import theme.divider_thickness
import utils.getCurrentMonthKey
import utils.getCurrentMonthName
import utils.toMoneyFormat
import utils.views.Loading
import utils.views.Toolbar

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinInject(), navigator: Navigator) {
    HomeObserver(viewModel, navigator)
}

@Composable
private fun HomeObserver(viewModel: HomeViewModel, navigator: Navigator) {
    when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is GenericState.Success -> {
            HomeContent(
                navigator = navigator,
                bodyContent = {
                    HomeBodyContent(
                        uiState.data.expenseAmount
                    )
                },
                expenseFooterContent = {
                    HomeFooterContent(
                        uiState.data.expenses
                    ) {
                        navigator.navigate(
                            Screen.CategoryMonthDetailScreen.createRoute(
                                CategoryMonthDetailArgs(
                                    category = it.category.name,
                                    month = getCurrentMonthKey()
                                )
                            )
                        )
                    }
                },
                incomeFooterContent = {
                    HomeFooterContent(
                        uiState.data.incomes
                    )
                },
                onRefresh = {
                    viewModel.getFinanceStatus()
                }
            )
        }

        GenericState.Loading, GenericState.Initial -> {
            HomeContent(
                navigator = navigator,
                bodyContent = {
                    Loading()
                },
                expenseFooterContent = {
                    Loading()
                },
                incomeFooterContent = {
                    Loading()
                },
                onRefresh = {
                    viewModel.getFinanceStatus()
                }
            )
        }

        else -> Unit
    }
}

@Composable
private fun HomeContent(
    navigator: Navigator,
    bodyContent: @Composable () -> Unit,
    expenseFooterContent: @Composable () -> Unit,
    incomeFooterContent: @Composable () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeToolbar(
                onAddExpensePressed = {
                    navigator.navigate(Screen.CreateExpenseScreen.route)
                },
                onAddIncomePressed = {
                    navigator.navigate(Screen.CreateIncomeScreen.route)
                },
                onRefresh = onRefresh
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPrimary)
            ) {
                HomeBodyMonthExpense(
                    modifier = Modifier
                        .weight(0.45f)
                        .fillMaxSize(),
                    bodyContent = bodyContent
                )
                CardExpenses(
                    modifier = Modifier
                        .weight(0.55f)
                        .fillMaxSize(),
                    expenseFooterContent = expenseFooterContent,
                    incomeFooterContent = incomeFooterContent
                )
            }
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
private fun HomeBodyContent(monthAmount: Int) {
    Text(
        text = getCurrentMonthName(),
        style = MaterialTheme.typography.h6,
        color = Color.White
    )
    Text(
        text = (monthAmount / 100.0).toMoneyFormat(),
        style = MaterialTheme.typography.h3,
        color = Color.White,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
private fun CardExpenses(
    modifier: Modifier,
    expenseFooterContent: @Composable () -> Unit,
    incomeFooterContent: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        val tabs = FinanceEnum.entries.toList()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp)
        ) {
            var tabIndex by remember { mutableStateOf(FinanceEnum.EXPENSE) }
            TabRow(
                selectedTabIndex = tabs.indexOf(tabIndex),
                backgroundColor = Color.White,
                divider = {}
            ) {
                tabs.forEach { financeEnum ->
                    Tab(
                        text = {
                            Text(
                                text = financeEnum.financeName,
                                style = MaterialTheme.typography.body1,
                                color = Color.Black
                            )
                        },
                        selected = tabIndex == financeEnum,
                        onClick = { tabIndex = financeEnum }
                    )
                }
            }
            when (tabIndex) {
                FinanceEnum.EXPENSE -> expenseFooterContent()
                FinanceEnum.INCOME -> incomeFooterContent()
            }
        }
    }
}

@Composable
fun HomeFooterContent(
    expenses: List<FinanceScreenExpenses>,
    onCategoryClick: (FinanceScreenExpenses) -> Unit = {}
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
                            Divider(
                                modifier = Modifier.fillMaxWidth().padding(
                                    start = 64.dp
                                ),
                                thickness = divider_thickness,
                                color = ColorSeparator
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
                            ExpenseIconProgress(expense)
                            Column(
                                modifier = Modifier.weight(1f).padding(start = 16.dp)
                            ) {
                                Text(
                                    text = expense.category.categoryName,
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${expense.percentage}% of budget",
                                    style = MaterialTheme.typography.caption,
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
                                    style = MaterialTheme.typography.body2,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${expense.count} transactions",
                                    style = MaterialTheme.typography.caption,
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
fun ExpenseIconProgress(expense: FinanceScreenExpenses) {
    val percentage = (expense.percentage / 100.00).toFloat()
    Box(contentAlignment = Alignment.Center) {
        var progress by remember { mutableStateOf(0f) }
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
        LaunchedEffect(percentage) {
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
    onAddExpensePressed: () -> Unit,
    onAddIncomePressed: () -> Unit,
    onRefresh: () -> Unit
) {
    Toolbar(
        elevation = 0.dp,
        title = "My Finances",
        dropDownIcon = Icons.Default.Add,
        dropDownMenu = true,
        leftIcon = Icons.Filled.Refresh,
        onLeftIconPressed = onRefresh,
        dropDownItems = listOf(
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
