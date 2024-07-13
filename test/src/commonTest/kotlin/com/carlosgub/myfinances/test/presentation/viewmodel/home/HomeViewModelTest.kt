package com.carlosgub.myfinances.test.presentation.viewmodel.home

import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.usecase.GetFinanceUseCase
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenState
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeViewModel
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.financeScreenModelMock
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class HomeViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var getFinanceUseCase = GetFinanceUseCase(fakeFinanceRepositoryImpl)
    private var homeViewModel = HomeViewModel(getFinanceUseCase)

    @Test
    fun `Get Finance Status`() =
        runTest {
            homeViewModel.test(this) {
                expectInitialState()
                containerHost.getFinanceStatus()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
                expectState {
                    copy(
                        showLoading = false,
                        financeScreenModel = financeScreenModelMock,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Set Finance`() =
        runTest {
            homeViewModel.test(this) {
                expectInitialState()
                containerHost.setFinance(financeScreenModelMock)
                expectState {
                    copy(
                        showLoading = false,
                        financeScreenModel = financeScreenModelMock,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Show Loading`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
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
    fun `Set Month Key`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.setMonthKey(getCurrentMonthKey())
                expectState {
                    copy(
                        monthKey = getCurrentMonthKey(),
                    )
                }
            }
        }

    @Test
    fun `Navigate To Add Expense`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.navigateToAddExpense()
                expectSideEffect(HomeScreenSideEffect.NavigateToAddExpense)
            }
        }

    @Test
    fun `Navigate To Add Income`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.navigateToAddIncome()
                expectSideEffect(HomeScreenSideEffect.NavigateToAddIncome)
            }
        }

    @Test
    fun `Navigate To Months`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.navigateToMonths()
                expectSideEffect(HomeScreenSideEffect.NavigateToMonths)
            }
        }

    @Test
    fun `Navigate To Month Detail`() =
        runTest {
            val expectedCategoryName = com.carlosgub.myfinances.domain.model.CategoryEnum.HOME.name
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.navigateToMonthDetail(expectedCategoryName)
                expectSideEffect(HomeScreenSideEffect.NavigateToMonthDetail(expectedCategoryName))
            }
        }
}
