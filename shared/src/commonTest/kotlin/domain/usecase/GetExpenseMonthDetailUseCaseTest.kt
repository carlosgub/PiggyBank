package domain.usecase

import app.cash.turbine.test
import com.carlosgub.myfinances.core.state.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.expenseOne
import data.repository.source.database.monthExpenseDetailScreenModel
import domain.model.CategoryEnum.Companion.getCategoryEnumFromName
import kotlinx.coroutines.test.runTest
import utils.getCurrentMonthKey
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
                    categoryEnum = getCategoryEnumFromName(expenseOne.category),
                    monthKey = getCurrentMonthKey(),
                ),
            ).test {
                assertEquals(expected, awaitItem())
                awaitComplete()
            }
        }
}
