package domain.usecase

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.financeScreenModel
import data.repository.source.database.monthIncomeDetailScreenModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFinanceUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getFinanceUseCase = GetFinanceUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Finance success`() = runTest {
        val expected = financeScreenModel
        val result =
            getFinanceUseCase(GetFinanceUseCase.Params(monthKey = getCurrentMonthKey())).first()
        when (result) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }
}