package presentation.viewmodel.months

import domain.usecase.GetMonthsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class MonthsViewModelTest {

    lateinit var getMonthsUseCase: GetMonthsUseCase

    @Test
    fun getMonths() = runTest {

        /*MonthsViewModel(getMonthsUseCase).test(this, MonthsScreenState()){
            coeve{getMonthsUseCase()} returns flow {
                emit(GenericState.Success(mapOf()))
            }
            containerHost.getMonths()
            expectState {copy(showLoading = true)}
        }*/
    }
}
