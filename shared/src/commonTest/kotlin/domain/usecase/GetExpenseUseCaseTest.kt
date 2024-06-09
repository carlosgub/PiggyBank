package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.expenseFinanceModelOne
import data.repository.source.database.expenseOne
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseUseCase = GetExpenseUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Expense success`() =
        runTest {
            val expected = GenericState.Success(expenseFinanceModelOne)
            val result = getExpenseUseCase(GetExpenseUseCase.Params(id = expenseOne.id))
            assertEquals(expected, result)
        }
}
