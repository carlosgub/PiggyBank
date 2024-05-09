package presentation.viewmodel.createincome

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import domain.usecase.CreateIncomeUseCase
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import utils.toMoneyFormat
import utils.toStringDateFormat
import kotlin.test.Test

class CreateIncomeViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var createIncomeUseCase = CreateIncomeUseCase(fakeFinanceRepositoryImpl)
    private var createIncomeViewModel = CreateIncomeViewModel(createIncomeUseCase)

    @Test
    fun `Call Create but show error`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.create()
                expectState {
                    copy(
                        showError = true,
                    )
                }
            }
        }

    @Test
    fun `Call Create success`() =
        runTest {
            val state =
                CreateIncomeScreenState(
                    note = "note",
                    dateInMillis = 1000L,
                    amount = 100.0,
                )
            createIncomeViewModel.test(
                testScope = this,
                initialState = state,
            ) {
                expectInitialState()
                containerHost.create()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
                expectSideEffect(GenericState.Success(Unit))
            }
        }

    @Test
    fun `Show Loading`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.showLoading()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
            }
        }

    @Test
    fun `Call Create Income Function`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.createIncome()
                expectSideEffect(GenericState.Success(Unit))
            }
        }

    @Test
    fun `Set Note`() =
        runTest {
            val setNote = "note"
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.setNote(setNote)
                expectState {
                    copy(
                        note = setNote,
                    )
                }
            }
        }

    @Test
    fun `Set Date`() =
        runTest {
            val date = 1000L
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.setDate(date)
                expectState {
                    copy(
                        dateInMillis = date,
                        date = date.toStringDateFormat(),
                    )
                }
            }
        }

    @Test
    fun `Set Amount`() =
        runTest {
            val amount = "100"
            val amountInteger = amount.toInt() / 100.0
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.setAmount(amount)
                expectState {
                    copy(
                        amount = amountInteger,
                        amountField = amountInteger.toMoneyFormat(),
                    )
                }
            }
        }

    @Test
    fun `Shot Date Error true`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.showDateError(true)
                expectState {
                    copy(
                        showDateError = true,
                    )
                }
            }
        }

    @Test
    fun `Shot Date Error false`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(showDateError = true),
            ) {
                expectInitialState()
                containerHost.showDateError(false)
                expectState {
                    copy(
                        showDateError = false,
                    )
                }
            }
        }

    @Test
    fun `Shot Note Error true`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.showNoteError(true)
                expectState {
                    copy(
                        showNoteError = true,
                    )
                }
            }
        }

    @Test
    fun `Shot Note Error false`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(showNoteError = true),
            ) {
                expectInitialState()
                containerHost.showNoteError(false)
                expectState {
                    copy(
                        showNoteError = false,
                    )
                }
            }
        }

    @Test
    fun `Show Error true`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.showError(true)
                expectState {
                    copy(
                        showError = true,
                    )
                }
            }
        }

    @Test
    fun `Show Error false`() =
        runTest {
            createIncomeViewModel.test(
                this,
                CreateIncomeScreenState(
                    showError = true,
                ),
            ) {
                expectInitialState()
                containerHost.showError(false)
                expectState {
                    copy(
                        showError = false,
                    )
                }
            }
        }
}
