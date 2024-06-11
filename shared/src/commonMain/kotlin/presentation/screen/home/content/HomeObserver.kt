package presentation.screen.home.content

import moe.tlaster.precompose.navigation.Navigator
import presentation.navigation.AppNavigation
import presentation.viewmodel.home.HomeScreenSideEffect
import presentation.viewmodel.home.HomeScreenState

fun homeObserver(
    sideEffect: HomeScreenSideEffect,
    navigator: Navigator,
    state: HomeScreenState,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        HomeScreenSideEffect.NavigateToAddExpense ->
            appNavigation.navigateToAddExpense(
                navigator = navigator,
            )

        HomeScreenSideEffect.NavigateToAddIncome ->
            appNavigation.navigateToAddIncome(
                navigator = navigator,
            )

        is HomeScreenSideEffect.NavigateToMonthDetail -> appNavigation.navigateToMonthDetail(
            navigator = navigator,
            categoryName = sideEffect.categoryName,
            monthKey = state.monthKey,
        )

        HomeScreenSideEffect.NavigateToMonths -> appNavigation.navigateToMonths(
            navigator = navigator,
        )
    }
}
