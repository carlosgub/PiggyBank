package com.carlosgub.myfinances.test.domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.usecase.GetFinanceUseCase
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.financeModelMock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFinanceUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getFinanceUseCase = GetFinanceUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Finance success`() =
        runTest {
            val expected = GenericState.Success(financeModelMock)
            getFinanceUseCase(GetFinanceUseCase.Params(monthKey = getCurrentMonthKey()))
                .test {
                    assertEquals(expected, awaitItem())
                    awaitComplete()
                }
        }
}
