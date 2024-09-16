package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.usecase.GetIncomeMonthDetailUseCase
import com.carlosgub.myfinances.presentation.mapper.MonthDetailIncomeModelToMonthDetailIncomeScreenModel
import com.carlosgub.myfinances.presentation.model.IncomeScreenModel
import com.carlosgub.myfinances.presentation.model.MonthDetailIncomeScreenModel
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class CategoryMonthDetailIncomeViewModel(
    private val getIncomeMonthDetailUseCase: GetIncomeMonthDetailUseCase,
) : ViewModel(),
    ContainerHost<CategoryMonthDetailIncomeScreenState, CategoryMonthDetailIncomeScreenSideEffect>,
    CategoryMonthDetailIncomeScreenIntents {
    override fun getMonthDetail(): Job =
        intent {
            showLoading()
            observeIncome(
                monthKey = state.monthKey,
            )
        }

    @VisibleForTesting
    suspend fun observeIncome(monthKey: String) {
        getIncomeMonthDetailUseCase(
            GetIncomeMonthDetailUseCase.Params(
                monthKey = monthKey,
            ),
        ).collect { result ->
            when (result) {
                is GenericState.Success -> setMonthDetailScreenModel(MonthDetailIncomeModelToMonthDetailIncomeScreenModel.map(result.data))
                else -> Unit
            }
        }
    }

    override fun navigateToEditIncome(incomeScreenModel: IncomeScreenModel): Job =
        intent {
            postSideEffect(
                CategoryMonthDetailIncomeScreenSideEffect.NavigateToMonthDetail(
                    incomeScreenModel,
                ),
            )
        }

    override fun setInitialConfiguration(
        monthKey: String,
        category: String,
    ): Job =
        intent {
            reduce {
                state.copy(
                    monthKey = monthKey,
                    category = getCategoryEnumFromName(category),
                )
            }
            getMonthDetail()
        }

    @VisibleForTesting
    fun setMonthDetailScreenModel(monthDetail: MonthDetailIncomeScreenModel): Job =
        intent {
            reduce {
                state.copy(
                    monthDetail = monthDetail,
                    showLoading = false,
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
                    monthDetail = MonthDetailIncomeScreenModel(),
                    isInitialDataLoaded = false,
                )
            }
        }

    override val container: Container<CategoryMonthDetailIncomeScreenState, CategoryMonthDetailIncomeScreenSideEffect> =
        viewModelScope.container(CategoryMonthDetailIncomeScreenState())
}
