package domain.usecase

import app.cash.turbine.test
import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.financeScreenModel
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFinanceUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getFinanceUseCase = GetFinanceUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Finance success`() =
        runTest {
            val expected = financeScreenModel
            getFinanceUseCase(GetFinanceUseCase.Params(monthKey = getCurrentMonthKey()))
                .test {
                    assertEquals(expected, (awaitItem() as GenericState.Success).data)
                    awaitComplete()
                }
        }
}
