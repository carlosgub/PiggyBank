package com.carlosgub.myfinances.test.data.repository.database.impl

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.data.repository.FinanceRepositoryImpl
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.test.data.repository.database.FakeDatabaseFinanceDataSource
import com.carlosgub.myfinances.test.mock.expenseFinanceModelOne
import com.carlosgub.myfinances.test.mock.expenseOne
import com.carlosgub.myfinances.test.mock.financeScreenModelMock
import com.carlosgub.myfinances.test.mock.incomeFinanceModelOne
import com.carlosgub.myfinances.test.mock.monthExpenseDetailScreenModel
import com.carlosgub.myfinances.test.mock.monthIncomeDetailScreenModel
import com.carlosgub.myfinances.test.mock.monthListFiltered
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FinanceRepositoryImplTest {
    private val fakeDatabaseFinanceDataSource = FakeDatabaseFinanceDataSource()
    private val financeRepositoryImpl = FinanceRepositoryImpl(fakeDatabaseFinanceDataSource)

    @Test
    fun `Get Finance success`() =
        runTest {
            val expected = GenericState.Success(financeScreenModelMock)
            financeRepositoryImpl.getFinance(getCurrentMonthKey()).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Get Expense success`() =
        runTest {
            val expected = GenericState.Success(expenseFinanceModelOne)
            val result = financeRepositoryImpl.getExpense(1)
            assertEquals(expected, result)
        }

    @Test
    fun `Get Income success`() =
        runTest {
            val expected = GenericState.Success(incomeFinanceModelOne)
            val result = financeRepositoryImpl.getIncome(1)
            assertEquals(expected, result)
        }

    @Test
    fun `Get Expense Month Detail success`() =
        runTest {
            val expected = GenericState.Success(
                monthExpenseDetailScreenModel,
            )
            financeRepositoryImpl.getExpenseMonthDetail(
                categoryEnum = getCategoryEnumFromName(expenseOne.category),
                monthKey = getCurrentMonthKey(),
            ).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Get Income Month Detail success`() =
        runTest {
            val expected = GenericState.Success(
                monthIncomeDetailScreenModel,
            )
            financeRepositoryImpl.getIncomeMonthDetail(
                monthKey = getCurrentMonthKey(),
            ).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `Get Months success`() =
        runTest {
            val expected = GenericState.Success(
                monthListFiltered,
            )
            financeRepositoryImpl.getMonths().test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
