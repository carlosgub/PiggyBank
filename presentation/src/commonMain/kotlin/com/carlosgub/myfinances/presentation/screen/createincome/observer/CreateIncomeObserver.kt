package com.carlosgub.myfinances.presentation.screen.createincome.observer

import com.carlosgub.myfinances.core.state.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun createIncomeObserver(
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
