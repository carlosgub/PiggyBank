package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetCategoryMonthDetailUseCase
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.CategoryEnum
import model.MonthDetailScreenModel
import model.MonthModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MonthsScreenViewModel(
    private val getMonthsUseCase: GetMonthsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<List<MonthModel>>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getMonths() {
        _uiState.value = GenericState.Loading
        viewModelScope.launch {
            _uiState.emit(
                getMonthsUseCase.getMonths()
            )
        }
    }
}
