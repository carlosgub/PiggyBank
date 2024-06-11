package presentation.screen.month.content

import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.AppNavigation
import presentation.viewmodel.months.MonthsScreenSideEffect

fun monthsObserver(
    sideEffect: MonthsScreenSideEffect,
    navigator: Navigator,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is MonthsScreenSideEffect.NavigateToMonthDetail -> appNavigation.navigateToHome(
            navigator = navigator,
            monthKey = sideEffect.monthKey,
        )
    }
}
