package data.repository.source.database

import domain.model.CategoryEnum
import domain.model.ExpenseScreenModel
import domain.model.FinanceLocalDate
import domain.model.FinanceModel
import domain.model.FinanceScreenExpenses
import domain.model.FinanceScreenModel
import domain.model.MonthExpense
import domain.model.MonthModel
import expense.Expense
import income.Income
import utils.createLocalDateTime
import utils.getCurrentMonthKey
import utils.isLeapYear
import utils.monthLength
import utils.toLocalDate

val expenseOne = Expense(
    id = 1L,
    amount = 100L,
    note = "expenseOne",
    category = CategoryEnum.CLOTHES.name,
    month = getCurrentMonthKey(),
    dateInMillis = 0L
)

val expenseTwo = Expense(
    id = 2L,
    amount = 200L,
    note = "expenseTwo",
    category = CategoryEnum.TAXI.name,
    month = getCurrentMonthKey(),
    dateInMillis = 0L
)

val expenseThree = Expense(
    id = 3L,
    amount = 300L,
    note = "expenseThree",
    category = CategoryEnum.LOVE.name,
    month = getCurrentMonthKey(),
    dateInMillis = 0L
)

val expenseLocalDateOne = FinanceLocalDate(expenseOne.dateInMillis.toLocalDate())

val expenseFinanceModelOne = FinanceModel(
    id = expenseOne.id,
    amount = expenseOne.amount,
    note = expenseOne.note,
    category = expenseOne.category,
    monthKey = expenseOne.month,
    date = expenseLocalDateOne.date,
    localDateTime = expenseLocalDateOne.localDateTime,
)

val expensesList = listOf(
    expenseOne,
    expenseTwo,
    expenseThree
)

val incomeOne = Income(
    id = 1L,
    amount = 100L,
    note = "incomeOne",
    category = CategoryEnum.WORK.name,
    month = getCurrentMonthKey(),
    dateInMillis = 0L
)

val incomeTwo = Income(
    id = 2L,
    amount = 200L,
    note = "incomeTwo",
    category = CategoryEnum.WORK.name,
    month = getCurrentMonthKey(),
    dateInMillis = 0L
)

val incomeLocalDateOne = FinanceLocalDate(incomeOne.dateInMillis.toLocalDate())

val incomeFinanceModelOne = FinanceModel(
    id = incomeOne.id,
    amount = incomeOne.amount,
    note = incomeOne.note,
    category = incomeOne.category,
    monthKey = incomeOne.month,
    date = incomeLocalDateOne.date,
    localDateTime = incomeLocalDateOne.localDateTime,
)

val incomeThree = Income(
    id = 3L,
    amount = 300L,
    note = "incomeThree",
    category = CategoryEnum.WORK.name,
    month = getCurrentMonthKey(),
    dateInMillis = 0L
)

val incomeList = listOf(
    incomeOne,
    incomeTwo,
    incomeThree
)

val financeScreenExpensesOne = FinanceScreenExpenses(
    count = 1,
    amount = 100L,
    percentage = 17,
    category = CategoryEnum.CLOTHES
)

val financeScreenExpensesTwo = FinanceScreenExpenses(
    count = 1,
    amount = 200L,
    percentage = 33,
    category = CategoryEnum.TAXI
)

val financeScreenExpensesThree = FinanceScreenExpenses(
    count = 1,
    amount = 300L,
    percentage = 50,
    category = CategoryEnum.LOVE,
)

val financeScreenExpensesLists = listOf(
    financeScreenExpensesThree,
    financeScreenExpensesTwo,
    financeScreenExpensesOne,
)

val financeScreenIncomeOne = FinanceScreenExpenses(
    count = 3,
    amount = 600L,
    percentage = 100,
    category = CategoryEnum.WORK,
)

val financeScreenIncomeLists = listOf(
    financeScreenIncomeOne
)

val expenseScreenModelOne = ExpenseScreenModel(
    id = expenseOne.id,
    amount = expenseOne.amount,
    note = expenseOne.note,
    category = expenseOne.category,
    localDateTime = expenseLocalDateOne.localDateTime,
    date = expenseLocalDateOne.date,
)

val localDateTwo = FinanceLocalDate(expenseTwo.dateInMillis.toLocalDate())

val expenseScreenModelTwo = ExpenseScreenModel(
    id = expenseTwo.id,
    amount = expenseTwo.amount,
    note = expenseTwo.note,
    category = expenseTwo.category,
    localDateTime = localDateTwo.localDateTime,
    date = localDateTwo.date,
)

val localDateThree = FinanceLocalDate(expenseThree.dateInMillis.toLocalDate())

val expenseScreenModelThree = ExpenseScreenModel(
    id = expenseThree.id,
    amount = expenseThree.amount,
    note = expenseThree.note,
    category = expenseThree.category,
    localDateTime = localDateThree.localDateTime,
    date = localDateThree.date,
)

val expenseScreenModelList = listOf(
    expenseScreenModelThree,
    expenseScreenModelTwo,
    expenseScreenModelOne
)

val monthOne = MonthModel(
    id = "062024",
    month = "06",
    year = "2024"
)

val monthTwo = MonthModel(
    id = "012024",
    month = "01",
    year = "2024"
)

val monthThree = MonthModel(
    id = "022024",
    month = "02",
    year = "2024"
)

val monthList = listOf(
    monthOne,
    monthTwo,
    monthThree
)

val date =
    createLocalDateTime(
        year = getCurrentMonthKey().substring(2, 6).toInt(),
        monthNumber = getCurrentMonthKey().substring(0, 2).trimStart('0').toInt(),
    )

val daySpent =
    (1..date.monthNumber.monthLength(isLeapYear(date.year))).associate { day ->
        val dateInternal =
            createLocalDateTime(
                year = date.year,
                monthNumber = date.monthNumber,
                dayOfMonth = day,
            )
        dateInternal to
                expenseScreenModelList.filter { expense ->
                    expense.localDateTime == dateInternal
                }.sumOf { it.amount }
    }

val financeScreenModel = FinanceScreenModel(
    month = date.month,
    expenseAmount = expensesList.sumOf { it.amount },
    monthExpense = MonthExpense(
        incomeTotal = incomeList.sumOf { it.amount } / 100.0,
        percentage = 100
    ),
    expenses = financeScreenExpensesLists,
    income = financeScreenIncomeLists,
    daySpent = daySpent
)