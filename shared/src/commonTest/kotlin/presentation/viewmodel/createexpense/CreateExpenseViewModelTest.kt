package presentation.viewmodel.createexpense

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import domain.model.CategoryEnum
import domain.usecase.CreateExpenseUseCase
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import utils.toMoneyFormat
import utils.toStringDateFormat
import kotlin.test.Test

class CreateExpenseViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var createExpenseUseCase = CreateExpenseUseCase(fakeFinanceRepositoryImpl)
    private var createExpenseViewModel = CreateExpenseViewModel(createExpenseUseCase)

    @Test
    fun `Call Create but show error`() =
        runTest {
            createExpenseViewModel.test(this, CreateExpenseScreenState()) {
                expectInitialState()
                containerHost.create()
                expectState {
                    copy(
                        showError = true
                    )
                }
            }
        }

    @Test
    fun `Call Create success`() =
        runTest {
            val state =
                CreateExpenseScreenState(
                    note = "note",
                    dateInMillis = 1000L,
                    amount = 100.0
                )
            createExpenseViewModel.test(
                this,
                state
            ) {
                expectInitialState()
                containerHost.create()
                expectState {
                    copy(
                        showLoading = true
                    )
                }
                expectSideEffect(GenericState.Success(Unit))
            }
        }

    @Test
    fun `Show Loading`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Call Create Expense Function`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
                expectInitialState()
                containerHost.createExpense()
                expectSideEffect(GenericState.Success(Unit))
            }
        }

    @Test
    fun `Set Note`() =
        runTest {
            val setNote = "note"
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Set Date`() =
        runTest {
            val date = 1000L
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Set Category`() =
        runTest {
            val category = CategoryEnum.CLOTHES
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Set Amount`() =
        runTest {
            val amount = "100"
            val amountInteger = amount.toInt() / 100.0
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Shot Date Error true`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Shot Date Error false`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState(
                    showDateError = true
                )
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
    fun `Shot Note Error true`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Shot Note Error false`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState(
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
    fun `Show Error true`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState()
            ) {
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
    fun `Show Error false`() =
        runTest {
            createExpenseViewModel.test(
                this,
                CreateExpenseScreenState(
                    showError = true
                )
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
}
