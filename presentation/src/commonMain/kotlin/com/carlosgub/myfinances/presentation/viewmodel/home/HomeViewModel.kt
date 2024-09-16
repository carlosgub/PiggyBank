package com.carlosgub.myfinances.presentation.viewmodel.home

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.model.FinanceEnum
import com.carlosgub.myfinances.domain.usecase.GetFinanceUseCase
import com.carlosgub.myfinances.presentation.mapper.FinanceModelToFinanceScreenModel
import com.carlosgub.myfinances.presentation.model.FinanceScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

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
                    is GenericState.Success -> {
                        val financeScreenModel = FinanceModelToFinanceScreenModel.map(result.data)
                        setFinance(financeScreenModel)
                    }

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

    override fun navigateToMonthDetail(category: CategoryEnum): Job =
        intent {
            if(category.type==FinanceEnum.EXPENSE){
                postSideEffect(HomeScreenSideEffect.NavigateToMonthExpenseDetail(category.name))
            }else{
                postSideEffect(HomeScreenSideEffect.NavigateToMonthIncomeDetail(category.name))
            }
        }

    override fun navigateToMonths(): Job =
        intent {
            postSideEffect(HomeScreenSideEffect.NavigateToMonths)
        }
}
