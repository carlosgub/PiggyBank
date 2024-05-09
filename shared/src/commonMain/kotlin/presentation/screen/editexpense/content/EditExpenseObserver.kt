package presentation.screen.editexpense.content

import core.sealed.GenericState
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
