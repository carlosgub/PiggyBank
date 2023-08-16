package presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.sealed.GenericState
import model.Finance
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.viewmodel.HomeViewModel
import theme.ColorPrimary
import utils.Toolbar

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinInject(), navigator: Navigator) {
    HomeObserver(viewModel, navigator)
}

@Composable
private fun HomeObserver(viewModel: HomeViewModel, navigator: Navigator) {
    when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
        is GenericState.Success -> {
            HomeContent(
                finance = uiState.data
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
) {
    Scaffold(
        topBar = {
            HomeToolbar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(ColorPrimary),
        ) {
            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = finance.month.toString())
            }
            Card(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxSize(),
                shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
            ) {

            }
        }
    }
}

@Composable
private fun HomeToolbar() {
    Toolbar(
        elevation = 0.dp,
        title = "My Finances",
        leftIcon = Icons.Default.Add,
        onLeftIconPressed = {

        }
    )
}
