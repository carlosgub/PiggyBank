package com.carlosgub.myfinances.presentation.screen.createincome.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.core.state.GenericState

fun createIncomeObserver(
    sideEffect: GenericState<Unit>,
    navController: NavHostController,
) {
    when (sideEffect) {
        is GenericState.Error -> {
        }

        is GenericState.Success -> {
            navController.popBackStack()
        }
    }
}
