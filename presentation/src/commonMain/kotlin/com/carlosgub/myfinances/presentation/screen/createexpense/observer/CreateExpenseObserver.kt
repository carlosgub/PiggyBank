package com.carlosgub.myfinances.presentation.screen.createexpense.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.core.state.GenericState

fun createExpenseObserver(
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
