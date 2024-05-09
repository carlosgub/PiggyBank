package presentation.viewmodel.months

import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.monthListFiltered
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import utils.getCurrentMonthKey
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
                        showLoading = true
                    )
                }
                expectState {
                    copy(
                        months = monthListFiltered,
                        showLoading = false
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
                        showLoading = false
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
                        showLoading = true
                    )
                }
            }
        }
}
