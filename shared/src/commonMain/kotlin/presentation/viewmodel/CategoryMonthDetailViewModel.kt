package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetCategoryMonthDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.CategoryEnum
import data.model.Expense
import model.MonthDetailScreenModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class CategoryMonthDetailViewModel(
    private val getCategoryMonthDetailUseCase: GetCategoryMonthDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<MonthDetailScreenModel>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getMonthDetail(categoryEnum: CategoryEnum, monthKey: String) {
        _uiState.value = GenericState.Loading
        viewModelScope.launch {
            _uiState.emit(
                getCategoryMonthDetailUseCase.getCategoryMonthDetail(
                    GetCategoryMonthDetailUseCase.Params(
                        categoryEnum = categoryEnum,
                        monthKey = monthKey
                    )
                )
            )
        }
    }
}