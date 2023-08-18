@file:OptIn(ExperimentalMaterialApi::class)

package presentation.screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import model.FinanceScreenExpenses
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
import utils.getCurrentMonthName
import utils.toMoneyFormat
import utils.toPrecision
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
                        uiState.data.monthAmount
                    )
                },
                footerContent = {
                    HomeFooterContent(
                        uiState.data.expenses
                    )
                }
            )
        }

        GenericState.Loading, GenericState.Initial -> {
            HomeContent(
                navigator = navigator,
                bodyContent = {
                    Loading()
                },
                footerContent = {
                    Loading()
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
    footerContent: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            HomeToolbar(
                onAddPressed = {
                    navigator.navigate(Screen.CreateExpenseScreen.route)
                }
            )
        }
    ) { paddingValues ->
        /*val pullRefreshState = rememberPullRefreshState(
        viewModel.isRefreshing,
        onRefresh = {
            viewModel.getFinanceStatus()
        }
    )*/
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
//            .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPrimary),
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
                    footerContent = footerContent
                )
            }
//        PullRefreshIndicator(
//            refreshing = viewModel.isRefreshing,
//            state = pullRefreshState,
//            modifier = Modifier.align(Alignment.TopCenter)
//        )
        }
    }
}

@Composable
private fun HomeBodyMonthExpense(
    modifier: Modifier,
    bodyContent: @Composable () -> Unit,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
    footerContent: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Expenses",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth()
            )
            footerContent()
        }
    }
}

@Composable
fun HomeFooterContent(expenses: List<FinanceScreenExpenses>) {
    if (expenses.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.padding(vertical = 12.dp)
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
                            .clickable {}
                            .padding(vertical = 12.dp)

                    ) {
                        ExpenseIconProgress(expense)
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 16.dp)
                        ) {
                            Text(expense.category.categoryName)
                            Text("${expense.percentage}% of budget")
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 16.dp)
                        ) {
                            Text((expense.amount / 100.0).toMoneyFormat())
                            Text("${expense.count} transactions")
                        }
                    }
                }

            }

        }
    } else {

    }

}

@Composable
fun ExpenseIconProgress(expense: FinanceScreenExpenses) {
    val percentage = (expense.percentage / 100.00).toFloat()
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.size(64.dp),
            strokeWidth = 3.dp,
            color = Gray400
        )
        CircularProgressIndicator(
            progress = percentage,
            modifier = Modifier.size(64.dp),
            strokeWidth = 3.dp,
            color = expense.category.color
        )

        Icon(
            imageVector = expense.category.icon,
            contentDescription = null,
            tint = Gray600,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun HomeToolbar(
    onAddPressed: () -> Unit
) {
    Toolbar(
        elevation = 0.dp,
        title = "My Finances",
        leftIcon = Icons.Default.Add,
        onLeftIconPressed = onAddPressed
    )
}
