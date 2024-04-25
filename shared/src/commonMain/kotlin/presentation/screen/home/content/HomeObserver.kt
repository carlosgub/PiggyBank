package presentation.screen.home.content

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.CategoryMonthDetailArgs
import model.FinanceEnum
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.home.HomeScreenIntents
import presentation.viewmodel.home.HomeScreenSideEffect
import presentation.viewmodel.home.HomeScreenState


fun homeObserver(
    coroutine: CoroutineScope,
    sideEffect: HomeScreenSideEffect,
    navigator: Navigator,
    intents: HomeScreenIntents,
    state: HomeScreenState
) {
    when (sideEffect) {
        HomeScreenSideEffect.NavigateToAddExpense -> navigateToAddExpenseScreen(
            coroutine,
            navigator,
            intents
        )

        HomeScreenSideEffect.NavigateToAddIncome -> navigateToAddIncomeScreen(
            coroutine,
            navigator,
            intents
        )

        is HomeScreenSideEffect.NavigateToMonthDetail -> navigateMonthDetailScreen(
            coroutine,
            navigator,
            intents,
            sideEffect.financeScreenExpenses.category.name,
            state.monthKey
        )

        HomeScreenSideEffect.NavigateToMonths -> navigator.navigate(Screen.MonthsScreen.route)
    }
}

private fun navigateToAddIncomeScreen(
    coroutine: CoroutineScope,
    navigator: Navigator,
    intents: HomeScreenIntents
) {
    coroutine.launch {
        val result = navigator.navigateForResult(
            Screen.CreateScreen.createRoute(
                FinanceEnum.INCOME.name
            )
        )
        if (result as Boolean) {
            intents.getFinanceStatus()
        }
    }
}

private fun navigateToAddExpenseScreen(
    coroutine: CoroutineScope,
    navigator: Navigator,
    intents: HomeScreenIntents
) {
    coroutine.launch {
        val result = navigator.navigateForResult(
            Screen.CreateScreen.createRoute(
                FinanceEnum.EXPENSE.name
            )
        )
        if (result as Boolean) {
            intents.getFinanceStatus()
        }
    }
}

private fun navigateMonthDetailScreen(
    coroutine: CoroutineScope,
    navigator: Navigator,
    intents: HomeScreenIntents,
    category: String,
    monthKey: String
) {
    coroutine.launch {
        val result = navigator.navigateForResult(
            Screen.CategoryMonthDetailScreen.createRoute(
                CategoryMonthDetailArgs(
                    category = category,
                    month = monthKey
                )
            )
        )
        if (result as Boolean) {
            intents.getFinanceStatus()
        }
    }
}