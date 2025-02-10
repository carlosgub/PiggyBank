package com.carlosgub.myfinances.presentation.screen.editincome.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.core.state.GenericState

fun editIncomeObserver(
    sideEffect: GenericState<Unit>,
    navController: NavHostController,
) {
    when (sideEffect) {
        is GenericState.Success -> navController.popBackStack()
        else -> Unit
    }
}
