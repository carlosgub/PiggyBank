package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.GetFinanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.Finance
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val getFinanceUseCase: GetFinanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenericState<Finance>>(GenericState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        updateImages()
    }

    private fun updateImages() {
        viewModelScope.launch {
            _uiState.emit(
                getFinanceUseCase.getFinance()
            )
        }
    }
}