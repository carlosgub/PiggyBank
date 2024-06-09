package presentation.screen.editincome.content

import com.carlosgub.myfinances.core.state.GenericState
import moe.tlaster.precompose.navigation.Navigator

fun editIncomeObserver(
    sideEffect: GenericState<Unit>,
    navigator: Navigator,
) {
    when (sideEffect) {
        is GenericState.Success -> navigator.popBackStack()
        else -> Unit
    }
}
