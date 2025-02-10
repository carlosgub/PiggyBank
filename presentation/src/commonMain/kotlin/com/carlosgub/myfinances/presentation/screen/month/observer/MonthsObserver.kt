package com.carlosgub.myfinances.presentation.screen.month.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsScreenSideEffect

fun monthsObserver(
    sideEffect: MonthsScreenSideEffect,
    navController: NavHostController,
    appNavigation: AppNavigation,
) {
    when (sideEffect) {
        is MonthsScreenSideEffect.NavigateToMonthDetail -> appNavigation.navigateToHome(
            navController = navController,
            monthKey = sideEffect.monthKey,
        )
    }
}
