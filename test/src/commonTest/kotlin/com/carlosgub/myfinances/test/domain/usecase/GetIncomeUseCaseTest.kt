package com.carlosgub.myfinances.test.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.usecase.GetIncomeUseCase
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.incomeFinanceModelOne
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetIncomeUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getIncomeUseCase = GetIncomeUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Income success`() =
        runTest {
            val expected = GenericState.Success(incomeFinanceModelOne)
            val result = getIncomeUseCase(GetIncomeUseCase.Params(id = incomeFinanceModelOne.id))
            assertEquals(expected, result)
        }
}
