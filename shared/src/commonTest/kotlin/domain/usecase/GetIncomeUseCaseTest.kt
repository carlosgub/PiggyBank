package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.incomeFinanceModelOne
import data.repository.source.database.incomeOne
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetIncomeUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getIncomeUseCase = GetIncomeUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Income success`() =
        runTest {
            val expected = incomeFinanceModelOne
            when (val result = getIncomeUseCase(GetIncomeUseCase.Params(id = incomeOne.id))) {
                is GenericState.Success -> {
                    assertEquals(expected, result.data)
                }

                else -> Unit
            }
        }
}
