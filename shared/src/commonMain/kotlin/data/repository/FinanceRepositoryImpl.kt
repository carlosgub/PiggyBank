package data.repository

import core.mapper.ResultMapper
import core.network.ResponseResult
import core.sealed.GenericState
import data.firebase.FirebaseFinance
import domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.FinanceScreenExpenses
import model.FinanceScreenModel
import utils.getCategoryEnumFromName

class FinanceRepositoryImpl(
    private val firebaseFinance: FirebaseFinance
) : FinanceRepository {

    override suspend fun getCurrentFinance(): GenericState<FinanceScreenModel> =
        withContext(Dispatchers.Default) {
            when (val result = firebaseFinance.getFinance()) {
                is ResponseResult.Success -> {
                    val list = result.data.category.entries.map {
                        FinanceScreenExpenses(
                            category = getCategoryEnumFromName(it.key),
                            amount = it.value.amount,
                            count = it.value.amount
                        )
                    }
                    GenericState.Success(
                        FinanceScreenModel(
                            monthAmount = result.data.amount,
                            expenses = list
                        )
                    )
                }

                is ResponseResult.Error -> GenericState.Error(result.error.message.orEmpty())
            }
        }

    override suspend fun createExpense(
        amount: Int,
        category: String,
        note: String
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                firebaseFinance.createExpense(
                    amount,
                    category,
                    note
                )
            )
        }
}
