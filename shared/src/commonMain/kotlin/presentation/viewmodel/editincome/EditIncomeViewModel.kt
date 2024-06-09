package presentation.viewmodel.editincome

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import domain.usecase.DeleteIncomeUseCase
import domain.usecase.EditIncomeUseCase
import domain.usecase.GetIncomeUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import utils.toMillis
import utils.toMoneyFormat
import utils.toStringDateFormat

class EditIncomeViewModel(
    val editIncomeUseCase: EditIncomeUseCase,
    val deleteUseCase: DeleteIncomeUseCase,
    val getIncomeUseCase: GetIncomeUseCase,
) : ViewModel(), ContainerHost<EditIncomeScreenState, GenericState<Unit>>, EditIncomeScreenIntents {
    override fun setDate(date: Long): Job =
        intent {
            reduce {
                state.copy(
                    dateInMillis = date,
                    date = date.toStringDateFormat(),
                )
            }
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

    override fun setNote(note: String): Job =
        intent {
            reduce { state.copy(note = note) }
        }

    override fun edit(): Job =
        intent {
            when {
                state.amount <= 0 -> showError(true)
                state.dateInMillis == 0L -> showDateError(true)
                state.note.trim().isBlank() -> showNoteError(true)
                else -> {
                    showLoading()
                    editIncome(id = state.id)
                }
            }
        }

    @VisibleForTesting
    fun editIncome(id: Long): Job =
        intent {
            val result =
                editIncomeUseCase(
                    EditIncomeUseCase.Params(
                        amount = (state.amount * 100).toLong(),
                        note = state.note,
                        dateInMillis = state.dateInMillis,
                        id = id,
                    ),
                )
            postSideEffect(
                result,
            )
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

    override fun delete(): Job =
        intent {
            val result =
                deleteUseCase(
                    DeleteIncomeUseCase.Params(
                        id = state.id,
                        monthKey = state.monthKey,
                    ),
                )
            postSideEffect(result)
        }

    @VisibleForTesting
    fun showLoading(): Job =
        intent {
            reduce { state.copy(showLoading = true) }
        }

    override fun getIncome(id: Long): Job =
        intent {
            val result =
                getIncomeUseCase(
                    GetIncomeUseCase.Params(
                        id = id,
                    ),
                )
            if (result is GenericState.Success) {
                setAmount(result.data.amount.toString())
                setDate(result.data.localDateTime.toMillis())
                setNote(result.data.note)
                reduce {
                    state.copy(
                        initialDataLoaded = true,
                        id = result.data.id,
                        monthKey = result.data.monthKey,
                    )
                }
            }
        }

    override val container: Container<EditIncomeScreenState, GenericState<Unit>> =
        viewModelScope.container(EditIncomeScreenState())
}
