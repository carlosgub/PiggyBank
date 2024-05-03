package data.repository

import core.mapper.ResultMapper
import core.network.ResponseResult
import core.sealed.GenericState
import data.source.database.DatabaseFinance
import domain.model.CategoryEnum
import domain.model.ExpenseScreenModel
import domain.model.FinanceEnum
import domain.model.FinanceLocalDate
import domain.model.FinanceModel
import domain.model.FinanceScreenExpenses
import domain.model.FinanceScreenModel
import domain.model.MonthDetailScreenModel
import domain.model.MonthExpense
import domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import utils.createLocalDateTime
import utils.getCategoryEnumFromName
import utils.getCurrentMonthKey
import utils.isLeapYear
import utils.monthLength
import utils.toLocalDate
import utils.toMonthKey
import kotlin.math.roundToInt

class FinanceRepositoryImpl(
    private val databaseFinance: DatabaseFinance,
) : FinanceRepository {
    override suspend fun getFinance(monthKey: String): Flow<GenericState<FinanceScreenModel>> =
        flow {
            databaseFinance.getAllMonthExpenses(monthKey)
                .combine(databaseFinance.getAllMonthIncome(monthKey)) { expenses, income ->
                    Pair(expenses, income)
                }.collect { pair ->
                    val expenses = pair.first
                    val income = pair.second
                    if (expenses is ResponseResult.Success && income is ResponseResult.Success) {
                        val expenseTotal = expenses.data.sumOf { it.amount }
                        val incomeTotal = income.data.sumOf { it.amount }
                        val expenseList =
                            expenses.data.groupBy { it.category }
                                .map {
                                    val amount = it.value.sumOf { it.amount }
                                    FinanceScreenExpenses(
                                        category = getCategoryEnumFromName(it.key),
                                        amount = amount,
                                        count = it.value.size,
                                        percentage = (amount / expenseTotal.toFloat() * 100).roundToInt(),
                                    )
                                }.sortedByDescending { it.percentage }
                        val incomeList =
                            income.data.groupBy { it.category }
                                .map {
                                    val amount = it.value.sumOf { it.amount }
                                    FinanceScreenExpenses(
                                        category = getCategoryEnumFromName(it.key),
                                        amount = amount,
                                        count = it.value.size,
                                        percentage = (amount / incomeTotal.toFloat() * 100).roundToInt(),
                                    )
                                }.sortedByDescending { it.amount }
                        val monthExpense =
                            MonthExpense(
                                incomeTotal = incomeTotal / 100.0,
                                percentage = if (incomeTotal != 0L) expenseTotal * 100 / incomeTotal else 100,
                            )
                        val date =
                            createLocalDateTime(
                                year = monthKey.substring(2, 6).toInt(),
                                monthNumber = monthKey.substring(0, 2).trimStart('0').toInt(),
                            )
                        val expenseScreenModelList =
                            expenses.data.map { expense ->
                                val localDate = FinanceLocalDate(expense.dateInMillis.toLocalDate())
                                ExpenseScreenModel(
                                    id = expense.id,
                                    amount = expense.amount,
                                    note = expense.note.replaceFirstChar { it.uppercase() },
                                    category = expense.category,
                                    localDateTime = localDate.localDateTime,
                                    date = localDate.date,
                                    monthKey = monthKey,
                                )
                            }.sortedByDescending { it.localDateTime }
                        val daySpent =
                            (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
                                val dateInternal =
                                    createLocalDateTime(
                                        year = monthKey.substring(2, 6).toInt(),
                                        monthNumber =
                                            monthKey.substring(0, 2).trimStart('0')
                                                .toInt(),
                                        dayOfMonth = day,
                                    )
                                dateInternal to
                                    expenseScreenModelList.filter { expense ->
                                        expense.localDateTime == dateInternal
                                    }.sumOf { it.amount }
                            }
                        emit(
                            GenericState.Success(
                                FinanceScreenModel(
                                    expenseAmount = expenseTotal,
                                    expenses = expenseList,
                                    income = incomeList,
                                    month =
                                        createLocalDateTime(
                                            year = monthKey.substring(2, 6).toInt(),
                                            monthNumber =
                                                monthKey.substring(0, 2).trimStart('0')
                                                    .toInt(),
                                        ).month,
                                    monthExpense = monthExpense,
                                    daySpent = daySpent,
                                ),
                            ),
                        )
                    } else {
                        if (expenses is ResponseResult.Error) {
                            GenericState.Error(expenses.error.message.orEmpty())
                        } else {
                            GenericState.Error((income as ResponseResult.Error).error.message.orEmpty())
                        }
                    }
                }
        }

    override suspend fun getExpense(id: Long): GenericState<FinanceModel> =
        withContext(Dispatchers.Default) {
            val expense = databaseFinance.getExpense(id)
            if (expense is ResponseResult.Success) {
                val localDate = FinanceLocalDate(expense.data.dateInMillis.toLocalDate())
                GenericState.Success(
                    FinanceModel(
                        id = expense.data.id,
                        amount = expense.data.amount,
                        note = expense.data.note,
                        category = expense.data.category,
                        localDateTime = localDate.localDateTime,
                        date = localDate.date,
                        monthKey = expense.data.month,
                    ),
                )
            } else {
                GenericState.Error("")
            }
        }

    override suspend fun getIncome(id: Long): GenericState<FinanceModel> =
        withContext(Dispatchers.Default) {
            val income = databaseFinance.getIncome(id)
            if (income is ResponseResult.Success) {
                val localDate = FinanceLocalDate(income.data.dateInMillis.toLocalDate())
                GenericState.Success(
                    FinanceModel(
                        id = income.data.id,
                        amount = income.data.amount,
                        note = income.data.note,
                        category = income.data.category,
                        localDateTime = localDate.localDateTime,
                        date = localDate.date,
                        monthKey = income.data.month,
                    ),
                )
            } else {
                GenericState.Error("")
            }
        }

    override suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                databaseFinance.createExpense(
                    amount = amount,
                    category = category,
                    note = note,
                    dateInMillis = dateInMillis,
                ),
            )
        }

    override suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            ResultMapper.toGenericState(
                databaseFinance.createIncome(
                    amount = amount,
                    note = note,
                    dateInMillis = dateInMillis,
                ),
            )
        }

    override suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            when (
                val result =
                    databaseFinance.editExpense(
                        amount = amount,
                        category = category,
                        note = note,
                        dateInMillis = dateInMillis,
                        id = id,
                    )
            ) {
                is ResponseResult.Success -> GenericState.Success(Unit)
                is ResponseResult.Error -> GenericState.Error(result.error.message.toString())
            }
        }

    override suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            when (
                val result =
                    databaseFinance.editIncome(
                        amount = amount,
                        note = note,
                        dateInMillis = dateInMillis,
                        id = id,
                    )
            ) {
                is ResponseResult.Success -> GenericState.Success(Unit)
                is ResponseResult.Error -> GenericState.Error(result.error.message.toString())
            }
        }

    override suspend fun delete(
        financeEnum: FinanceEnum,
        id: Long,
        monthKey: String,
    ): GenericState<Unit> =
        withContext(Dispatchers.Default) {
            when (
                val result =
                    databaseFinance.delete(
                        financeEnum = financeEnum,
                        id = id,
                        monthKey = monthKey,
                    )
            ) {
                is ResponseResult.Success -> GenericState.Success(Unit)
                is ResponseResult.Error -> GenericState.Error(result.error.message.toString())
            }
        }

    override suspend fun getExpenseMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String,
    ): Flow<GenericState<MonthDetailScreenModel>> =
        flow {
            databaseFinance.getExpenseMonthDetail(categoryEnum, monthKey).collect { result ->
                when (result) {
                    is ResponseResult.Error -> GenericState.Error(result.error.message.orEmpty())
                    is ResponseResult.Success -> {
                        val monthAmount = result.data.sumOf { it.amount }
                        val expenseScreenModelList =
                            result.data.map { expense ->
                                val localDate = FinanceLocalDate(expense.dateInMillis.toLocalDate())
                                ExpenseScreenModel(
                                    id = expense.id,
                                    amount = expense.amount,
                                    note = expense.note.replaceFirstChar { it.uppercase() },
                                    category = expense.category,
                                    localDateTime = localDate.localDateTime,
                                    date = localDate.date,
                                    monthKey = monthKey,
                                )
                            }.sortedByDescending { it.localDateTime }
                        val date =
                            createLocalDateTime(
                                year = monthKey.substring(2, 6).toInt(),
                                monthNumber = monthKey.substring(0, 2).trimStart('0').toInt(),
                            )
                        val daySpent =
                            (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
                                val dateInternal =
                                    createLocalDateTime(
                                        year = monthKey.substring(2, 6).toInt(),
                                        monthNumber = monthKey.substring(0, 2).trimStart('0').toInt(),
                                        dayOfMonth = day,
                                    )
                                dateInternal to
                                    expenseScreenModelList.filter { expense ->
                                        expense.localDateTime == dateInternal
                                    }.sumOf { it.amount }
                            }
                        emit(
                            GenericState.Success(
                                MonthDetailScreenModel(
                                    monthAmount = monthAmount,
                                    expenseScreenModel = expenseScreenModelList,
                                    daySpent = daySpent,
                                ),
                            ),
                        )
                    }
                }
            }
        }

    override suspend fun getIncomeMonthDetail(monthKey: String): Flow<GenericState<MonthDetailScreenModel>> =
        flow {
            databaseFinance.getIncomeMonthDetail(monthKey)
                .collect { result ->
                    when (result) {
                        is ResponseResult.Error -> GenericState.Error(result.error.message.orEmpty())
                        is ResponseResult.Success -> {
                            val monthAmount = result.data.sumOf { it.amount }
                            val expenseScreenModelList =
                                result.data.map { expense ->
                                    val localDate =
                                        FinanceLocalDate(expense.dateInMillis.toLocalDate())
                                    ExpenseScreenModel(
                                        id = expense.id,
                                        amount = expense.amount,
                                        note = expense.note.replaceFirstChar { it.uppercase() },
                                        category = expense.category,
                                        localDateTime = localDate.localDateTime,
                                        date = localDate.date,
                                        monthKey = monthKey,
                                    )
                                }.sortedByDescending { it.localDateTime }
                            val date =
                                createLocalDateTime(
                                    year = monthKey.substring(2, 6).toInt(),
                                    monthNumber = monthKey.substring(0, 2).trimStart('0').toInt(),
                                )
                            val daySpent =
                                (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
                                    val dateInternal =
                                        createLocalDateTime(
                                            year = monthKey.substring(2, 6).toInt(),
                                            monthNumber =
                                                monthKey.substring(0, 2).trimStart('0')
                                                    .toInt(),
                                            dayOfMonth = day,
                                        )
                                    dateInternal to
                                        expenseScreenModelList.filter { expense ->
                                            expense.localDateTime == dateInternal
                                        }.sumOf { it.amount }
                                }
                            emit(
                                GenericState.Success(
                                    MonthDetailScreenModel(
                                        monthAmount = monthAmount,
                                        expenseScreenModel = expenseScreenModelList,
                                        daySpent = daySpent,
                                    ),
                                ),
                            )
                        }
                    }
                }
        }

    override suspend fun getMonths(): Flow<GenericState<Map<Int, List<LocalDateTime>>>> =
        flow {
            databaseFinance.getMonths().collect { result ->
                when (result) {
                    is ResponseResult.Success -> {
                        emit(
                            GenericState.Success(
                                result.data.map { month ->
                                    createLocalDateTime(
                                        year = month.year.toInt(),
                                        monthNumber = month.month.trimStart('0').toInt(),
                                    )
                                }.filter { localDateTime ->
                                    localDateTime.toMonthKey() != getCurrentMonthKey()
                                }.groupBy { localDateTime ->
                                    localDateTime.year
                                },
                            ),
                        )
                    }

                    is ResponseResult.Error -> emit(GenericState.Error(result.error.message.toString()))
                }
            }
        }
}
