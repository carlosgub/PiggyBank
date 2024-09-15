package com.carlosgub.myfinances.test.domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import com.carlosgub.myfinances.domain.usecase.GetExpenseMonthDetailUseCase
import com.carlosgub.myfinances.test.data.repository.impl.FakeFinanceRepositoryImpl
import com.carlosgub.myfinances.test.mock.expenseModelOne
import com.carlosgub.myfinances.test.mock.monthExpenseDetailModel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseMonthDetailUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseMonthDetailUseCase =
        GetExpenseMonthDetailUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Category Month Detail success`() =
        runTest {
            val expected = GenericState.Success(monthExpenseDetailModel)
            getExpenseMonthDetailUseCase(
                GetExpenseMonthDetailUseCase.Params(
                    categoryEnum = getCategoryEnumFromName(expenseModelOne.category),
                    monthKey = getCurrentMonthKey(),
                ),
            ).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
