package com.carlosgub.myfinances.test.domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.usecase.GetExpenseUseCase
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.expenseModelOne
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseUseCase = GetExpenseUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Expense success`() =
        runTest {
            val expected = GenericState.Success(expenseModelOne)
            val result = getExpenseUseCase(GetExpenseUseCase.Params(id = expenseModelOne.id))
            assertEquals(expected, result)
        }
}
