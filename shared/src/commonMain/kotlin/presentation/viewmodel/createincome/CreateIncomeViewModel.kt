package presentation.viewmodel.createincome

import core.sealed.GenericState
import domain.usecase.CreateIncomeUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.toLocalDate
import utils.toMoneyFormat
import utils.toNumberOfTwoDigits

class CreateIncomeViewModel(
    val createIncomeUseCase: CreateIncomeUseCase
) : ViewModel(), ContainerHost<CreateIncomeScreenState, GenericState<Unit>>,
    CreateIncomeScreenIntents {

    override fun create(): Job = intent {
        when {
            state.amount <= 0 -> showError(true)
            state.dateInMillis == 0L -> showDateError(true)
            state.note.trim().isBlank() -> showNoteError(true)
            else -> {
                showLoading()
                createIncome()
            }
        }
    }

    private fun showLoading(): Job = intent {
        reduce { state.copy(showLoading = true) }
    }

    private fun createIncome(): Job = intent {
        val result = createIncomeUseCase(
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

    override val container: Container<CreateIncomeScreenState, GenericState<Unit>> =
        viewModelScope.container(CreateIncomeScreenState())
}
