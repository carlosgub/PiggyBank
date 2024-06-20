package domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import data.repository.impl.FakeFinanceRepositoryImpl
import kotlinx.coroutines.test.runTest
import mock.monthIncomeDetailScreenModel
import kotlin.test.Test
import kotlin.test.assertEquals

class GetIncomeMonthDetailUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getIncomeMonthDetailUseCase = GetIncomeMonthDetailUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Income Month Detail success`() =
        runTest {
            val expected = GenericState.Success(monthIncomeDetailScreenModel)
            getIncomeMonthDetailUseCase(GetIncomeMonthDetailUseCase.Params(monthKey = getCurrentMonthKey()))
                .test {
                    assertEquals(expected, awaitItem())
                    awaitComplete()
                }
        }
}
