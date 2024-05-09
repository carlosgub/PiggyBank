package presentation.screen.month.content

import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.months.MonthsScreenSideEffect

fun monthsObserver(
    sideEffect: MonthsScreenSideEffect,
    navigator: Navigator
) {
    when (sideEffect) {
        is MonthsScreenSideEffect.NavigateToMonthDetail ->
            navigateToMonthDetail(
                navigator = navigator,
                monthKey = sideEffect.monthKey
            )
    }
}

private fun navigateToMonthDetail(
    navigator: Navigator,
    monthKey: String
) {
    navigator.navigate(
        Screen.Home.createRoute(monthKey)
    )
}
