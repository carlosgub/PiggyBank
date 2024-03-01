package presentation.viewmodel

import domain.usecase.DeleteUseCase
import domain.usecase.EditExpenseUseCase
import domain.usecase.EditIncomeUseCase
import kotlinx.coroutines.Job
import model.CategoryEnum
import model.ExpenseScreenModel
import model.FinanceEnum
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.getCategoryEnumFromName
import utils.isExpense
import utils.toLocalDate
import utils.toMillis
import utils.toMoneyFormat
import utils.toNumberOfTwoDigits

class EditViewModel(
    val editExpenseUseCase: EditExpenseUseCase,
    val editIncomeUseCase: EditIncomeUseCase,
    val deleteUseCase: DeleteUseCase
) : ViewModel(), ContainerHost<EditScreenState, EditSideEffects>, EditScreenIntents {

    override fun setCategory(categoryEnum: CategoryEnum): Job = intent {
        reduce { state.copy(category = categoryEnum) }
    }

    override fun setDate(date: Long): Job = intent {
        reduce { state.copy(dateInMillis = date) }
        val localDate = date.toLocalDate()
        val dateFormat = "${localDate.dayOfMonth.toNumberOfTwoDigits()}/" +
                "${localDate.monthNumber.toNumberOfTwoDigits()}/" +
                "${localDate.year}"
        reduce { state.copy(date = dateFormat) }
    }

    override fun setAmount(textFieldValue: String): Job = intent {
        if (textFieldValue != state.amountField) {
            val cleanString: String =
                textFieldValue.replace("""[$,.A-Za-z]""".toRegex(), "").trim().trimStart('0')
            val parsed = if (cleanString.isBlank()) {
                0.0
            } else {
                cleanString.toDouble()
            }
            val amount = parsed / 100

            reduce {
                state.copy(
                    amountField = amount.toMoneyFormat(),
                    amount = amount
                )
            }
        }
    }

    override fun setNote(note: String): Job = intent {
        reduce { state.copy(note = note) }
    }

    override fun create(
        financeEnum: FinanceEnum,
        id: Long
    ): Job = intent {
        if (state.amount <= 0) {
            showError(true)
        } else if (state.dateInMillis == 0L) {
            showDateError(true)
        } else if (state.note.trim().isBlank()) {
            showNoteError(true)
        } else {
            postSideEffect(
                EditSideEffects.Loading
            )
            if (financeEnum.isExpense()) editExpense(id = id) else editIncome(id = id)
        }
    }

    private fun editExpense(id: Long): Job = intent {
        val result = editExpenseUseCase.editExpense(
            EditExpenseUseCase.Params(
                amount = (state.amount * 100).toLong(),
                note = state.note,
                dateInMillis = state.dateInMillis,
                id = id,
                category = state.category.name
            )
        )
        postSideEffect(
            result
        )
    }

    private fun editIncome(id: Long): Job = intent {
        val result = editIncomeUseCase.editIncome(
            EditIncomeUseCase.Params(
                amount = (state.amount * 100).toLong(),
                note = state.note,
                dateInMillis = state.dateInMillis,
                id = id
            )
        )
        postSideEffect(
            result
        )
    }

    override fun showDateError(boolean: Boolean): Job = intent {
        reduce { state.copy(showDateError = boolean) }
    }

    override fun showNoteError(boolean: Boolean): Job = intent {
        reduce { state.copy(showNoteError = boolean) }
    }

    override fun showError(boolean: Boolean): Job = intent {
        reduce { state.copy(showError = boolean) }
    }

    override fun delete(
        id: Long,
        financeEnum: FinanceEnum,
        monthKey: String
    ): Job = intent {
        postSideEffect(EditSideEffects.Loading)
        val result = deleteUseCase.delete(
            DeleteUseCase.Params(
                financeEnum = financeEnum,
                id = id,
                monthKey = monthKey
            )
        )
        postSideEffect(result)
    }

    override fun updateValues(expenseScreenModel: ExpenseScreenModel): Job = intent {
        setAmount(expenseScreenModel.amount.toString())
        setCategory(getCategoryEnumFromName(expenseScreenModel.category))
        setDate(expenseScreenModel.localDateTime.toMillis())
        setNote(expenseScreenModel.note)
        reduce { state.copy(initialDataLoaded = true) }
    }

    override val container: Container<EditScreenState, EditSideEffects> =
        viewModelScope.container(EditScreenState())
}

data class EditScreenState(
    val category: CategoryEnum = CategoryEnum.FOOD,
    val amountField: String = 0.0.toMoneyFormat(),
    val amount: Double = 0.0,
    val showDateError: Boolean = false,
    val showNoteError: Boolean = false,
    val showError: Boolean = false,
    val note: String = "",
    val date: String = "",
    val dateInMillis: Long = 0L,
    val initialDataLoaded: Boolean = false
)

interface EditScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun create(financeEnum: FinanceEnum, id: Long): Job
    fun delete(
        id: Long,
        financeEnum: FinanceEnum,
        monthKey: String
    ): Job

    fun updateValues(
        expenseScreenModel: ExpenseScreenModel
    ): Job
}

sealed class EditSideEffects {
    object Loading : EditSideEffects()
    object Initial : EditSideEffects()
    data class Error(val message: String) : EditSideEffects()
    object Success : EditSideEffects()
    object Delete : EditSideEffects()
}
