package com.carlosgub.myfinances.presentation.screen.home.content

import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenState
import moe.tlaster.precompose.navigation.Navigator

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
