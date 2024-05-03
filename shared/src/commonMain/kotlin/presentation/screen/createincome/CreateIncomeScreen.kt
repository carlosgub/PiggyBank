package presentation.screen.createincome

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import presentation.screen.createincome.content.CreateIncomeContent
import presentation.screen.createincome.content.createIncomeObserver
import presentation.viewmodel.createincome.CreateIncomeViewModel
import utils.views.Toolbar

@Composable
fun CreateIncomeScreen(viewModel: CreateIncomeViewModel = koinInject()) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val createScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            createIncomeObserver(
                sideEffect = sideEffect,
                navigator = navigator,
            )
        }
    }
    Scaffold(
        topBar = {
            CreateIncomeToolbar(
                onBack = {
                    navigator.popBackStack()
                },
            )
        },
    ) { paddingValues ->
        CreateIncomeContent(
            state = createScreenState,
            intents = viewModel,
            modifier =
                Modifier
                    .padding(paddingValues),
        )
    }
}

@Composable
private fun CreateIncomeToolbar(onBack: () -> Unit) {
    Toolbar(
        hasNavigationIcon = true,
        title = "Create Income",
        navigation = onBack,
    )
}
