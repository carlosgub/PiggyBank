package com.carlosgub.myfinances.test.presentation.viewmodel.categorymonthdetailexpense

import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.usecase.GetExpenseMonthDetailUseCase
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense.CategoryMonthDetailExpenseScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense.CategoryMonthDetailExpenseScreenState
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense.CategoryMonthDetailExpenseViewModel
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.expenseScreenModelOne
import com.carlosgub.myfinances.test.mock.monthExpenseDetailScreenModel
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class CategoryMonthDetailViewModelExpenseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var getExpenseMonthDetailUseCase =
        GetExpenseMonthDetailUseCase(fakeFinanceRepositoryImpl)
    private var categoryMonthDetailViewModel =
        CategoryMonthDetailExpenseViewModel(
            getExpenseMonthDetailUseCase = getExpenseMonthDetailUseCase,
        )

    @Test
    fun `Get Months Detail Expense`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailExpenseScreenState()) {
                expectInitialState()
                containerHost.getMonthDetail()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
                expectState {
                    copy(
                        monthDetail = monthExpenseDetailScreenModel,
                        showLoading = false,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Observe Expense`() =
        runTest {
            val state = CategoryMonthDetailExpenseScreenState()
            categoryMonthDetailViewModel.test(
                this,
                state,
            ) {
                expectInitialState()
                containerHost.observeExpense(
                    categoryEnum = state.category,
                    monthKey = state.monthKey,
                )
                expectState {
                    copy(
                        monthDetail = monthExpenseDetailScreenModel,
                        showLoading = false,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Navigate To Edit Expense`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailExpenseScreenState()) {
                expectInitialState()
                containerHost.navigateToEditExpense(expenseScreenModelOne)
                expectSideEffect(
                    CategoryMonthDetailExpenseScreenSideEffect.NavigateToMonthDetail(
                        expenseScreenModelOne,
                    ),
                )
            }
        }

    @Test
    fun `Set Initial Configuration`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailExpenseScreenState()) {
                expectInitialState()
                containerHost.setInitialConfiguration(
                    monthKey = getCurrentMonthKey(),
                    category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK.name,
                )
                expectState {
                    copy(
                        monthKey = getCurrentMonthKey(),
                        category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK,
                    )
                }
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
                expectState {
                    copy(
                        monthDetail = monthExpenseDetailScreenModel,
                        showLoading = false,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Set Month Detail Screen Model`() =
        runTest {
            categoryMonthDetailViewModel.test(
                this,
                CategoryMonthDetailExpenseScreenState(),
            ) {
                expectInitialState()
                containerHost.setMonthDetailScreenModel(monthExpenseDetailScreenModel)
                expectState {
                    copy(
                        monthDetail = monthExpenseDetailScreenModel,
                        showLoading = false,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Show Loading`() =
        runTest {
            categoryMonthDetailViewModel.test(
                this,
                CategoryMonthDetailExpenseScreenState(),
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
}
