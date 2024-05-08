package domain.usecase

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.expenseFinanceModelOne
import data.repository.source.database.expenseOne
import data.repository.source.database.financeScreenModel
import data.repository.source.database.monthIncomeDetailScreenModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseUseCase = GetExpenseUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Expense success`() = runTest {
        val expected = expenseFinanceModelOne
        val result = getExpenseUseCase(GetExpenseUseCase.Params(id = expenseOne.id))
        when (result) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }
}