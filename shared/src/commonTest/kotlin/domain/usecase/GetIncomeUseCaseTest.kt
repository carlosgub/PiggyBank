package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import data.repository.impl.FakeFinanceRepositoryImpl
import kotlinx.coroutines.test.runTest
import mock.incomeFinanceModelOne
import kotlin.test.Test
import kotlin.test.assertEquals

class GetIncomeUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getIncomeUseCase = GetIncomeUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Income success`() =
        runTest {
            val expected = GenericState.Success(incomeFinanceModelOne)
            val result = getIncomeUseCase(GetIncomeUseCase.Params(id = incomeFinanceModelOne.id))
            assertEquals(expected, result)
        }
}
