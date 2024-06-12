package domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.financeScreenModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFinanceUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getFinanceUseCase = GetFinanceUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Finance success`() =
        runTest {
            val expected = GenericState.Success(financeScreenModel)
            getFinanceUseCase(GetFinanceUseCase.Params(monthKey = getCurrentMonthKey()))
                .test {
                    assertEquals(expected, awaitItem())
                    awaitComplete()
                }
        }
}
