package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetExpenseMonthDetailUseCase
import domain.usecase.GetIncomeMonthDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.CategoryEnum
import model.FinanceEnum
import model.MonthDetailScreenModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class CategoryMonthDetailViewModel(
    private val getCategoryMonthDetailUseCase: GetExpenseMonthDetailUseCase,
    private val getIncomeMonthDetailUseCase: GetIncomeMonthDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<MonthDetailScreenModel>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getMonthDetail(categoryEnum: CategoryEnum, monthKey: String) {
        _uiState.value = GenericState.Loading
        viewModelScope.launch {
            _uiState.emit(
                if(categoryEnum.type == FinanceEnum.EXPENSE){
                    getCategoryMonthDetailUseCase.getExpenseMonthDetail(
                        GetExpenseMonthDetailUseCase.Params(
                            categoryEnum = categoryEnum,
                            monthKey = monthKey
                        )
                    )
                }else{
                    getIncomeMonthDetailUseCase.getIncomeMonthDetail(
                        GetIncomeMonthDetailUseCase.Params(
                            monthKey = monthKey
                        )
                    )
                }

            )
        }
    }
}
