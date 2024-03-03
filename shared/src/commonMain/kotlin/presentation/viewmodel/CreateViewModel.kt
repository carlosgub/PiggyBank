package presentation.viewmodel

import core.sealed.GenericState
import domain.usecase.CreateExpenseUseCase
import domain.usecase.CreateIncomeUseCase
import kotlinx.coroutines.Job
import model.CategoryEnum
import model.FinanceEnum
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.isExpense
import utils.toLocalDate
import utils.toMoneyFormat
import utils.toNumberOfTwoDigits

class CreateViewModel(
    val createExpenseUseCase: CreateExpenseUseCase,
    val createIncomeUseCase: CreateIncomeUseCase
) : ViewModel(), ContainerHost<CreateScreenState, GenericState<Unit>>, CreateScreenIntents {


    override fun create(financeEnum: FinanceEnum): Job = intent {
        if (state.amount <= 0) {
            showError(true)
        } else if (state.dateInMillis == 0L) {
            showDateError(true)
        } else if (state.note.trim().isBlank()) {
            showNoteError(true)
        } else {
            showLoading()
            if (financeEnum.isExpense()) createExpense() else createIncome()
        }
    }


    private fun showLoading(): Job = intent {
        reduce { state.copy(showLoading = true) }
    }

    private fun createExpense(): Job = intent {
        val result = createExpenseUseCase.createExpense(
            CreateExpenseUseCase.Params(
                amount = (state.amount * 100).toInt(),
                note = state.note,
                dateInMillis = state.dateInMillis,
                category = state.category.name
            )
        )
        postSideEffect(
            result
        )
    }

    private fun createIncome(): Job = intent {
        val result = createIncomeUseCase.createIncome(
            CreateIncomeUseCase.Params(
                amount = (state.amount * 100).toInt(),
                note = state.note,
                dateInMillis = state.dateInMillis
            )
        )
        postSideEffect(
            result
        )
    }

    override fun setNote(note: String): Job = intent {
        reduce { state.copy(note = note) }
    }

    override fun setDate(date: Long): Job = intent {
        reduce { state.copy(dateInMillis = date) }
        val localDate = date.toLocalDate()
        val dateFormat = "${localDate.dayOfMonth.toNumberOfTwoDigits()}/" +
                "${localDate.monthNumber.toNumberOfTwoDigits()}/" +
                "${localDate.year}"
        reduce { state.copy(date = dateFormat) }
    }

    override fun setCategory(categoryEnum: CategoryEnum): Job = intent {
        reduce { state.copy(category = categoryEnum) }
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

    override fun showDateError(boolean: Boolean): Job = intent {
        reduce { state.copy(showDateError = boolean) }
    }

    override fun showNoteError(boolean: Boolean): Job = intent {
        reduce { state.copy(showNoteError = boolean) }
    }

    override fun showError(boolean: Boolean): Job = intent {
        reduce { state.copy(showError = boolean) }
    }

    override val container: Container<CreateScreenState, GenericState<Unit>> =
        viewModelScope.container(CreateScreenState())
}

data class CreateScreenState(
    val category: CategoryEnum = CategoryEnum.FOOD,
    val amountField: String = 0.0.toMoneyFormat(),
    val amount: Double = 0.0,
    val showDateError: Boolean = false,
    val showNoteError: Boolean = false,
    val showError: Boolean = false,
    val note: String = "",
    val date: String = "",
    val dateInMillis: Long = 0L,
    val showLoading: Boolean = false
)

interface CreateScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun create(financeEnum: FinanceEnum): Job
}