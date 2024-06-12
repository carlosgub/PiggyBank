package presentation.viewmodel.createexpense

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.core.utils.toStringDateFormat
import domain.model.CategoryEnum
import domain.usecase.CreateExpenseUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class CreateExpenseViewModel(
    val createExpenseUseCase: CreateExpenseUseCase,
) : ViewModel(),
    ContainerHost<CreateExpenseScreenState, GenericState<Unit>>,
    CreateExpenseScreenIntents {
    override fun create(): Job =
        intent {
            when {
                state.amount <= 0 -> showError(true)
                state.dateInMillis == 0L -> showDateError(true)
                state.note.trim().isBlank() -> showNoteError(true)
                else -> {
                    showLoading()
                    createExpense()
                }
            }
        }

    @VisibleForTesting
    fun showLoading(): Job =
        intent {
            reduce { state.copy(showLoading = true) }
        }

    @VisibleForTesting
    fun createExpense(): Job =
        intent {
            val result =
                createExpenseUseCase(
                    CreateExpenseUseCase.Params(
                        amount = (state.amount * 100).toInt(),
                        note = state.note,
                        dateInMillis = state.dateInMillis,
                        category = state.category.name,
                    ),
                )
            postSideEffect(
                result,
            )
        }

    override fun setNote(note: String): Job =
        intent {
            reduce { state.copy(note = note) }
        }

    override fun setDate(date: Long): Job =
        intent {
            reduce {
                state.copy(
                    dateInMillis = date,
                    date = date.toStringDateFormat(),
                )
            }
        }

    override fun setCategory(categoryEnum: CategoryEnum): Job =
        intent {
            reduce { state.copy(category = categoryEnum) }
        }

    override fun setAmount(textFieldValue: String): Job =
        intent {
            if (textFieldValue != state.amountField) {
                val cleanString: String =
                    textFieldValue.replace("""[$,.A-Za-z]""".toRegex(), "").trim().trimStart('0')
                val parsed =
                    if (cleanString.isBlank()) {
                        0.0
                    } else {
                        cleanString.toDouble()
                    }
                val amount = parsed / 100

                reduce {
                    state.copy(
                        amountField = amount.toMoneyFormat(),
                        amount = amount,
                    )
                }
            }
        }

    override fun showDateError(boolean: Boolean): Job =
        intent {
            reduce { state.copy(showDateError = boolean) }
        }

    override fun showNoteError(boolean: Boolean): Job =
        intent {
            reduce { state.copy(showNoteError = boolean) }
        }

    override fun showError(boolean: Boolean): Job =
        intent {
            reduce { state.copy(showError = boolean) }
        }

    override val container: Container<CreateExpenseScreenState, GenericState<Unit>> =
        viewModelScope.container(CreateExpenseScreenState())
}
