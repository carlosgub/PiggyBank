package data.repository

import core.sealed.GenericState
import data.repository.source.database.expenseFinanceModelOne
import data.repository.source.database.expenseOne
import data.repository.source.database.financeScreenModel
import data.repository.source.database.impl.FakeDatabaseFinanceDataSource
import data.repository.source.database.incomeFinanceModelOne
import data.repository.source.database.monthExpenseDetailScreenModel
import data.repository.source.database.monthIncomeDetailScreenModel
import data.repository.source.database.monthListFiltered
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import utils.getCategoryEnumFromName
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class FinanceRepositoryImplTest {
    private val fakeDatabaseFinanceDataSource = FakeDatabaseFinanceDataSource()
    private val financeRepositoryImpl = FinanceRepositoryImpl(fakeDatabaseFinanceDataSource)

    @Test
    fun `Get Finance success`() = runTest {
        val expected = financeScreenModel
        when (val result = financeRepositoryImpl.getFinance(getCurrentMonthKey()).first()) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }

    }

    @Test
    fun `Get Expense success`() = runTest {
        val expected = expenseFinanceModelOne
        when (val result = financeRepositoryImpl.getExpense(1)) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }

    @Test
    fun `Get Income success`() = runTest {
        val expected = incomeFinanceModelOne
        when (val result = financeRepositoryImpl.getIncome(1)) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }

    @Test
    fun `Get Expense Month Detail success`() = runTest {
        val expected = monthExpenseDetailScreenModel
        when (val result = financeRepositoryImpl.getExpenseMonthDetail(
            categoryEnum = getCategoryEnumFromName(expenseOne.category),
            monthKey = getCurrentMonthKey()
        ).first()) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }

    @Test
    fun `Get Income Month Detail success`() = runTest {
        val expected = monthIncomeDetailScreenModel
        when (val result = financeRepositoryImpl.getIncomeMonthDetail(
            monthKey = getCurrentMonthKey()
        ).first()) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }

    @Test
    fun `Get Months success`() = runTest {
        val expected = monthListFiltered
        when (val result = financeRepositoryImpl.getMonths().first()) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }
}