package presentation.screen.createincome.content

import core.sealed.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun createIncomeObserver(
    sideEffect: GenericState<Unit>,
    navigator: Navigator
) {
    when (sideEffect) {
        is GenericState.Error -> {
        }

        is GenericState.Success -> {
            navigator.popBackStack()
        }
    }
}
