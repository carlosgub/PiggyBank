package data.repository

import core.mapper.ResultMapper
import core.sealed.GenericState
import data.firebase.FirebaseFinance
import domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Finance

class FinanceRepositoryImpl(
    private val firebaseFinance: FirebaseFinance
) : FinanceRepository {

    override suspend fun getFinance(): GenericState<Finance> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                firebaseFinance.getFinance()
            )
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
