package com.carlosgub.myfinances.data.repository

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.data.mock.expenseFinanceModelOne
import com.carlosgub.myfinances.data.mock.expenseOne
import com.carlosgub.myfinances.data.mock.financeScreenModel
import com.carlosgub.myfinances.data.mock.incomeFinanceModelOne
import com.carlosgub.myfinances.data.mock.monthExpenseDetailScreenModel
import com.carlosgub.myfinances.data.mock.monthIncomeDetailScreenModel
import com.carlosgub.myfinances.data.mock.monthListFiltered
import com.carlosgub.myfinances.data.repository.database.impl.FakeDatabaseFinanceDataSource
import domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FinanceRepositoryImplTest {
    private val fakeDatabaseFinanceDataSource = FakeDatabaseFinanceDataSource()
    private val financeRepositoryImpl = FinanceRepositoryImpl(fakeDatabaseFinanceDataSource)

    @Test
    fun `Get Finance success`() =
        runTest {
            val expected = GenericState.Success(financeScreenModel)
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
