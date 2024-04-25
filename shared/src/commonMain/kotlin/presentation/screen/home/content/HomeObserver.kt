package presentation.screen.home.content

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
            coroutine = coroutine,
            navigator = navigator,
            intents = intents,
            categoryName = sideEffect.financeScreenExpenses.category.name,
            monthKey = state.monthKey
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
    categoryName: String,
    monthKey: String
) {
    coroutine.launch {
        val result = navigator.navigateForResult(
            Screen.CategoryMonthDetailScreen.createRoute(
                monthKey = monthKey,
                categoryName = categoryName
            )
        )
        if (result as Boolean) {
            intents.getFinanceStatus()
        }
    }
}
