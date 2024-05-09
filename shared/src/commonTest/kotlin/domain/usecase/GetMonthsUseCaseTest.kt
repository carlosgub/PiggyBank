package domain.usecase

import app.cash.turbine.test
import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.monthListFiltered
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMonthsUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getMonthsUseCase = GetMonthsUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Months success`() =
        runTest {
            val expected = monthListFiltered
            getMonthsUseCase().test {
                assertEquals(
                    expected,
                    (awaitItem() as GenericState.Success).data,
                )
                awaitComplete()
            }
        }
}
