package com.carlosgub.myfinances.test.domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.monthListFiltered
import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMonthsUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getMonthsUseCase = GetMonthsUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Months success`() =
        runTest {
            val expected = GenericState.Success(monthListFiltered)
            getMonthsUseCase().test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
