package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MonthsScreenViewModel(
    private val getMonthsUseCase: GetMonthsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<Map<Int, List<LocalDateTime>>>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    fun getMonths() {
        _uiState.value = GenericState.Loading
        viewModelScope.launch {
            delay(200)
            _uiState.emit(
                getMonthsUseCase.getMonths()
            )
        }
    }
}
