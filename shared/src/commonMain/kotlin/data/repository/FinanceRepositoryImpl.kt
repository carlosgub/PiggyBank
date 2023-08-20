package data.repository

import core.mapper.ResultMapper
import core.network.ResponseResult
import core.sealed.GenericState
import data.firebase.FirebaseFinance
import domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.CategoryEnum
import model.Expense
import model.FinanceEnum
import model.FinanceScreenExpenses
import model.FinanceScreenModel
import utils.getCategoryEnumFromName
import kotlin.math.roundToInt

class FinanceRepositoryImpl(
    private val firebaseFinance: FirebaseFinance
) : FinanceRepository {

    override suspend fun getCurrentFinance(): GenericState<FinanceScreenModel> =
        withContext(Dispatchers.Default) {
            when (val result = firebaseFinance.getFinance()) {
                is ResponseResult.Success -> {
                    var expenseTotal = 0
                    var incomeTotal = 0
                    result.data.category.forEach {
                        if (getCategoryEnumFromName(it.key).type == FinanceEnum.EXPENSE) {
                            expenseTotal += it.value.amount
                        } else {
                            incomeTotal += it.value.amount
                        }
                    }
                    val expenseList =
                        result.data.category.entries.filter { getCategoryEnumFromName(it.key).type == FinanceEnum.EXPENSE }
                            .map {
                                FinanceScreenExpenses(
                                    category = getCategoryEnumFromName(it.key),
                                    amount = it.value.amount,
                                    count = it.value.count,
                                    percentage = (it.value.amount / expenseTotal.toFloat() * 100).roundToInt()
                                )
                            }.sortedByDescending { it.percentage }
                    val incomeList =
                        result.data.category.entries.filter { getCategoryEnumFromName(it.key).type == FinanceEnum.INCOME }
                            .map {
                                FinanceScreenExpenses(
                                    category = getCategoryEnumFromName(it.key),
                                    amount = it.value.amount,
                                    count = it.value.count,
                                    percentage = (it.value.amount / incomeTotal.toFloat() * 100).roundToInt()
                                )
                            }.sortedByDescending { it.percentage }
                    GenericState.Success(
                        FinanceScreenModel(
                            expenseAmount = result.data.expenseAmount,
                            expenses = expenseList,
                            incomes = incomeList
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

    override suspend fun createIncome(amount: Int, note: String): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                firebaseFinance.createIncome(
                    amount,
                    note
                )
            )
        }

    override suspend fun getCategoryMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): GenericState<List<Expense>> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                firebaseFinance.getCategoryMonthDetail(
                    categoryEnum,
                    monthKey
                )
            )
        }
}
