package presentation.viewmodel.monthDetail

import core.sealed.GenericState
import domain.usecase.GetExpenseMonthDetailUseCase
import domain.usecase.GetIncomeMonthDetailUseCase
import kotlinx.coroutines.Job
import model.CategoryMonthDetailArgs
import model.FinanceEnum
import model.MonthDetailScreenModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.getCategoryEnumFromName

class CategoryMonthDetailViewModel(
    private val getCategoryMonthDetailUseCase: GetExpenseMonthDetailUseCase,
    private val getIncomeMonthDetailUseCase: GetIncomeMonthDetailUseCase
) : ViewModel(),
    ContainerHost<CategoryMonthDetailScreenState, GenericState<MonthDetailScreenModel>>,
    CategoryMonthDetailScreenIntents {

    override fun getMonthDetail(): Job = intent {
        showLoading()
        val result = if (state.category.type == FinanceEnum.EXPENSE) {
            getCategoryMonthDetailUseCase.getExpenseMonthDetail(
                GetExpenseMonthDetailUseCase.Params(
                    categoryEnum = state.category,
                    monthKey = state.monthKey
                )
            )
        } else {
            getIncomeMonthDetailUseCase.getIncomeMonthDetail(
                GetIncomeMonthDetailUseCase.Params(
                    monthKey = state.monthKey
                )
            )
        }
        when (result) {
            is GenericState.Success -> setMonthDetailScreenModel(result.data)
            else -> Unit
        }

    }

    override fun setInitialConfiguration(args: CategoryMonthDetailArgs): Job = intent {
        reduce {
            state.copy(
                monthKey = args.month,
                category = getCategoryEnumFromName(args.category)
            )
        }
        getMonthDetail()
    }

    private fun setMonthDetailScreenModel(monthDetail: MonthDetailScreenModel): Job = intent {
        reduce {
            state.copy(
                monthDetail = monthDetail,
                showLoading = false,
                isInitialDataLoaded = true
            )
        }
    }

    private fun showLoading(): Job = intent {
        reduce {
            state.copy(
                showLoading = true,
                monthDetail = MonthDetailScreenModel(),
                isInitialDataLoaded = false
            )
        }
    }

    override val container: Container<CategoryMonthDetailScreenState, GenericState<MonthDetailScreenModel>> =
        viewModelScope.container(CategoryMonthDetailScreenState())
}
