package domain.model

enum class FinanceEnum(
    val financeName: String,
) {
    EXPENSE(
        financeName = "Expenses",
    ),
    INCOME(
        financeName = "Income",
    ),
}
