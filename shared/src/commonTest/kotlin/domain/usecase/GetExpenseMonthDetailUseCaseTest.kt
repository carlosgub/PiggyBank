package domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import data.repository.impl.FakeFinanceRepositoryImpl
import domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import kotlinx.coroutines.test.runTest
import mock.expenseFinanceModelOne
import mock.monthExpenseDetailScreenModel
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseMonthDetailUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseMonthDetailUseCase =
        GetExpenseMonthDetailUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Category Month Detail success`() =
        runTest {
            val expected = GenericState.Success(monthExpenseDetailScreenModel)
            getExpenseMonthDetailUseCase(
                GetExpenseMonthDetailUseCase.Params(
                    categoryEnum = getCategoryEnumFromName(expenseFinanceModelOne.category),
                    monthKey = getCurrentMonthKey(),
                ),
            ).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
