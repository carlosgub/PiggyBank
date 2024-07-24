package com.carlosgub.myfinances.test.mock

import com.carlosgub.myfinances.core.utils.createLocalDateTime
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.core.utils.isLeapYear
import com.carlosgub.myfinances.core.utils.monthLength
import com.carlosgub.myfinances.core.utils.toLocalDate
import com.carlosgub.myfinances.core.utils.toLocaleString
import com.carlosgub.myfinances.core.utils.toMonthKey
import com.carlosgub.myfinances.domain.model.CategoryEnum
import com.carlosgub.myfinances.domain.model.ExpenseScreenModel
import com.carlosgub.myfinances.domain.model.FinanceLocalDate
import com.carlosgub.myfinances.domain.model.FinanceModel
import com.carlosgub.myfinances.domain.model.FinanceScreenExpenses
import com.carlosgub.myfinances.domain.model.FinanceScreenModel
import com.carlosgub.myfinances.domain.model.MonthDetailScreenModel
import com.carlosgub.myfinances.domain.model.MonthExpense
import com.carlosgub.myfinances.domain.model.MonthModel
import expense.Expense
import income.Income
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

val expenseOne =
    Expense(
        id = 1L,
        amount = 100L,
        note = "expenseOne",
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.CLOTHES.name,
        month = getCurrentMonthKey(),
        dateInMillis = 0L,
    )

val expenseTwo =
    Expense(
        id = 2L,
        amount = 200L,
        note = "expenseTwo",
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.TAXI.name,
        month = getCurrentMonthKey(),
        dateInMillis = 0L,
    )

val expenseThree =
    Expense(
        id = 3L,
        amount = 300L,
        note = "expenseThree",
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.LOVE.name,
        month = getCurrentMonthKey(),
        dateInMillis = 0L,
    )

val expenseLocalDateOne =
    FinanceLocalDate(
        expenseOne.dateInMillis.toLocalDate(),
    )

val expenseFinanceModelOne =
    FinanceModel(
        id = expenseOne.id,
        amount = expenseOne.amount,
        note = expenseOne.note,
        category = expenseOne.category,
        monthKey = expenseOne.month,
        date = expenseLocalDateOne.date,
        localDateTime = expenseLocalDateOne.localDateTime,
    )

val expensesList =
    listOf(
        expenseOne,
        expenseTwo,
        expenseThree,
    )

val incomeOne =
    Income(
        id = 1L,
        amount = 100L,
        note = "incomeOne",
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK.name,
        month = getCurrentMonthKey(),
        dateInMillis = 0L,
    )

val incomeTwo =
    Income(
        id = 2L,
        amount = 200L,
        note = "incomeTwo",
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK.name,
        month = getCurrentMonthKey(),
        dateInMillis = 0L,
    )

val incomeLocalDateOne =
    FinanceLocalDate(incomeOne.dateInMillis.toLocalDate())

val incomeFinanceModelOne =
    FinanceModel(
        id = incomeOne.id,
        amount = incomeOne.amount,
        note = incomeOne.note,
        category = incomeOne.category,
        monthKey = incomeOne.month,
        date = incomeLocalDateOne.date,
        localDateTime = incomeLocalDateOne.localDateTime,
    )

val incomeThree =
    Income(
        id = 3L,
        amount = 300L,
        note = "incomeThree",
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK.name,
        month = getCurrentMonthKey(),
        dateInMillis = 0L,
    )

val incomeList =
    listOf(
        incomeOne,
        incomeTwo,
        incomeThree,
    )

val financeScreenExpensesOne =
    FinanceScreenExpenses(
        count = 1,
        amount = 100L,
        percentage = 17,
        category = CategoryEnum.CLOTHES,
    )

val financeScreenExpensesTwo =
    FinanceScreenExpenses(
        count = 1,
        amount = 200L,
        percentage = 33,
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.TAXI,
    )

val financeScreenExpensesThree =
    FinanceScreenExpenses(
        count = 1,
        amount = 300L,
        percentage = 50,
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.LOVE,
    )

val financeScreenExpensesLists =
    persistentListOf(
        financeScreenExpensesThree,
        financeScreenExpensesTwo,
        financeScreenExpensesOne,
    )

val financeScreenIncomeOne =
    FinanceScreenExpenses(
        count = 3,
        amount = 600L,
        percentage = 100,
        category = com.carlosgub.myfinances.domain.model.CategoryEnum.WORK,
    )

val financeScreenIncomeLists =
    persistentListOf(
        financeScreenIncomeOne,
    )

val expenseScreenModelOne =
    ExpenseScreenModel(
        id = expenseOne.id,
        amount = expenseOne.amount,
        note = expenseOne.note.replaceFirstChar { it.uppercase() },
        category = expenseOne.category,
        localDateTime = expenseLocalDateOne.localDateTime,
        date = expenseLocalDateOne.date,
    )

val localDateTwo =
    FinanceLocalDate(expenseTwo.dateInMillis.toLocalDate())

val expenseScreenModelTwo =
    ExpenseScreenModel(
        id = expenseTwo.id,
        amount = expenseTwo.amount,
        note = expenseTwo.note.replaceFirstChar { it.uppercase() },
        category = expenseTwo.category,
        localDateTime = localDateTwo.localDateTime,
        date = localDateTwo.date,
    )

