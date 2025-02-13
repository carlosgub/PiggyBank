package com.carlosgub.myfinances.presentation.screen.home.content

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.carlosgub.myfinances.core.utils.createLocalDateTime
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.core.utils.isLeapYear
import com.carlosgub.myfinances.core.utils.monthLength
import com.carlosgub.myfinances.core.utils.toLocalDate
import com.carlosgub.myfinances.core.utils.toLocaleString
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.model.ExpenseModel
import com.carlosgub.myfinances.domain.model.FinanceExpenses
import com.carlosgub.myfinances.domain.model.FinanceLocalDate
import com.carlosgub.myfinances.domain.model.MonthExpense
import com.carlosgub.myfinances.presentation.model.FinanceScreenModel
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeScreenState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

class HomeContentPreviewParameterProvider : PreviewParameterProvider<HomeScreenState> {
    private val expenseLocalDateOne =
        FinanceLocalDate(
            0L.toLocalDate(),
        )

    private val expenseModelOne =
        ExpenseModel(
            id = 1L,
            amount = 100L,
            note = "expenseOne",
            category = com.carlosgub.myfinances.domain.model.CategoryEnum.CLOTHES.name,
            monthKey = getCurrentMonthKey(),
            date = expenseLocalDateOne.date,
            localDateTime = expenseLocalDateOne.localDateTime,
        )

    private val expenseModelTwo =
        ExpenseModel(
            id = 2L,
            amount = 200L,
            note = "expenseOne",
            category = com.carlosgub.myfinances.domain.model.CategoryEnum.TAXI.name,
            monthKey = getCurrentMonthKey(),
            date = expenseLocalDateOne.date,
            localDateTime = expenseLocalDateOne.localDateTime,
        )

    private val expensesList =
        listOf(
            expenseModelOne,
            expenseModelTwo,
        )

    private val financeExpensesOne =
        FinanceExpenses(
            count = 1,
            amount = 100L,
            percentage = 17,
            category = CategoryEnum.CLOTHES,
        )

    private val financeExpensesList = listOf(
        financeExpensesOne,
    )

    private val date =
        createLocalDateTime(
            year = getCurrentMonthKey().substring(2, 6).toInt(),
            monthNumber = getCurrentMonthKey().substring(0, 2).trimStart('0').toInt(),
        )

    private val daySpentFinanceScreenModel =
        (1..date.monthNumber.monthLength(isLeapYear(date.year)))
            .associate { day ->
                val dateInternal =
                    createLocalDateTime(
                        year = date.year,
                        monthNumber = date.monthNumber,
                        dayOfMonth = day,
                    )
                dateInternal to expensesList
                    .filter { expense ->
                        expense.localDateTime == dateInternal
                    }.sumOf { it.amount }
            }.toImmutableMap()

    private val financeScreenModel =
        FinanceScreenModel(
            month = date.month.toLocaleString(),
            expenseAmount = expensesList.sumOf { it.amount },
            monthExpense = MonthExpense(
                incomeTotal = 0L,
                percentage = 100,
            ),
            expenses = financeExpensesList.toImmutableList(),
            income = persistentListOf(),
            daySpent = daySpentFinanceScreenModel,
        )

    override val values = sequenceOf(
        HomeScreenState(
            showLoading = true,
            isInitialDataLoaded = false,
            monthKey = getCurrentMonthKey(),
            financeScreenModel = financeScreenModel,
        ),
        HomeScreenState(
            showLoading = false,
            isInitialDataLoaded = true,
            monthKey = getCurrentMonthKey(),
            financeScreenModel = financeScreenModel,
        ),
    )
}
