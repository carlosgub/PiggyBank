package presentation.viewmodel.editexpense

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.expenseFinanceModelOne
import domain.model.CategoryEnum
import domain.usecase.DeleteExpenseUseCase
import domain.usecase.EditExpenseUseCase
import domain.usecase.GetExpenseUseCase
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import utils.getCategoryEnumFromName
import utils.toMillis
import utils.toMoneyFormat
import utils.toStringDateFormat
import kotlin.test.Test


class EditExpenseViewModelTest {

    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var editExpenseUseCase = EditExpenseUseCase(fakeFinanceRepositoryImpl)
    private var getExpenseUseCase = GetExpenseUseCase(fakeFinanceRepositoryImpl)
    private var deleteExpenseUseCase = DeleteExpenseUseCase(fakeFinanceRepositoryImpl)
    private var editExpenseViewModel = EditExpenseViewModel(
        editExpenseUseCase = editExpenseUseCase,
        getExpenseUseCase = getExpenseUseCase,
        deleteExpenseUseCase = deleteExpenseUseCase
    )

    @Test
    fun `Call Create but show error`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.edit()
            expectState {
                copy(
                    showError = true
                )
            }
        }
    }

    @Test
    fun `Call Create success`() = runTest {
        val state = EditExpenseScreenState(
            note = "note",
            dateInMillis = 1000L,
            amount = 100.0
        )
        editExpenseViewModel.test(this, state) {
            expectInitialState()
            containerHost.edit()
            expectState {
                copy(
                    showLoading = true
                )
            }
            expectSideEffect(GenericState.Success(Unit))
        }
    }

    @Test
    fun `Show Loading`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.showLoading()
            expectState {
                copy(
                    showLoading = true
                )
            }
        }
    }

    @Test
    fun `Call Create Expense Function`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.editExpense(1L)
            expectSideEffect(GenericState.Success(Unit))
        }
    }

    @Test
    fun `Set Note`() = runTest {
        val setNote = "note"
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.setNote(setNote)
            expectState {
                copy(
                    note = setNote
                )
            }
        }
    }

    @Test
    fun `Set Date`() = runTest {
        val date = 1000L
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.setDate(date)
            expectState {
                copy(
                    dateInMillis = date,
                    date = date.toStringDateFormat()
                )
            }
        }
    }

    @Test
    fun `Set Category`() = runTest {
        val category = CategoryEnum.CLOTHES
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.setCategory(category)
            expectState {
                copy(
                    category = category
                )
            }
        }
    }

    @Test
    fun `Set Amount`() = runTest {
        val amount = "100"
        val amountInteger = amount.toInt() / 100.0
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.setAmount(amount)
            expectState {
                copy(
                    amount = amountInteger,
                    amountField = amountInteger.toMoneyFormat()
                )
            }
        }
    }

    @Test
    fun `Shot Date Error true`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.showDateError(true)
            expectState {
                copy(
                    showDateError = true
                )
            }
        }
    }

    @Test
    fun `Shot Date Error false`() = runTest {
        editExpenseViewModel.test(
            this,
            EditExpenseScreenState(showDateError = true)
        ) {
            expectInitialState()
            containerHost.showDateError(false)
            expectState {
                copy(
                    showDateError = false
                )
            }
        }
    }

    @Test
    fun `Shot Note Error true`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.showNoteError(true)
            expectState {
                copy(
                    showNoteError = true
                )
            }
        }
    }

    @Test
    fun `Shot Note Error false`() = runTest {
        editExpenseViewModel.test(
            this,
            EditExpenseScreenState(
                showNoteError = true
            )
        ) {
            expectInitialState()
            containerHost.showNoteError(false)
            expectState {
                copy(
                    showNoteError = false
                )
            }
        }
    }

    @Test
    fun `Show Error true`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.showError(true)
            expectState {
                copy(
                    showError = true
                )
            }
        }
    }

    @Test
    fun `Show Error false`() = runTest {
        editExpenseViewModel.test(
            this,
            EditExpenseScreenState(showError = true)
        ) {
            expectInitialState()
            containerHost.showError(false)
            expectState {
                copy(
                    showError = false
                )
            }
        }
    }

    @Test
    fun `Delete Expense`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            expectInitialState()
            containerHost.delete()
            expectSideEffect(GenericState.Success(Unit))
        }
    }

    @Test
    fun `Get Expense`() = runTest {
        editExpenseViewModel.test(this, EditExpenseScreenState()) {
            val amount = expenseFinanceModelOne.amount / 100.0
            expectInitialState()
            containerHost.getExpense(expenseFinanceModelOne.id)
            expectState {
                copy(
                    initialDataLoaded = true,
                    id = expenseFinanceModelOne.id,
                    monthKey = expenseFinanceModelOne.monthKey,
                )
            }
            expectState {
                copy(
                    amountField = amount.toMoneyFormat(),
                    amount = amount
                )
            }
            expectState {
                copy(
                    category = getCategoryEnumFromName(expenseFinanceModelOne.category)
                )
            }
            expectState {
                copy(
                    dateInMillis = expenseFinanceModelOne.localDateTime.toMillis(),
                    date = expenseFinanceModelOne.localDateTime.toMillis().toStringDateFormat()
                )
            }
            expectState {
                copy(
                    note = expenseFinanceModelOne.note
                )
            }
        }
    }
}
