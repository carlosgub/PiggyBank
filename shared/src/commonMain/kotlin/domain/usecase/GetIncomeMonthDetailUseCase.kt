package domain.usecase

import com.carlosgub.myfinances.core.state.GenericState
import domain.model.MonthDetailScreenModel
import domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow

class GetIncomeMonthDetailUseCase(
    private val financeRepository: FinanceRepository,
) {
    suspend operator fun invoke(params: Params): Flow<GenericState<MonthDetailScreenModel>> =
        financeRepository.getIncomeMonthDetail(
            monthKey = params.monthKey,
        )

    data class Params(
        val monthKey: String,
    )
}
