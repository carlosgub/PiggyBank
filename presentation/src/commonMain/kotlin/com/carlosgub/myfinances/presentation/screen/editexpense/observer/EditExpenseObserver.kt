package com.carlosgub.myfinances.presentation.screen.editexpense.observer

import androidx.navigation.NavHostController
import com.carlosgub.myfinances.core.state.GenericState

fun editExpenseObserver(
    sideEffect: GenericState<Unit>,
    navController: NavHostController,
) {
    when (sideEffect) {
        is GenericState.Success -> navController.popBackStack()
        else -> Unit
    }
}
