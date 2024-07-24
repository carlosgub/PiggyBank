package com.carlosgub.myfinances.presentation.viewmodel.editexpense

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.toAmount
import com.carlosgub.myfinances.core.utils.toMillis
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.core.utils.toStringDateFormat
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.usecase.DeleteExpenseUseCase
import com.carlosgub.myfinances.domain.usecase.EditExpenseUseCase
import com.carlosgub.myfinances.domain.usecase.GetExpenseUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class EditExpenseViewModel(
    val editExpenseUseCase: EditExpenseUseCase,
    val deleteExpenseUseCase: DeleteExpenseUseCase,
    val getExpenseUseCase: GetExpenseUseCase,
) : ViewModel(),
    ContainerHost<EditExpenseScreenState, GenericState<Unit>>,
    EditExpenseScreenIntents {
    override fun setCategory(categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum): Job =
        intent {
            reduce { state.copy(category = categoryEnum) }
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
                    editExpense(id = state.id)
                }
            }
        }

    @VisibleForTesting
    fun editExpense(id: Long): Job =
        intent {
            val result =
                editExpenseUseCase(
                    EditExpenseUseCase.Params(
                        amount = state.amount,
                        note = state.note,
                        dateInMillis = state.dateInMillis,
                        id = id,
                        category = state.category.name,
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
                deleteExpenseUseCase(
                    DeleteExpenseUseCase.Params(
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

    override fun getExpense(id: Long): Job =
        intent {
            val result =
                getExpenseUseCase(
                    GetExpenseUseCase.Params(
                        id = id,
                    ),
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
                        monthKey = result.data.monthKey,
                    )
                }
            }
        }

    override val container: Container<EditExpenseScreenState, GenericState<Unit>> =
        viewModelScope.container(EditExpenseScreenState())
}
