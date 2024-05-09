package presentation.screen.home.content

import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.home.HomeScreenSideEffect
import presentation.viewmodel.home.HomeScreenState

fun homeObserver(
    sideEffect: HomeScreenSideEffect,
    navigator: Navigator,
    state: HomeScreenState,
) {
    when (sideEffect) {
        HomeScreenSideEffect.NavigateToAddExpense ->
            navigateToAddExpenseScreen(
                navigator = navigator,
            )

        HomeScreenSideEffect.NavigateToAddIncome ->
            navigateToAddIncomeScreen(
                navigator = navigator,
            )

        is HomeScreenSideEffect.NavigateToMonthDetail ->
            navigateMonthDetailScreen(
                navigator = navigator,
                categoryName = sideEffect.categoryName,
                monthKey = state.monthKey,
            )

        HomeScreenSideEffect.NavigateToMonths -> navigator.navigate(Screen.MonthsScreen.route)
    }
}

private fun navigateToAddIncomeScreen(navigator: Navigator) {
    navigator.navigate(
        Screen.CreateIncomeScreen.route,
    )
}

private fun navigateToAddExpenseScreen(navigator: Navigator) {
    navigator.navigate(
        Screen.CreateExpenseScreen.route,
    )
}

private fun navigateMonthDetailScreen(
    navigator: Navigator,
    categoryName: String,
    monthKey: String,
) {
    navigator.navigate(
        Screen.CategoryMonthDetailScreen.createRoute(
            monthKey = monthKey,
            categoryName = categoryName,
        ),
    )
}
