package presentation.viewmodel

import core.result.SingleEvent
import domain.usecase.DeleteUseCase
import domain.usecase.EditExpenseUseCase
import domain.usecase.EditIncomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import model.CategoryEnum
import model.FinanceEnum
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import presentation.viewmodel.state.EditState
import utils.isExpense
import utils.toLocalDate
import utils.toMoneyFormat
import utils.toNumberOfTwoDigits

class EditViewModel(
    val editExpenseUseCase: EditExpenseUseCase,
    val editIncomeUseCase: EditIncomeUseCase,
    val deleteUseCase: DeleteUseCase
) : ViewModel() {

    private val _category = MutableStateFlow(CategoryEnum.FOOD)
    val category = _category.asStateFlow()
    private val _dateInMillis = MutableStateFlow(0L)
    private val _amountField = MutableStateFlow(0.0.toMoneyFormat())
    val amountField = _amountField.asStateFlow()
    private val _noteField = MutableStateFlow("")
    val noteField = _noteField.asStateFlow()
    private val _dateValue = MutableStateFlow("")
    val dateValue = _dateValue.asStateFlow()
    private val _showError = MutableStateFlow(false)
    val showError = _showError.asStateFlow()
    private val _showNoteError = MutableStateFlow(false)
    val showNoteError = _showNoteError.asStateFlow()
    private val _showDateError = MutableStateFlow(false)
    val showDateError = _showDateError.asStateFlow()
    private val _amountValueField = MutableStateFlow(0.0)
    private val _uiState =
        MutableStateFlow<SingleEvent<EditState>>(SingleEvent(EditState.Initial))
    val uiState = _uiState.asStateFlow()

    fun setCategory(categoryEnum: CategoryEnum) {
        viewModelScope.launch {
            _category.emit(categoryEnum)
        }
    }

    fun setDateInMillis(dateInMillis: Long) {
        viewModelScope.launch {
            _dateInMillis.value = dateInMillis
        }
        setDateValue()
    }

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

    fun create(
        financeEnum: FinanceEnum,
        id: String
    ) {
        if (_amountValueField.value <= 0) {
            showError(true)
        } else if (_dateInMillis.value == 0L) {
            showDateError(true)
        } else if (_noteField.value.trim().isBlank()) {
            showNoteError(true)
        } else {
            _uiState.value = SingleEvent(EditState.Loading)
            if (financeEnum.isExpense()) editExpense(id = id) else editIncome(id = id)
        }
    }

    private fun editExpense(id: String) {
        viewModelScope.launch {
            _uiState.emit(
                SingleEvent(
                    editExpenseUseCase.editExpense(
                        EditExpenseUseCase.Params(
                            amount = (_amountValueField.value * 100).toInt(),
                            category = _category.value.name,
                            note = _noteField.value,
                            dateInMillis = _dateInMillis.value,
                            id = id
                        )
                    )
                )
            )
        }
    }

    private fun editIncome(id: String) {
        viewModelScope.launch {
            _uiState.emit(
                SingleEvent(
                    editIncomeUseCase.editIncome(
                        EditIncomeUseCase.Params(
                            amount = (_amountValueField.value * 100).toInt(),
                            note = _noteField.value,
                            dateInMillis = _dateInMillis.value,
                            id = id
                        )
                    )
                )
            )
        }
    }

    fun showError(show: Boolean) {
        viewModelScope.launch {
            _showError.emit(show)
        }
    }

    fun showNoteError(show: Boolean) {
        viewModelScope.launch {
            _showNoteError.emit(show)
        }
    }

    fun showDateError(show: Boolean) {
        viewModelScope.launch {
            _showDateError.emit(show)
        }
    }

    private fun setDateValue() {
        val date = _dateInMillis.value.toLocalDate()
        _dateValue.value =
            "${date.dayOfMonth.toNumberOfTwoDigits()}/" +
                    "${date.monthNumber.toNumberOfTwoDigits()}/" +
                    "${date.year}"
    }

    fun delete(
        id: String,
        financeEnum: FinanceEnum
    ) {
        _uiState.value = SingleEvent(EditState.Loading)
        viewModelScope.launch {
            _uiState.emit(
                SingleEvent(
                    deleteUseCase.delete(
                        DeleteUseCase.Params(
                            financeEnum = financeEnum,
                            id = id
                        )
                    )
                )
            )
        }
    }
}
