package mock

import com.carlosgub.myfinances.core.utils.createLocalDateTime
import com.carlosgub.myfinances.core.utils.getCurrentMonthKey
import com.carlosgub.myfinances.core.utils.isLeapYear
import com.carlosgub.myfinances.core.utils.monthLength
import com.carlosgub.myfinances.core.utils.toLocalDate
import com.carlosgub.myfinances.core.utils.toMonthKey
import domain.model.CategoryEnum
import domain.model.ExpenseScreenModel
import domain.model.FinanceLocalDate
import domain.model.FinanceModel
import domain.model.FinanceScreenExpenses
import domain.model.FinanceScreenModel
import domain.model.MonthDetailScreenModel
import domain.model.MonthExpense
import domain.model.MonthModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap

val expenseLocalDateOne =
    FinanceLocalDate(
        0L.toLocalDate(),
    )

val expenseFinanceModelOne =
    FinanceModel(
        id = 1L,
        amount = 100,
        note = "expenseOne",
        category = CategoryEnum.TAXI.name,
        monthKey = getCurrentMonthKey(),
        date = expenseLocalDateOne.date,
        localDateTime = expenseLocalDateOne.localDateTime,
    )

val incomeLocalDateOne =
    FinanceLocalDate(0L.toLocalDate())

val incomeFinanceModelOne =
    FinanceModel(
        id = 1L,
        amount = 100,
        note = "incomeOne",
        category = CategoryEnum.WORK.name,
        monthKey = getCurrentMonthKey(),
        date = incomeLocalDateOne.date,
        localDateTime = incomeLocalDateOne.localDateTime,
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
        category = CategoryEnum.TAXI,
    )

val financeScreenExpensesThree =
    FinanceScreenExpenses(
        count = 1,
        amount = 300L,
        percentage = 50,
        category = CategoryEnum.LOVE,
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
        category = CategoryEnum.WORK,
    )

val financeScreenIncomeLists =
    persistentListOf(
        financeScreenIncomeOne,
    )

val expenseScreenModelOne =
    ExpenseScreenModel(
        id = 1L,
        amount = 100,
        note = "ExpenseOne",
        category = financeScreenExpensesOne.category.name,
        localDateTime = expenseLocalDateOne.localDateTime,
        date = expenseLocalDateOne.date,
    )

val localDateTwo =
    FinanceLocalDate(0L.toLocalDate())

val expenseScreenModelTwo =
    ExpenseScreenModel(
        id = 2L,
        amount = 200,
        note = "ExpenseTwo",
        category = financeScreenExpensesTwo.category.name,
        localDateTime = localDateTwo.localDateTime,
        date = localDateTwo.date,
    )

val localDateThree =
    FinanceLocalDate(0L.toLocalDate())

val expenseScreenModelThree =
    ExpenseScreenModel(
        id = 3L,
        amount = 300,
        note = "ExpenseThree",
        category = financeScreenExpensesThree.category.name,
        localDateTime = localDateThree.localDateTime,
        date = localDateThree.date,
    )

val incomeScreenModelOne =
    ExpenseScreenModel(
        id = 1L,
        amount = 100,
        note = "IncomeOne",
        category = financeScreenIncomeOne.category.name,
        localDateTime = incomeLocalDateOne.localDateTime,
        date = incomeLocalDateOne.date,
    )

val incomeLocalDateTwo =
    FinanceLocalDate(0L.toLocalDate())

val incomeScreenModelTwo =
    ExpenseScreenModel(
        id = 2L,
        amount = 200,
        note = "IncomeTwo",
        category = CategoryEnum.WORK.name,
        localDateTime = incomeLocalDateTwo.localDateTime,
        date = incomeLocalDateTwo.date,
    )

val incomeLocalDateThree =
    FinanceLocalDate(0L.toLocalDate())

val incomeScreenModelThree =
    ExpenseScreenModel(
        id = 3L,
        amount = 300,
        note = "IncomeThree",
        category = CategoryEnum.WORK.name,
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

val financeScreenModel =
    FinanceScreenModel(
        month = date.month,
        expenseAmount = expenseScreenModelList.sumOf { it.amount },
        monthExpense = MonthExpense(
            incomeTotal = incomeScreenModelList.sumOf { it.amount } / 100.0,
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
        monthAmount = 100,
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
        monthAmount = incomeScreenModelList.sumOf { it.amount },
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
