package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.model.ExpenseScreenModel
import com.carlosgub.myfinances.domain.model.FinanceEnum
import com.carlosgub.myfinances.domain.model.MonthDetailScreenModel
import com.carlosgub.myfinances.domain.usecase.GetExpenseMonthDetailUseCase
import com.carlosgub.myfinances.domain.usecase.GetIncomeMonthDetailUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class CategoryMonthDetailViewModel(
    private val getExpenseMonthDetailUseCase: GetExpenseMonthDetailUseCase,
    private val getIncomeMonthDetailUseCase: GetIncomeMonthDetailUseCase,
) : ViewModel(),
    ContainerHost<CategoryMonthDetailScreenState, CategoryMonthDetailScreenSideEffect>,
    CategoryMonthDetailScreenIntents {
    override fun getMonthDetail(): Job =
        intent {
            showLoading()
            if (state.category.type == FinanceEnum.EXPENSE) {
                observeExpense(
                    categoryEnum = state.category,
                    monthKey = state.monthKey,
                )
            } else {
                observeIncome(
                    monthKey = state.monthKey,
                )
            }
        }

    @VisibleForTesting
    suspend fun observeExpense(
        categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum,
        monthKey: String,
    ) {
        getExpenseMonthDetailUseCase(
            GetExpenseMonthDetailUseCase.Params(
                categoryEnum = categoryEnum,
                monthKey = monthKey,
            ),
        ).collect { result ->
            when (result) {
                is GenericState.Success -> setMonthDetailScreenModel(result.data)
                else -> Unit
            }
        }
    }

    @VisibleForTesting
    suspend fun observeIncome(monthKey: String) {
        getIncomeMonthDetailUseCase(
            GetIncomeMonthDetailUseCase.Params(
                monthKey = monthKey,
            ),
        ).collect { result ->
            when (result) {
                is GenericState.Success -> setMonthDetailScreenModel(result.data)
                else -> Unit
            }
        }
    }

    override fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job =
        intent {
            postSideEffect(
                CategoryMonthDetailScreenSideEffect.NavigateToMonthDetail(
                    expenseScreenModel,
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
    fun setMonthDetailScreenModel(monthDetail: MonthDetailScreenModel): Job =
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
                    monthDetail = MonthDetailScreenModel(),
                    isInitialDataLoaded = false,
                )
            }
        }

    override val container: Container<CategoryMonthDetailScreenState, CategoryMonthDetailScreenSideEffect> =
        viewModelScope.container(CategoryMonthDetailScreenState())
}
