package presentation.viewmodel.home

import core.sealed.GenericState
import domain.usecase.GetFinanceUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import model.FinanceScreenModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class HomeViewModel(
    private val getFinanceUseCase: GetFinanceUseCase
) : ViewModel(), ContainerHost<HomeScreenState, Nothing>,
    HomeScreenIntents {

    override fun getFinanceStatus(): Job = intent {
        showLoading()
        delay(200)
        val result = getFinanceUseCase.getFinance(
            GetFinanceUseCase.Params(
                monthKey = state.monthKey
            )
        )
        when (result) {
            is GenericState.Success -> setFinance(result.data)
            else -> Unit
        }
    }

    override val container: Container<HomeScreenState, Nothing> =
        viewModelScope.container(HomeScreenState()) {
            getFinanceStatus()
        }

    private fun setFinance(financeScreenModel: FinanceScreenModel): Job = intent {
        reduce {
            state.copy(
                showLoading = false,
                financeScreenModel = financeScreenModel,
                isInitialDataLoaded = true
            )
        }
    }

    private fun showLoading(): Job = intent {
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
