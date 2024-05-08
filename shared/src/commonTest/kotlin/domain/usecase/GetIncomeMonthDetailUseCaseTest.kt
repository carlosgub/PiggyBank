package domain.usecase

import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.monthIncomeDetailScreenModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class GetIncomeMonthDetailUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getIncomeMonthDetailUseCase = GetIncomeMonthDetailUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Income Motnh Detail success`() = runTest {
        val expected = monthIncomeDetailScreenModel
        val result =
            getIncomeMonthDetailUseCase(GetIncomeMonthDetailUseCase.Params(monthKey = getCurrentMonthKey())).first()
        when (result) {
            is GenericState.Success -> {
                assertEquals(expected, result.data)
            }

            else -> Unit
        }
    }
}