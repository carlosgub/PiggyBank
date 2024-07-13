package com.carlosgub.myfinances.presentation.viewmodel.home

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import domain.model.FinanceScreenModel
import domain.usecase.GetFinanceUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class HomeViewModel(
    private val getFinanceUseCase: GetFinanceUseCase,
) : ViewModel(),
    ContainerHost<HomeScreenState, HomeScreenSideEffect>,
    HomeScreenIntents {
    override fun getFinanceStatus(): Job =
        intent {
            showLoading()
            delay(200)
            getFinanceUseCase(
                GetFinanceUseCase.Params(
                    monthKey = state.monthKey,
                ),
            ).collect { result ->
                when (result) {
                    is GenericState.Success -> setFinance(result.data)
                    else -> Unit
                }
            }
        }

    override val container: Container<HomeScreenState, HomeScreenSideEffect> =
        viewModelScope.container(HomeScreenState()) {
            getFinanceStatus()
        }

    @VisibleForTesting
    fun setFinance(financeScreenModel: FinanceScreenModel): Job =
        intent {
            reduce {
                state.copy(
                    showLoading = false,
                    financeScreenModel = financeScreenModel,
                    isInitialDataLoaded = true,
                )
            }
        }

    @VisibleForTesting
    fun showLoading(): Job =
        intent {
            reduce {
                state.copy(
                    showLoading = true,
                    isInitialDataLoaded = false,
                    financeScreenModel = FinanceScreenModel(),
                )
            }
        }

    override fun setMonthKey(monthKey: String): Job =
        intent {
            reduce { state.copy(monthKey = monthKey) }
        }

    override fun navigateToAddExpense(): Job =
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToAddExpense)
        }

    override fun navigateToAddIncome(): Job =
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToAddIncome)
        }

    override fun navigateToMonthDetail(categoryName: String): Job =
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToMonthDetail(categoryName))
        }

    override fun navigateToMonths(): Job =
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToMonths)
        }
}
