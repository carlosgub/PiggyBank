package com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.usecase.GetExpenseMonthDetailUseCase
import com.carlosgub.myfinances.presentation.mapper.MonthDetailExpenseModelToMonthDetailExpenseScreenModel
import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import com.carlosgub.myfinances.presentation.model.MonthDetailExpenseScreenModel
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class CategoryMonthDetailExpenseViewModel(
    private val getExpenseMonthDetailUseCase: GetExpenseMonthDetailUseCase,
) : ViewModel(),
    ContainerHost<CategoryMonthDetailExpenseScreenState, CategoryMonthDetailExpenseScreenSideEffect>,
    CategoryMonthDetailExpenseScreenIntents {
    override fun getMonthDetail(): Job =
        intent {
            showLoading()
            observeExpense(
                categoryEnum = state.category,
                monthKey = state.monthKey,
            )
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
                is GenericState.Success -> setMonthDetailScreenModel(
                    MonthDetailExpenseModelToMonthDetailExpenseScreenModel.map(
                        result.data,
                    ),
                )

                else -> Unit
            }
        }
    }

    override fun navigateToEditExpense(expenseScreenModel: ExpenseScreenModel): Job =
        intent {
            postSideEffect(
                CategoryMonthDetailExpenseScreenSideEffect.NavigateToMonthDetail(
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
    fun setMonthDetailScreenModel(monthDetail: MonthDetailExpenseScreenModel): Job =
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
                    monthDetail = MonthDetailExpenseScreenModel(),
                    isInitialDataLoaded = false,
                )
            }
        }

    override val container: Container<CategoryMonthDetailExpenseScreenState, CategoryMonthDetailExpenseScreenSideEffect> =
        viewModelScope.container(CategoryMonthDetailExpenseScreenState())
}
