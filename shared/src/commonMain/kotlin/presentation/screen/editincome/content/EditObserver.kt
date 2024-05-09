package presentation.screen.editincome.content

import core.sealed.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun editIncomeObserver(
    sideEffect: GenericState<Unit>,
    navigator: Navigator
) {
    when (sideEffect) {
        is GenericState.Success -> navigator.popBackStack()
        else -> Unit
    }
}
