package presentation.viewmodel.home

import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import data.repository.impl.FakeFinanceRepositoryImpl
import domain.model.CategoryEnum
import domain.usecase.GetFinanceUseCase
import kotlinx.coroutines.test.runTest
import mock.financeScreenModel
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class HomeViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var getFinanceUseCase = GetFinanceUseCase(fakeFinanceRepositoryImpl)
    private var homeViewModel = HomeViewModel(getFinanceUseCase)

    @Test
    fun `Get Finance Status`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
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
                        financeScreenModel = mock.financeScreenModel,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Set Finance`() =
        runTest {
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.setFinance(financeScreenModel)
                expectState {
                    copy(
                        showLoading = false,
                        financeScreenModel = mock.financeScreenModel,
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
            val expectedCategoryName = CategoryEnum.HOME.name
            homeViewModel.test(this, HomeScreenState()) {
                expectInitialState()
                containerHost.navigateToMonthDetail(expectedCategoryName)
                expectSideEffect(HomeScreenSideEffect.NavigateToMonthDetail(expectedCategoryName))
            }
        }
}
