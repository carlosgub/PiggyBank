package com.carlosgub.myfinances.domain.repository

import com.carlosgub.myfinances.core.state.GenericState
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.model.ExpenseModel
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.model.IncomeModel
import com.carlosgub.myfinances.domain.model.MonthDetailExpenseModel
import com.carlosgub.myfinances.domain.model.MonthDetailIncomeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface FinanceRepository {
    suspend fun getFinance(monthKey: String): Flow<GenericState<FinanceModel>>

    suspend fun getExpense(id: Long): GenericState<ExpenseModel>

    suspend fun getIncome(id: Long): GenericState<IncomeModel>

    suspend fun createExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit>

    suspend fun createIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
    ): GenericState<Unit>

    suspend fun editExpense(
        amount: Long,
        category: String,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): GenericState<Unit>

    suspend fun editIncome(
        amount: Long,
        note: String,
        dateInMillis: Long,
        id: Long,
    ): GenericState<Unit>

    suspend fun deleteIncome(
        id: Long,
        monthKey: String,
    ): GenericState<Unit>

    suspend fun deleteExpense(
        id: Long,
        monthKey: String,
    ): GenericState<Unit>

    suspend fun getExpenseMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String,
    ): Flow<GenericState<MonthDetailExpenseModel>>

    suspend fun getIncomeMonthDetail(monthKey: String): Flow<GenericState<MonthDetailIncomeModel>>

    suspend fun getMonths(): Flow<GenericState<Map<Int, List<LocalDateTime>>>>
}
