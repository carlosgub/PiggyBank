package presentation.viewmodel.months

import core.sealed.GenericState
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class MonthsScreenViewModel(
    private val getMonthsUseCase: GetMonthsUseCase
) : ViewModel(), ContainerHost<MonthsScreenState, Nothing>,
    MonthsScreenIntents {

    override val container: Container<MonthsScreenState, Nothing> =
        viewModelScope.container(MonthsScreenState()) {
            getMonths()
        }

    override fun getMonths(): Job = intent {
        showLoading()
        delay(200)
        when (val result = getMonthsUseCase.getMonths()) {
            is GenericState.Success -> {
                setMonths(result.data)
            }

            else -> Unit
        }
    }

    private fun setMonths(months: Map<Int, List<LocalDateTime>>): Job = intent {
        reduce {
            state.copy(
                showLoading = false,
                months = months
            )
        }
    }

    private fun showLoading(): Job = intent {
        reduce { state.copy(showLoading = true) }
    }
}
