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
import myfinances.shared.generated.resources.Res
import myfinances.shared.generated.resources.create_income_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import presentation.screen.createincome.content.CreateIncomeContent
import presentation.screen.createincome.content.createIncomeObserver
import presentation.viewmodel.createincome.CreateIncomeViewModel
import utils.views.Toolbar

@Composable
fun CreateIncomeScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateIncomeViewModel = koinInject(),
) {
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
        modifier = modifier,
    ) { paddingValues ->
        CreateIncomeContent(
            state = createScreenState,
            intents = viewModel,
            modifier = Modifier
                .padding(paddingValues),
        )
    }
}

@Composable
private fun CreateIncomeToolbar(onBack: () -> Unit) {
    Toolbar(
        hasNavigationIcon = true,
        title = stringResource(Res.string.create_income_title),
        navigation = onBack,
    )
}
