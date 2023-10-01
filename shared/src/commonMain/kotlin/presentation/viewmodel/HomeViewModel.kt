package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetFinanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.FinanceScreenModel
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val getFinanceUseCase: GetFinanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<FinanceScreenModel>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    val isRefreshing: Boolean = (uiState.value is GenericState.Loading)

    fun getFinanceStatus(monthKey: String) {
        _uiState.value = GenericState.Loading
        viewModelScope.launch {
            _uiState.emit(
                getFinanceUseCase.getFinance(
                    GetFinanceUseCase.Params(
                        monthKey = monthKey
                    )
                )
            )
        }
    }
}
