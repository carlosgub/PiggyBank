package presentation.screen.create

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.CreateArgs
import model.FinanceEnum
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.Navigator
import org.koin.compose.koinInject
import presentation.screen.create.content.CreateContent
import presentation.screen.create.content.createObserver
import presentation.viewmodel.create.CreateViewModel
import utils.isExpense
import utils.views.Toolbar

@Composable
fun CreateScreen(
    viewModel: CreateViewModel = koinInject(),
    navigator: Navigator,
    args: CreateArgs
) {
    val scope = CoroutineScope(Dispatchers.Main)
    val createScreenState by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    scope.launch {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            createObserver(
                sideEffect = sideEffect,
                navigator = navigator
            )
        }
    }
    Scaffold(
        topBar = {
            CreateToolbar(
                financeEnum = args.financeEnum,
                onBack = {
                    navigator.goBackWith(false)
                }
            )
        }
    ) { paddingValues ->
        CreateContent(
            state = createScreenState,
            intents = viewModel,
            financeEnum = args.financeEnum,
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}

@Composable
private fun CreateToolbar(
    financeEnum: FinanceEnum,
    onBack: () -> Unit
) {
    val title = if (financeEnum.isExpense()) "Expense" else "Income"
    Toolbar(
        hasNavigationIcon = true,
        title = "Create $title",
        navigation = onBack
    )
}
