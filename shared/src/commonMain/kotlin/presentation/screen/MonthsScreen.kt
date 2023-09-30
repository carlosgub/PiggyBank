package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.sealed.GenericState
import model.CategoryEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.Navigator
import presentation.viewmodel.CategoryMonthDetailViewModel
import presentation.viewmodel.MonthsScreenViewModel
import theme.White
import utils.views.Loading
import utils.views.Toolbar

@Composable
fun MonthsScreen(
    navigator: Navigator
) {
    val viewModel = koinViewModel(MonthsScreenViewModel::class)
    Scaffold(
        topBar = {
            MonthsToolbar(
                onBack = {
                    navigator.goBack()
                }
            )
        }
    ) { paddingValues ->
        MonthsObserver(viewModel, paddingValues)
    }
}

@Composable
private fun MonthsObserver(
    viewModel: MonthsScreenViewModel,
    paddingValues: PaddingValues
) {
    when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is GenericState.Success -> {
            MonthsContent(
                paddingValues,
                content = {

                }
            )
        }

        GenericState.Loading -> {
            MonthsContent(
                paddingValues,
                content = {
                    Loading()
                }
            )
        }

        GenericState.Initial -> {
            viewModel.getMonths()
        }

        else -> Unit
    }
}

@Composable
fun MonthsContent(
    paddingValues: PaddingValues,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(White)
            .padding(
                top = paddingValues.calculateTopPadding()
            )
            .fillMaxSize()
    ) {
        content()
    }
}

@Composable
private fun MonthsToolbar(
    onBack: () -> Unit
) {
    Toolbar(
        backgroundColor = Color.White,
        hasNavigationIcon = true,
        navigation = onBack,
        contentColor = Color.Black,
        title = "Months"
    )
}


