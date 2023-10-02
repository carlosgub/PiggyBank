package data.repository

import core.mapper.ResultMapper
import core.network.ResponseResult
import core.sealed.GenericState
import data.firebase.FirebaseFinance
import domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import model.CategoryEnum
import model.ExpenseScreenModel
import model.FinanceEnum
import model.FinanceScreenExpenses
import model.FinanceScreenModel
import model.MonthDetailScreenModel
import model.MonthExpense
import utils.createLocalDateTime
import utils.getCategoryEnumFromName
import utils.isLeapYear
import utils.monthLength
import utils.toLocalDate
import utils.toNumberOfTwoDigits
import kotlin.math.roundToInt

class FinanceRepositoryImpl(
    private val firebaseFinance: FirebaseFinance
) : FinanceRepository {

    override suspend fun getFinance(monthKey: String): GenericState<FinanceScreenModel> =
        withContext(Dispatchers.Default) {
            when (val result = firebaseFinance.getFinance(monthKey)) {
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
                    val monthExpense =
                        MonthExpense(
                            incomeTotal = incomeTotal / 100.0,
                            percentage = if (incomeTotal != 0) expenseTotal * 100 / incomeTotal else 100
                        )
                    GenericState.Success(
                        FinanceScreenModel(
                            expenseAmount = result.data.expenseAmount,
                            expenses = expenseList,
                            income = incomeList,
                            localDateTime = createLocalDateTime(
                                year = monthKey.substring(2, 6).toInt(),
                                monthNumber = monthKey.substring(0, 2).trimStart('0').toInt()
                            ),
                            monthExpense = monthExpense
                        )
                    )
                }

                is ResponseResult.Error -> GenericState.Error(result.error.message.orEmpty())
            }
        }

    override suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                firebaseFinance.createExpense(
                    amount = amount,
                    category = category,
                    note = note,
                    dateInMillis = dateInMillis
                )
            )
        }

    override suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                firebaseFinance.createIncome(
                    amount,
                    note,
                    dateInMillis
                )
            )
        }

    override suspend fun getCategoryMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): GenericState<MonthDetailScreenModel> =
        withContext(Dispatchers.Default) {
            when (val result = firebaseFinance.getCategoryMonthDetail(categoryEnum, monthKey)) {
                is ResponseResult.Error -> GenericState.Error(result.error.message.orEmpty())
                is ResponseResult.Success -> {
                    val monthAmount = result.data.sumOf { it.amount }
                    val expenseScreenModelList = result.data.map { expense ->
                        val localDate: LocalDate = if (expense.dateInMillis != null) {
                            expense.dateInMillis.toLocalDate()
                        } else {
                            val milliseconds =
                                expense.timestamp.seconds * 1000 + expense.timestamp.nanoseconds / 1000000
                            milliseconds.toLocalDate()
                        }
                        val localDateTime = createLocalDateTime(
                            year = localDate.year,
                            monthNumber = localDate.monthNumber,
                            dayOfMonth = localDate.dayOfMonth
                        )
                        val dayOfMonth = localDateTime.dayOfMonth.toNumberOfTwoDigits()
                        val month =
                            localDateTime.month.name.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        val year =
                            localDateTime.year
                        ExpenseScreenModel(
                            amount = expense.amount,
                            userId = expense.userId,
                            note = expense.note.replaceFirstChar { it.uppercase() },
                            category = expense.category,
                            localDateTime = localDateTime,
                            date = "$dayOfMonth $month $year"
                        )
                    }
                    val date = createLocalDateTime(
                        year = monthKey.substring(2, 6).toInt(),
                        monthNumber = monthKey.substring(0, 2).trimStart('0').toInt()
                    )
                    val daySpent =
                        (1..date.monthNumber.monthLength(isLeapYear(date.year))).map { day ->
                            val dateInternal = createLocalDateTime(
                                year = monthKey.substring(2, 6).toInt(),
                                monthNumber = monthKey.substring(0, 2).trimStart('0').toInt(),
                                dayOfMonth = day
                            )
                            dateInternal to expenseScreenModelList.filter { expense ->
                                expense.localDateTime == dateInternal
                            }
                                .sumOf { it.amount }
                        }.toMap()
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

    override suspend fun getMonths(): GenericState<Map<Int, List<LocalDateTime>>> =
        withContext(Dispatchers.Default) {
            when (val result = firebaseFinance.getMonths()) {
                is ResponseResult.Success -> {
                    GenericState.Success(
                        result.data.map { month ->
                            createLocalDateTime(
                                year = month.year.toInt(),
                                monthNumber = month.month.trimStart('0').toInt()
                            )
                        }.groupBy {
                            it.year
                        }
                    )
                }

                is ResponseResult.Error -> GenericState.Error(result.error.message.toString())
            }
        }
}
