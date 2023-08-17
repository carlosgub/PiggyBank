@file:OptIn(ExperimentalMaterialApi::class)

package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import model.Finance
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.navigation.Screen
import presentation.screen.contents.ExpenseDialog
import presentation.viewmodel.HomeViewModel
import theme.ColorPrimary
import utils.toMoneyFormat
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
                finance = uiState.data,
                viewModel = viewModel,
                navigator = navigator
            )
        }

        GenericState.Loading -> {

        }

        else -> Unit
    }

}

@Composable
private fun HomeContent(
    finance: Finance,
    viewModel: HomeViewModel,
    navigator: Navigator
) {
    Scaffold(
        topBar = {
            HomeToolbar(
                onAddPressed = {
                    navigator.navigate(Screen.CreateExpenseScreen.createRoute(finance))
                }
            )
        }
    ) { paddingValues ->
        HomeBodyContent(
            paddingValues, finance, viewModel
        )
    }
}

@Composable
private fun HomeBodyContent(
    paddingValues: PaddingValues,
    finance: Finance,
    viewModel: HomeViewModel
) {
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
                finance,
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxSize()
            )
            CardExpenses(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxSize()
            )
        }
//        PullRefreshIndicator(
//            refreshing = viewModel.isRefreshing,
//            state = pullRefreshState,
//            modifier = Modifier.align(Alignment.TopCenter)
//        )
        if (viewModel.showAddExpenseDialog.collectAsStateWithLifecycle().value) {
            ExpenseDialog(
                onDismissRequest = {
                    viewModel.showAddExpenseDialog(false)
                }
            )
        }
    }
}

@Composable
private fun HomeBodyMonthExpense(
    finance: Finance,
    modifier: Modifier
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val year = today.year
        val month = today.month
        val currentKey = "${
            if (month.number < 10) {
                "0${month.number}"
            } else {
                month.number
            }
        }${year}"
        Text(
            text = month.name,
            style = MaterialTheme.typography.h6,
            color = Color.White
        )
        val amountText = if (finance.month.containsKey(currentKey)) {
            (finance.month[currentKey]!!.amount / 100.0).toMoneyFormat()
        } else {
            0.0f.toMoneyFormat()
        }
        Text(
            text = amountText,
            style = MaterialTheme.typography.h3,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun CardExpenses(
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "My Expenses",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
        }
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
