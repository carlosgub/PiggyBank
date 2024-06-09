package presentation.screen.createexpense

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.carlosgub.myfinances.core.navigation.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import myfinances.shared.generated.resources.Res
import myfinances.shared.generated.resources.create_expense_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import presentation.screen.createexpense.content.CreateExpenseContent
import presentation.screen.createexpense.content.createObserver
import presentation.viewmodel.createexpense.CreateExpenseViewModel
import utils.views.Toolbar

@Composable
fun CreateExpenseScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateExpenseViewModel = koinInject(),
) {
    val navigator = LocalNavController.current
    val scope = CoroutineScope(Dispatchers.Main)
    val createScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            createObserver(
                sideEffect = sideEffect,
                navigator = navigator,
            )
        }
    }
    Scaffold(
        topBar = {
            CreateExpenseToolbar(
                onBack = {
                    navigator.popBackStack()
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        CreateExpenseContent(
            state = createScreenState,
            intents = viewModel,
            modifier = Modifier
                .padding(paddingValues),
        )
    }
}

@Composable
private fun CreateExpenseToolbar(onBack: () -> Unit) {
    Toolbar(
        hasNavigationIcon = true,
        title = stringResource(Res.string.create_expense_title),
        navigation = onBack,
    )
}
