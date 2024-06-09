package data.repository

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import data.repository.source.database.expenseFinanceModelOne
import data.repository.source.database.expenseOne
import data.repository.source.database.financeScreenModel
import data.repository.source.database.impl.FakeDatabaseFinanceDataSource
import data.repository.source.database.incomeFinanceModelOne
import data.repository.source.database.monthExpenseDetailScreenModel
import data.repository.source.database.monthIncomeDetailScreenModel
import data.repository.source.database.monthListFiltered
import domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
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
