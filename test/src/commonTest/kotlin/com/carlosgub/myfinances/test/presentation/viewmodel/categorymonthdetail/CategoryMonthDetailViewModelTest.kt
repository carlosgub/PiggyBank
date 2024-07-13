package com.carlosgub.myfinances.test.presentation.viewmodel.categorymonthdetail

import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.usecase.GetExpenseMonthDetailUseCase
import com.carlosgub.myfinances.domain.usecase.GetIncomeMonthDetailUseCase
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail.CategoryMonthDetailScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail.CategoryMonthDetailScreenState
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetail.CategoryMonthDetailViewModel
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.expenseScreenModelOne
import com.carlosgub.myfinances.test.mock.monthExpenseDetailScreenModel
import com.carlosgub.myfinances.test.mock.monthIncomeDetailScreenModel
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class CategoryMonthDetailViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var getExpenseMonthDetailUseCase =
        GetExpenseMonthDetailUseCase(fakeFinanceRepositoryImpl)
    private var getIncomeMonthDetailUseCase = GetIncomeMonthDetailUseCase(fakeFinanceRepositoryImpl)
    private var categoryMonthDetailViewModel =
        CategoryMonthDetailViewModel(
            getExpenseMonthDetailUseCase = getExpenseMonthDetailUseCase,
            getIncomeMonthDetailUseCase = getIncomeMonthDetailUseCase,
        )

    @Test
    fun `Get Months Detail Expense`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailScreenState()) {
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
    fun `Get Months Detail Income`() =
        runTest {
            categoryMonthDetailViewModel.test(
                this,
                CategoryMonthDetailScreenState(
                    category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK,
                ),
            ) {
                expectInitialState()
                containerHost.getMonthDetail()
                expectState {
                    copy(
                        showLoading = true,
                    )
                }
                expectState {
                    copy(
                        monthDetail = monthIncomeDetailScreenModel,
                        showLoading = false,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Observe Expense`() =
        runTest {
            val state = CategoryMonthDetailScreenState()
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
    fun `Observe Income`() =
        runTest {
            val state = CategoryMonthDetailScreenState()
            categoryMonthDetailViewModel.test(
                this,
                state,
            ) {
                expectInitialState()
                containerHost.observeIncome(
                    monthKey = state.monthKey,
                )
                expectState {
                    copy(
                        monthDetail = monthIncomeDetailScreenModel,
                        showLoading = false,
                        isInitialDataLoaded = true,
                    )
                }
            }
        }

    @Test
    fun `Navigate To Edit Expense`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailScreenState()) {
                expectInitialState()
                containerHost.navigateToEditExpense(expenseScreenModelOne)
                expectSideEffect(
                    CategoryMonthDetailScreenSideEffect.NavigateToMonthDetail(
                        expenseScreenModelOne,
                    ),
                )
            }
        }

    @Test
    fun `Set Initial Configuration`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailScreenState()) {
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
                        monthDetail = monthIncomeDetailScreenModel,
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
                CategoryMonthDetailScreenState(),
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
                CategoryMonthDetailScreenState(),
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
