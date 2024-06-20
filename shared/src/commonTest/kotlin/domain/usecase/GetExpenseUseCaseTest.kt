package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import data.repository.impl.FakeFinanceRepositoryImpl
import kotlinx.coroutines.test.runTest
import mock.expenseFinanceModelOne
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseUseCase = GetExpenseUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Expense success`() =
        runTest {
            val expected = GenericState.Success(expenseFinanceModelOne)
            val result = getExpenseUseCase(GetExpenseUseCase.Params(id = expenseFinanceModelOne.id))
            assertEquals(expected, result)
        }
}
