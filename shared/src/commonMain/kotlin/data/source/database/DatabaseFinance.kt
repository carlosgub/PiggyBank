package data.source.database

import core.network.ResponseResult
import data.source.database.expense.createExpense
import data.source.database.expense.deleteExpense
import data.source.database.expense.getExpense
import data.source.database.expense.getExpenseList
import data.source.database.expense.getExpenseListPerCategory
import data.source.database.expense.updateExpense
import data.source.database.income.createIncome
import data.source.database.income.deleteIncome
import data.source.database.income.getIncome
import data.source.database.income.getIncomeList
import data.source.database.income.getIncomeListPerCategory
import data.source.database.income.updateIncome
import data.source.database.month.createMonth
import data.source.database.month.deleteMonth
import data.source.database.month.getMonthList
import data.sqldelight.SharedDatabase
import domain.model.CategoryEnum
import domain.model.FinanceEnum
import domain.model.MonthModel
import expense.Expense
import income.Income
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.datetime.LocalDate
import utils.toLocalDate
import utils.toMonthString

class DatabaseFinance(
    private val sharedDatabase: SharedDatabase,
) {
    suspend fun getAllMonthExpenses(monthKey: String): Flow<ResponseResult<List<Expense>>> =
        flow {
            try {
                sharedDatabase().getExpenseList(monthKey).collect {
                    emit(ResponseResult.Success(it))
                }
            } catch (e: Exception) {
                emit(ResponseResult.Error(e))
            }
        }

    suspend fun getAllMonthIncome(monthKey: String): Flow<ResponseResult<List<Income>>> =
        flow {
            if (currentCoroutineContext().isActive) {
                try {
                    sharedDatabase().getIncomeList(monthKey).collect {
                        emit(ResponseResult.Success(it))
                    }
                } catch (e: Exception) {
                    emit(ResponseResult.Error(e))
                }
            }
        }

    suspend fun getExpense(id: Long): ResponseResult<Expense> =
        try {
            ResponseResult.Success(sharedDatabase().getExpense(id))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getIncome(id: Long): ResponseResult<Income> =
        try {
            ResponseResult.Success(sharedDatabase().getIncome(id))
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long,
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().createExpense(
                Expense(
                    id = 1,
                    amount = amount.toLong(),
                    category = category,
                    note = note,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey,
                ),
            )
            sharedDatabase().createMonth(
                currentMonthKey,
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long,
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().createIncome(
                Income(
                    id = 1,
                    amount = amount.toLong(),
                    category = CategoryEnum.WORK.name,
                    note = note,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey,
                ),
            )
            sharedDatabase().createMonth(
                currentMonthKey,
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getExpenseMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String,
    ): Flow<ResponseResult<List<Expense>>> =
        flow {
            try {
                sharedDatabase().getExpenseListPerCategory(
                    month = monthKey,
                    category = categoryEnum.name,
                ).collect { list ->
                    emit(ResponseResult.Success(list))
                }
            } catch (e: Exception) {
                emit(ResponseResult.Error(e))
            }
        }

    suspend fun getIncomeMonthDetail(monthKey: String): Flow<ResponseResult<List<Income>>> =
        flow {
            try {
                sharedDatabase().getIncomeListPerCategory(
                    month = monthKey,
                    category = CategoryEnum.WORK.name,
                ).collect { list ->
                    emit(ResponseResult.Success(list))
                }
            } catch (e: Exception) {
                emit(ResponseResult.Error(e))
            }
        }

    suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().updateExpense(
                Expense(
                    id = id,
                    note = note,
                    category = category,
                    amount = amount,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey,
                ),
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): ResponseResult<Unit> =
        try {
            val date: LocalDate = dateInMillis.toLocalDate()
            val currentMonthKey = "${date.month.toMonthString()}${date.year}"
            sharedDatabase().updateIncome(
                Income(
                    id = id,
                    note = note,
                    category = CategoryEnum.WORK.name,
                    amount = amount,
                    dateInMillis = dateInMillis,
                    month = currentMonthKey,
                ),
            )
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun delete(
        financeEnum: FinanceEnum,
        id: Long,
        monthKey: String,
    ): ResponseResult<Unit> =
        try {
            if (financeEnum == FinanceEnum.EXPENSE) {
                sharedDatabase().deleteExpense(
                    id = id,
                )
            } else {
                sharedDatabase().deleteIncome(
                    id = id,
                )
            }
            val expenses = sharedDatabase().getExpenseList(monthKey).first()
            val income = sharedDatabase().getIncomeList(monthKey).first()
            if (expenses.isEmpty() && income.isEmpty()) {
                sharedDatabase().deleteMonth(monthKey)
            }
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getMonths(): Flow<ResponseResult<List<MonthModel>>> =
        flow {
            try {
                sharedDatabase().getMonthList()
                    .collect { listMonth ->
                        val list =
                            listMonth.map { monthKey ->
                                MonthModel(
                                    id = monthKey,
                                    month = monthKey.take(2),
                                    year = monthKey.takeLast(4),
                                )
                            }.sortedByDescending { it.month }
                        emit(ResponseResult.Success(list))
                    }
            } catch (e: Exception) {
                emit(ResponseResult.Error(e))
            }
        }
}
