package com.carlosgub.myfinances.test.domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.usecase.GetIncomeMonthDetailUseCase
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.monthIncomeDetailModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetIncomeMonthDetailUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getIncomeMonthDetailUseCase = GetIncomeMonthDetailUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Income Month Detail success`() =
        runTest {
            val expected = GenericState.Success(monthIncomeDetailModel)
            getIncomeMonthDetailUseCase(GetIncomeMonthDetailUseCase.Params(monthKey = getCurrentMonthKey()))
                .test {
                    assertEquals(expected, awaitItem())
                    awaitComplete()
                }
        }
}
