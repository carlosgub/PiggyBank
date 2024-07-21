package com.carlosgub.myfinances.presentation.screen.month.content

import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsScreenSideEffect
import moe.tlaster.precompose.navigation.Navigator

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
