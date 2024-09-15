package com.carlosgub.myfinances.presentation.viewmodel.editincome

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.toAmount
import com.carlosgub.myfinances.core.utils.toMillis
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.core.utils.toStringDateFormat
import com.carlosgub.myfinances.domain.usecase.DeleteIncomeUseCase
import com.carlosgub.myfinances.domain.usecase.EditIncomeUseCase
import com.carlosgub.myfinances.domain.usecase.GetIncomeUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class EditIncomeViewModel(
    val editIncomeUseCase: EditIncomeUseCase,
    val deleteUseCase: DeleteIncomeUseCase,
    val getIncomeUseCase: GetIncomeUseCase,
) : ViewModel(),
    ContainerHost<EditIncomeScreenState, GenericState<Unit>>,
    EditIncomeScreenIntents {
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
                val amount = textFieldValue.toAmount()
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
                        amount = state.amount,
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
