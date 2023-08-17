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

    val isRefreshing: Boolean = (uiState.value is GenericState.Loading)

    private val _showAddExpenseDialog = MutableStateFlow<Boolean>(false)
    val showAddExpenseDialog = _showAddExpenseDialog.asStateFlow()

    init {
        getFinanceStatus()
    }

    fun getFinanceStatus() {
        viewModelScope.launch {
            _uiState.emit(
                getFinanceUseCase.getFinance()
            )
        }
    }

    fun showAddExpenseDialog(show: Boolean) {
        viewModelScope.launch {
            _showAddExpenseDialog.emit(show)
        }
    }
}