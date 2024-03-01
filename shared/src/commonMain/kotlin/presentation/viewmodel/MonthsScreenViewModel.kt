package presentation.viewmodel

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
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class MonthsScreenViewModel(
    private val getMonthsUseCase: GetMonthsUseCase
) : ViewModel(), ContainerHost<MonthsScreenState, GenericState<Map<Int, List<LocalDateTime>>>>,
    MonthsScreenIntents {

    override val container: Container<MonthsScreenState, GenericState<Map<Int, List<LocalDateTime>>>> =
        viewModelScope.container(MonthsScreenState())

    override fun getMonths(): Job = intent {
        delay(200)
        postSideEffect(
            getMonthsUseCase.getMonths()
        )
    }

    override fun setMonths(months: Map<Int, List<LocalDateTime>>): Job = intent {
        reduce {
            state.copy(
                showLoading = false,
                months = months
            )
        }
    }

    override fun showLoading(): Job = intent {
        reduce { state.copy(showLoading = true) }
    }
}

data class MonthsScreenState(
    val months: Map<Int, List<LocalDateTime>> = mapOf(),
    val showLoading: Boolean = false
)

interface MonthsScreenIntents {
    fun getMonths(): Job
    fun setMonths(months: Map<Int, List<LocalDateTime>>): Job
    fun showLoading(): Job
}
