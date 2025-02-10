package com.carlosgub.myfinances.presentation.mapper

import com.carlosgub.myfinances.core.mapper.Mapper
import com.carlosgub.myfinances.domain.model.ExpenseModel
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.model.IncomeModel
import com.carlosgub.myfinances.domain.model.MonthDetailExpenseModel
import com.carlosgub.myfinances.domain.model.MonthDetailIncomeModel
import com.carlosgub.myfinances.presentation.model.ExpenseScreenModel
import com.carlosgub.myfinances.presentation.model.FinanceScreenModel
import com.carlosgub.myfinances.presentation.model.IncomeScreenModel
import com.carlosgub.myfinances.presentation.model.MonthDetailExpenseScreenModel
import com.carlosgub.myfinances.presentation.model.MonthDetailIncomeScreenModel
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

object MonthDetailExpenseModelToMonthDetailExpenseScreenModel : Mapper<MonthDetailExpenseModel, MonthDetailExpenseScreenModel> {
    override fun map(from: MonthDetailExpenseModel) =
        MonthDetailExpenseScreenModel(
            monthAmount = from.monthAmount,
            daySpent = from.daySpent.toImmutableMap(),
            expenseScreenModelList = from.expenseModelList.map { ExpenseModelToExpenseScreenModel.map(it) }.toImmutableList(),
        )
}

object MonthDetailIncomeModelToMonthDetailIncomeScreenModel : Mapper<MonthDetailIncomeModel, MonthDetailIncomeScreenModel> {
    override fun map(from: MonthDetailIncomeModel) =
        MonthDetailIncomeScreenModel(
            monthAmount = from.monthAmount,
            daySpent = from.daySpent.toImmutableMap(),
            incomeScreenModelList = from.incomeModelList.map { IncomeModelToIncomeScreenModel.map(it) }.toImmutableList(),
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

object IncomeModelToIncomeScreenModel : Mapper<IncomeModel, IncomeScreenModel> {
    override fun map(from: IncomeModel) =
        IncomeScreenModel(
            amount = from.amount,
            id = from.id,
            note = from.note,
            category = from.category,
            localDateTime = from.localDateTime,
            date = from.date,
        )
}
