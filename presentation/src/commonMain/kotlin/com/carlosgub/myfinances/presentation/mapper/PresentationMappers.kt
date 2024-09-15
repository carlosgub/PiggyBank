package com.carlosgub.myfinances.presentation.mapper

import com.carlosgub.myfinances.core.mapper.Mapper
import com.carlosgub.myfinances.domain.model.ExpenseModel
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.model.MonthDetailModel
import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import com.carlosgub.myfinances.presentation.model.FinanceScreenModel
import com.carlosgub.myfinances.presentation.model.MonthDetailScreenModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

object FinanceModelToFinanceScreenModel : Mapper<FinanceModel, FinanceScreenModel> {
    override fun map(from: FinanceModel) =
        FinanceScreenModel(
            month = from.month,
            expenseAmount = from.expenseAmount,
            monthExpense = from.monthExpense,
            expenses = from.expenses.toImmutableList(),
            income = from.income.toImmutableList(),
            daySpent = from.daySpent.toImmutableMap(),
        )
}

object MonthDetailModelToMonthDetailScreenModel : Mapper<MonthDetailModel, MonthDetailScreenModel> {
    override fun map(from: MonthDetailModel) =
        MonthDetailScreenModel(
            monthAmount = from.monthAmount,
            daySpent = from.daySpent.toImmutableMap(),
            expenseScreenModel = from.expenseModel.map { ExpenseModelToExpenseScreenModel.map(it) }.toImmutableList(),
        )
}

object ExpenseModelToExpenseScreenModel : Mapper<ExpenseModel, ExpenseScreenModel> {
    override fun map(from: ExpenseModel) =
        ExpenseScreenModel(
            amount = from.amount,
            id = from.id,
            note = from.note,
            category = from.category,
            localDateTime = from.localDateTime,
            date = from.date,
        )
}
