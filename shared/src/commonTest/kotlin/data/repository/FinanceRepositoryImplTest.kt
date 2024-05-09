package data.repository

import app.cash.turbine.test
import core.sealed.GenericState
import data.repository.source.database.expenseFinanceModelOne
import data.repository.source.database.expenseOne
import data.repository.source.database.financeScreenModel
import data.repository.source.database.impl.FakeDatabaseFinanceDataSource
import data.repository.source.database.incomeFinanceModelOne
import data.repository.source.database.monthExpenseDetailScreenModel
import data.repository.source.database.monthIncomeDetailScreenModel
import data.repository.source.database.monthListFiltered
import kotlinx.coroutines.test.runTest
import utils.getCategoryEnumFromName
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class FinanceRepositoryImplTest {
    private val fakeDatabaseFinanceDataSource = FakeDatabaseFinanceDataSource()
    private val financeRepositoryImpl = FinanceRepositoryImpl(fakeDatabaseFinanceDataSource)

    @Test
    fun `Get Finance success`() =
        runTest {
            val expected = financeScreenModel
            financeRepositoryImpl.getFinance(getCurrentMonthKey()).test {
                assertEquals(expected, (awaitItem() as GenericState.Success).data)
                awaitComplete()
            }
        }

    @Test
    fun `Get Expense success`() =
        runTest {
            val expected = expenseFinanceModelOne
            when (val result = financeRepositoryImpl.getExpense(1)) {
                is GenericState.Success -> {
                    assertEquals(expected, result.data)
                }

                else -> Unit
            }
        }

    @Test
    fun `Get Income success`() =
        runTest {
            val expected = incomeFinanceModelOne
            when (val result = financeRepositoryImpl.getIncome(1)) {
                is GenericState.Success -> {
                    assertEquals(expected, result.data)
                }

                else -> Unit
            }
        }

    @Test
    fun `Get Expense Month Detail success`() =
        runTest {
            val expected = monthExpenseDetailScreenModel
            financeRepositoryImpl.getExpenseMonthDetail(
                categoryEnum = getCategoryEnumFromName(expenseOne.category),
                monthKey = getCurrentMonthKey()
            ).test {
                assertEquals(expected, (awaitItem() as GenericState.Success).data)
                awaitComplete()
            }
        }

    @Test
    fun `Get Income Month Detail success`() =
        runTest {
            val expected = monthIncomeDetailScreenModel
            financeRepositoryImpl.getIncomeMonthDetail(
                monthKey = getCurrentMonthKey()
            ).test {
                assertEquals(expected, (awaitItem() as GenericState.Success).data)
                awaitComplete()
            }
        }

    @Test
    fun `Get Months success`() =
        runTest {
            val expected = monthListFiltered
            financeRepositoryImpl.getMonths().test {
                assertEquals(expected, (awaitItem() as GenericState.Success).data)
                awaitComplete()
            }
        }
}
