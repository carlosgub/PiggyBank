package presentation.screen.edit.content

import core.sealed.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun editObserver(
    sideEffect: GenericState<Unit>,
    navigator: Navigator
) {
    when (sideEffect) {
        is GenericState.Success -> navigator.popBackStack()
        else -> Unit
    }
}
