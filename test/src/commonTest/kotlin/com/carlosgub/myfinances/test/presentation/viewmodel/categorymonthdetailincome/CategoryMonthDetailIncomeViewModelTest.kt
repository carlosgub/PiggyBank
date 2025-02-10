package com.carlosgub.myfinances.test.presentation.viewmodel.categorymonthdetailincome

import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.usecase.GetIncomeMonthDetailUseCase
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeScreenSideEffect
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeScreenState
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeViewModel
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.incomeScreenModelOne
import com.carlosgub.myfinances.test.mock.monthIncomeDetailScreenModel
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class CategoryMonthDetailIncomeViewModelTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private var getIncomeMonthDetailUseCase = GetIncomeMonthDetailUseCase(fakeFinanceRepositoryImpl)
    private var categoryMonthDetailViewModel =
        CategoryMonthDetailIncomeViewModel(
            getIncomeMonthDetailUseCase = getIncomeMonthDetailUseCase,
        )

    @Test
    fun `Get Months Detail Income`() =
        runTest {
            categoryMonthDetailViewModel.test(
                this,
                CategoryMonthDetailIncomeScreenState(
                    category = CategoryEnum.WORK,
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
    fun `Observe Income`() =
        runTest {
            val state = CategoryMonthDetailIncomeScreenState()
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
    fun `Navigate To Edit Income`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailIncomeScreenState()) {
                expectInitialState()
                containerHost.navigateToEditIncome(incomeScreenModelOne)
                expectSideEffect(
                    CategoryMonthDetailIncomeScreenSideEffect.NavigateToMonthDetail(
                        incomeScreenModelOne,
                    ),
                )
            }
        }

    @Test
    fun `Set Initial Configuration`() =
        runTest {
            categoryMonthDetailViewModel.test(this, CategoryMonthDetailIncomeScreenState()) {
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
                CategoryMonthDetailIncomeScreenState(),
            ) {
                expectInitialState()
                containerHost.setMonthDetailScreenModel(monthIncomeDetailScreenModel)
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
    fun `Show Loading`() =
        runTest {
            categoryMonthDetailViewModel.test(
                this,
                CategoryMonthDetailIncomeScreenState(),
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
