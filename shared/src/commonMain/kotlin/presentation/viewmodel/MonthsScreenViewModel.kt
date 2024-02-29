package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import model.CategoryEnum
import model.FinanceEnum
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import utils.toMoneyFormat

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
}

data class MonthsScreenState(
    val months: Map<Int, List<LocalDateTime>> = mapOf()
)

interface MonthsScreenIntents {
    fun getMonths(): Job
}
