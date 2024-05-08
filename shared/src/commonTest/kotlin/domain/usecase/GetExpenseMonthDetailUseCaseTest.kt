package domain.usecase

import app.cash.turbine.test
import core.sealed.GenericState
import data.repository.FakeFinanceRepositoryImpl
import data.repository.source.database.expenseOne
import data.repository.source.database.monthExpenseDetailScreenModel
import kotlinx.coroutines.test.runTest
import utils.getCategoryEnumFromName
import utils.getCurrentMonthKey
import kotlin.test.Test
import kotlin.test.assertEquals

class GetExpenseMonthDetailUseCaseTest {
    private val fakeFinanceRepositoryImpl = FakeFinanceRepositoryImpl()
    private val getExpenseMonthDetailUseCase =
        GetExpenseMonthDetailUseCase(fakeFinanceRepositoryImpl)

    @Test
    fun `Get Category Month Detail success`() = runTest {
        val expected = monthExpenseDetailScreenModel
        getExpenseMonthDetailUseCase(
            GetExpenseMonthDetailUseCase.Params(
                categoryEnum = getCategoryEnumFromName(expenseOne.category),
                monthKey = getCurrentMonthKey()
            )
        ).test {
            assertEquals(expected, (awaitItem() as GenericState.Success).data)
            awaitComplete()
        }
    }
}