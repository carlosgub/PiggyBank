package com.carlosgub.myfinances.presentation.screen.createexpense.observer

import com.carlosgub.myfinances.core.state.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun createExpenseObserver(
    sideEffect: GenericState<Unit>,
    navigator: Navigator,
) {
    when (sideEffect) {
        is GenericState.Error -> {
        }

        is GenericState.Success -> {
            navigator.popBackStack()
        }
    }
}
