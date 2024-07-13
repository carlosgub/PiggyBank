package com.carlosgub.myfinances.presentation.screen.editexpense.content

import com.carlosgub.myfinances.core.state.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun editExpenseObserver(
    sideEffect: GenericState<Unit>,
    navigator: Navigator,
) {
    when (sideEffect) {
        is GenericState.Success -> navigator.popBackStack()
        else -> Unit
    }
}
