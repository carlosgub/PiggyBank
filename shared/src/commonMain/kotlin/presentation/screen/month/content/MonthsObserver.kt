package presentation.screen.month.content

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.CategoryMonthDetailArgs
import model.CreateArgs
import model.FinanceEnum
import model.HomeArgs
import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.Screen
import presentation.viewmodel.home.HomeScreenIntents
import presentation.viewmodel.home.HomeScreenSideEffect
import presentation.viewmodel.home.HomeScreenState
import presentation.viewmodel.months.MonthsScreenSideEffect


fun monthsObserver(
    sideEffect: MonthsScreenSideEffect,
    navigator: Navigator
) {
    when (sideEffect) {
        is MonthsScreenSideEffect.NavigateToMonthDetail -> navigateToMonthDetail(
            navigator = navigator,
            homeArgs = sideEffect.homeArgs
        )
    }
}

private fun navigateToMonthDetail(
    navigator: Navigator,
    homeArgs: HomeArgs
) {
    navigator.navigate(
        Screen.Home.createRoute(homeArgs)
    )
}