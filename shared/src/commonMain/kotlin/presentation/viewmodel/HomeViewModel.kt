package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetFinanceUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.CategoryEnum
import model.FinanceEnum
import model.FinanceScreenModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.toMoneyFormat

class HomeViewModel(
    private val getFinanceUseCase: GetFinanceUseCase
) : ViewModel(), ContainerHost<HomeScreenState, GenericState<FinanceScreenModel>>,
    HomeScreenIntents {

    override fun getFinanceStatus(): Job = intent {
        postSideEffect(GenericState.Loading)
        delay(300)
        val result = getFinanceUseCase.getFinance(
            GetFinanceUseCase.Params(
                monthKey = state.monthKey
            )
        )
        postSideEffect(result)
    }

    override val container: Container<HomeScreenState, GenericState<FinanceScreenModel>> =
        viewModelScope.container(HomeScreenState())

    override fun setFinance(financeScreenModel: FinanceScreenModel): Job = intent {
        reduce {
            state.copy(
                showLoading = false,
                financeScreenModel = financeScreenModel,
                isInitialDataLoaded = true
            )
        }
    }

    override fun showLoading(): Job = intent {
        reduce {
            state.copy(
                showLoading = true,
                isInitialDataLoaded = false,
                financeScreenModel = FinanceScreenModel()
            )
        }
    }

    override fun setMonthKey(monthKey: String): Job = intent {
        reduce { state.copy(monthKey = monthKey) }
    }
}


data class HomeScreenState(
    val financeScreenModel: FinanceScreenModel = FinanceScreenModel(),
    val showLoading: Boolean = false,
    val monthKey: String = "",
    val isInitialDataLoaded: Boolean = false
)

interface HomeScreenIntents {
    fun setFinance(financeScreenModel: FinanceScreenModel): Job
    fun showLoading(): Job
    fun getFinanceStatus(): Job
    fun setMonthKey(monthKey: String): Job
}
