package presentation.viewmodel.edit

import core.sealed.GenericState
import domain.usecase.DeleteUseCase
import domain.usecase.EditExpenseUseCase
import domain.usecase.EditIncomeUseCase
import domain.usecase.GetOneFinanceUseCase
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
import utils.getCategoryEnumFromName
import utils.isExpense
import utils.toLocalDate
import utils.toMillis
import utils.toMoneyFormat
import utils.toNumberOfTwoDigits

class EditViewModel(
    val editExpenseUseCase: EditExpenseUseCase,
    val editIncomeUseCase: EditIncomeUseCase,
    val deleteUseCase: DeleteUseCase,
    val getOneFinanceUseCase: GetOneFinanceUseCase
) : ViewModel(), ContainerHost<EditScreenState, GenericState<Unit>>, EditScreenIntents {

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

    override fun edit(): Job = intent {
        when {
            state.amount <= 0 -> showError(true)
            state.dateInMillis == 0L -> showDateError(true)
            state.note.trim().isBlank() -> showNoteError(true)
            else -> {
                showLoading()
                if (state.financeEnum.isExpense()) {
                    editExpense(id = state.id)
                } else {
                    editIncome(id = state.id)
                }
            }
        }
    }

    private fun editExpense(id: Long): Job = intent {
        val result = editExpenseUseCase(
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
        val result = editIncomeUseCase(
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

    override fun delete(): Job = intent {
        val result = deleteUseCase(
            DeleteUseCase.Params(
                financeEnum = state.financeEnum,
                id = state.id,
                monthKey = state.monthKey
            )
        )
        postSideEffect(result)
    }

    private fun showLoading(): Job = intent {
        reduce { state.copy(showLoading = true) }
    }

    override fun getFinance(
        id: Long,
        financeEnum: FinanceEnum
    ): Job = intent {
        val result = getOneFinanceUseCase(
            GetOneFinanceUseCase.Params(
                id = id,
                financeEnum = financeEnum
            )
        )
        if (result is GenericState.Success) {
            setAmount(result.data.amount.toString())
            setCategory(getCategoryEnumFromName(result.data.category))
            setDate(result.data.localDateTime.toMillis())
            setNote(result.data.note)
            reduce {
                state.copy(
                    initialDataLoaded = true,
                    id = result.data.id,
                    financeEnum = financeEnum,
                    monthKey = result.data.monthKey
                )
            }
        }
    }

    override val container: Container<EditScreenState, GenericState<Unit>> =
        viewModelScope.container(EditScreenState())
}
