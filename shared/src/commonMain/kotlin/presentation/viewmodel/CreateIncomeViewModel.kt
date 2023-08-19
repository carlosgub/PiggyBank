package presentation.viewmodel

import core.result.SingleEvent
import core.sealed.GenericState
import domain.usecase.CreateExpenseUseCase
import domain.usecase.CreateIncomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import utils.toMoneyFormat

class CreateIncomeViewModel(
    private val createIncomeUseCase: CreateIncomeUseCase
) : ViewModel() {
    private val _amountField = MutableStateFlow(0.0.toMoneyFormat())
    val amountField = _amountField.asStateFlow()
    private val _noteField = MutableStateFlow("")
    val noteField = _noteField.asStateFlow()
    private val _showError = MutableStateFlow(false)
    val showError = _showError.asStateFlow()
    private val _amountValueField = MutableStateFlow(0.0)
    private val _uiState =
        MutableStateFlow<SingleEvent<GenericState<Unit>>>(SingleEvent(GenericState.Initial))
    val uiState = _uiState.asStateFlow()

    fun amountFieldChange(textFieldValue: String) {
        if (textFieldValue != _amountField.value) {
            val cleanString: String =
                textFieldValue.replace("""[$,.A-Za-z]""".toRegex(), "").trim().trimStart('0')
            val parsed = if (cleanString.isBlank()) {
                0.0
            } else {
                cleanString.toDouble()
            }
            val amount = parsed / 100

            viewModelScope.launch {
                _amountField.emit(amount.toMoneyFormat())
                _amountValueField.emit(amount)
            }
        }
    }

    fun noteFieldChange(note: String) {
        viewModelScope.launch {
            _noteField.emit(note)
        }
    }

    fun createExpense() {
        if (_amountValueField.value <= 0) {
            showError(true)
        } else {
            viewModelScope.launch {
                _uiState.emit(
                    SingleEvent(
                        createIncomeUseCase.createIncome(
                            CreateIncomeUseCase.Params(
                                amount = (_amountValueField.value * 100).toInt(),
                                note = _noteField.value
                            )
                        )
                    )
                )
            }
        }
    }

    fun showError(show: Boolean) {
        viewModelScope.launch {
            _showError.emit(show)
        }
    }
}