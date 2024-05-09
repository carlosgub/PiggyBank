package presentation.viewmodel.categorymonthdetail

import androidx.annotation.VisibleForTesting
import core.sealed.GenericState
import domain.model.CategoryEnum
import domain.model.ExpenseScreenModel
import domain.model.FinanceEnum
import domain.model.MonthDetailScreenModel
import domain.usecase.GetExpenseMonthDetailUseCase
import domain.usecase.GetIncomeMonthDetailUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.getCategoryEnumFromName

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
        categoryEnum: CategoryEnum,
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
