package com.carlosgub.myfinances.presentation.viewmodel.createexpense

import androidx.annotation.VisibleForTesting
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.toAmount
import com.carlosgub.myfinances.core.utils.toMoneyFormat
import com.carlosgub.myfinances.core.utils.toStringDateFormat
import com.carlosgub.myfinances.domain.usecase.CreateExpenseUseCase
import kotlinx.coroutines.Job
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

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
                        amount = state.amount,
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

    override fun setCategory(categoryEnum: com.carlosgub.myfinances.domain.model.CategoryEnum): Job =
        intent {
            reduce { state.copy(category = categoryEnum) }
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
