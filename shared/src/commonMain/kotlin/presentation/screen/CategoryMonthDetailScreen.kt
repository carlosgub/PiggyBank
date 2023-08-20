@file:OptIn(ExperimentalMaterialApi::class)

package presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import kotlinx.datetime.LocalDate
import model.CategoryEnum
import model.CategoryMonthDetailArgs
import model.Expense
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.CategoryMonthDetailViewModel
import utils.getCategoryEnumFromName
import utils.getCurrentMonthName
import utils.toMoneyFormat
import utils.views.Loading
import utils.views.Toolbar

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

                },
                body = {
                    CategoryMonthDetailBody(
                        uiState.data
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
    list: List<Expense>
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        LazyColumn {
            items(list){
                Text(it.note)
            }
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
