package data.repository

import core.mapper.ResultMapper
import core.network.ResponseResult
import core.sealed.GenericState
import data.firebase.FirebaseFinance
import domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import model.CategoryEnum
import model.ExpenseScreenModel
import model.FinanceEnum
import model.FinanceScreenExpenses
import model.FinanceScreenModel
import model.MonthDetailScreenModel
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
    ): GenericState<MonthDetailScreenModel> =
        withContext(Dispatchers.Default) {
            when (val result = firebaseFinance.getCategoryMonthDetail(
                categoryEnum,
                monthKey
            )) {
                is ResponseResult.Error -> GenericState.Error(result.error.message.orEmpty())
                is ResponseResult.Success -> {

                    val monthAmount = result.data.sumOf { it.amount }
                    val expenseScreenModelList = result.data.map { expense ->
                        val milliseconds =
                            expense.timestamp.seconds * 1000 + expense.timestamp.nanoseconds / 1000000
                        val netDate = Instant.fromEpochMilliseconds(milliseconds)
                        val today: LocalDate =
                            netDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
                        val dayOfMonth = if (today.dayOfMonth < 10) {
                            "0${today.dayOfMonth}"
                        } else {
                            today.dayOfMonth
                        }
                        val month = if (today.month.number < 10) {
                            "0${today.month.number}"
                        } else {
                            today.month.number
                        }
                        ExpenseScreenModel(
                            amount = expense.amount,
                            userId = expense.userId,
                            note = expense.note,
                            category = expense.category,
                            month = expense.month,
                            day = "${dayOfMonth}/${month}"

                        )
                    }
                    val daySpent = expenseScreenModelList.sortedBy { it.day } .associate { it.day to it.amount }
                    GenericState.Success(
                        MonthDetailScreenModel(
                            monthAmount = monthAmount,
                            expenseScreenModel = expenseScreenModelList,
                            daySpent = daySpent
                        )
                    )
                }
            }
        }
}
