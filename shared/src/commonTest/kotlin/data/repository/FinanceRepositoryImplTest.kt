package data.repository

import core.sealed.GenericState
import data.repository.source.database.expenseFinanceModelOne
import data.repository.source.database.financeScreenModel
import data.repository.source.database.impl.FakeDatabaseFinanceDataSource
import data.repository.source.database.incomeFinanceModelOne
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class FinanceRepositoryImplTest {
    private val fakeDatabaseFinanceDataSource = FakeDatabaseFinanceDataSource()
    private val financeRepositoryImpl = FinanceRepositoryImpl(fakeDatabaseFinanceDataSource)

    @Test
    fun `Get Finance success`() = runTest {
        val expected = financeScreenModel
        when (val getFinance = financeRepositoryImpl.getFinance(getCurrentMonthKey()).first()) {
            is GenericState.Success -> {
                assertEquals(expected, getFinance.data)
            }

            else -> Unit
        }

    }

    @Test
    fun `Get Expense success`() = runTest {
        val expected = expenseFinanceModelOne
        when (val getExpense = financeRepositoryImpl.getExpense(1)) {
            is GenericState.Success -> {
                assertEquals(expected, getExpense.data)
            }

            else -> Unit
        }
    }

    @Test
    fun `Get Income success`() = runTest {
        val expected = incomeFinanceModelOne
        when (val getExpense = financeRepositoryImpl.getIncome(1)) {
            is GenericState.Success -> {
                assertEquals(expected, getExpense.data)
            }

            else -> Unit
        }
    }
}