val localDateThree =
    FinanceLocalDate(expenseThree.dateInMillis.toLocalDate())

val expenseScreenModelThree =
    ExpenseScreenModel(
        id = expenseThree.id,
        amount = expenseThree.amount,
        note = expenseThree.note.replaceFirstChar { it.uppercase() },
        category = expenseThree.category,
        localDateTime = localDateThree.localDateTime,
        date = localDateThree.date,
    )

val incomeScreenModelOne =
    ExpenseScreenModel(
        id = incomeOne.id,
        amount = incomeOne.amount,
        note = incomeOne.note.replaceFirstChar { it.uppercase() },
        category = incomeOne.category,
        localDateTime = incomeLocalDateOne.localDateTime,
        date = incomeLocalDateOne.date,
    )

val incomeLocalDateTwo =
    FinanceLocalDate(incomeTwo.dateInMillis.toLocalDate())

val incomeScreenModelTwo =
    ExpenseScreenModel(
        id = incomeTwo.id,
        amount = incomeTwo.amount,
        note = incomeTwo.note.replaceFirstChar { it.uppercase() },
        category = incomeTwo.category,
        localDateTime = incomeLocalDateTwo.localDateTime,
        date = incomeLocalDateTwo.date,
    )

val incomeLocalDateThree =
    FinanceLocalDate(incomeThree.dateInMillis.toLocalDate())

val incomeScreenModelThree =
    ExpenseScreenModel(
        id = incomeThree.id,
        amount = incomeThree.amount,
        note = incomeThree.note.replaceFirstChar { it.uppercase() },
        category = incomeThree.category,
        localDateTime = incomeLocalDateThree.localDateTime,
        date = incomeLocalDateThree.date,
    )

val expenseScreenModelList =
    listOf(
        expenseScreenModelThree,
        expenseScreenModelTwo,
        expenseScreenModelOne,
    )

val incomeScreenModelList =
    listOf(
        incomeScreenModelOne,
        incomeScreenModelTwo,
        incomeScreenModelThree,
    )

val monthOne =
    MonthModel(
        id = "062024",
        month = "06",
        year = "2024",
    )

val monthTwo =
    MonthModel(
        id = "012024",
        month = "01",
        year = "2024",
    )

val monthThree =
    MonthModel(
        id = "022024",
        month = "02",
        year = "2024",
    )

val monthList =
    listOf(
        monthOne,
        monthTwo,
        monthThree,
    )

val date =
    createLocalDateTime(
        year = getCurrentMonthKey().substring(2, 6).toInt(),
        monthNumber = getCurrentMonthKey().substring(0, 2).trimStart('0').toInt(),
    )

val daySpentFinanceScreenModel =
    (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
        val dateInternal =
            createLocalDateTime(
                year = date.year,
                monthNumber = date.monthNumber,
                dayOfMonth = day,
            )
        dateInternal to expenseScreenModelList.filter { expense ->
            expense.localDateTime == dateInternal
        }.sumOf { it.amount }
    }.toImmutableMap()

val financeScreenModelMock =
    FinanceScreenModel(
        month = date.month.toLocaleString(),
        expenseAmount = expensesList.sumOf { it.amount },
        monthExpense = MonthExpense(
            incomeTotal = incomeList.sumOf { it.amount },
            percentage = 100,
        ),
        expenses = financeScreenExpensesLists,
        income = financeScreenIncomeLists,
        daySpent = daySpentFinanceScreenModel,
    )

val daySpentMonthExpenseDetailScreenModel =
    (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
        val dateInternal =
            createLocalDateTime(
                year = date.year,
                monthNumber = date.monthNumber,
                dayOfMonth = day,
            )
        dateInternal to listOf(
            expenseScreenModelOne,
        ).filter { expense ->
            expense.localDateTime == dateInternal
        }.sumOf { it.amount }
    }.toImmutableMap()

val monthExpenseDetailScreenModel =
    MonthDetailScreenModel(
        monthAmount = expenseOne.amount,
        expenseScreenModel = listOf(expenseScreenModelOne),
        daySpent = daySpentMonthExpenseDetailScreenModel,
    )

val daySpentMonthIncomeDetailScreenModel =
    (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
        val dateInternal =
            createLocalDateTime(
                year = date.year,
                monthNumber = date.monthNumber,
                dayOfMonth = day,
            )
        dateInternal to incomeScreenModelList.filter { expense ->
            expense.localDateTime == dateInternal
        }.sumOf { it.amount }
    }.toImmutableMap()

val monthIncomeDetailScreenModel =
    MonthDetailScreenModel(
        monthAmount = incomeList.sumOf { it.amount },
        expenseScreenModel = incomeScreenModelList,
        daySpent = daySpentMonthIncomeDetailScreenModel,
    )

val monthListFiltered =
    monthList.map { month ->
        createLocalDateTime(
            year = month.year.toInt(),
            monthNumber = month.month.trimStart('0').toInt(),
        )
    }.filter { localDateTime ->
        localDateTime.toMonthKey() != getCurrentMonthKey()
    }.groupBy { localDateTime ->
        localDateTime.year
    }.toImmutableMap()
