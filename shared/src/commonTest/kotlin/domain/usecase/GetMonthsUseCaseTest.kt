package domain.usecase

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.monthListFiltered
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMonthsUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getMonthsUseCase = GetMonthsUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Months success`() = runTest {
        val expected = monthListFiltered
        when (val result = getMonthsUseCase().first()) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }
}