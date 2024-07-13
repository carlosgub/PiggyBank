package com.carlosgub.myfinances.test.presentation.viewmodel.months

import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsScreenState
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsViewModel
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.monthListFiltered
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class MonthsViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var getMonthsUseCase = GetMonthsUseCase(fakeFinanceRepositoryImpl)
    private var monthViewModel = MonthsViewModel(getMonthsUseCase)

    @Test
    fun `Get Months`() =
        runTest {
            monthViewModel.test(this, MonthsScreenState()) {
                expectInitialState()
                containerHost.getMonths()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
                expectState {
                    copy(
                        months = monthListFiltered,
                        showLoading = false,
                    )
                }
            }
        }

    @Test
    fun `Navigate to Month Detail`() =
        runTest {
            monthViewModel.test(this, MonthsScreenState()) {
                expectInitialState()
                containerHost.navigateToMonthDetail(getCurrentMonthKey())
                expectSideEffect(MonthsScreenSideEffect.NavigateToMonthDetail(getCurrentMonthKey()))
            }
        }

    @Test
    fun `Set Months`() =
        runTest {
            monthViewModel.test(this, MonthsScreenState()) {
                expectInitialState()
                containerHost.setMonths(monthListFiltered)
                expectState {
                    copy(
                        months = monthListFiltered,
                        showLoading = false,
                    )
                }
            }
        }

    @Test
    fun `Show Loading`() =
        runTest {
            monthViewModel.test(this, MonthsScreenState()) {
                expectInitialState()
                containerHost.showLoading()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
            }
        }
}
