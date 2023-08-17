package data.firebase

import core.network.ResponseResult
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import model.Expense
import model.Finance
import model.FinanceMonthCategoryDetail
import model.FinanceMonthDetail
import utils.toMoneyFormat

class FirebaseFinance constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    val userId = "123"
    suspend fun getFinance(): ResponseResult<Finance> =
        try {
            val costsResponse = firebaseFirestore.collection("COSTS").document(userId).get()
            val finance = costsResponse.data<Finance>()
            ResponseResult.Success(finance)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String
    ): ResponseResult<Unit> =
        try {
            val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val year = today.year
            val month = today.month
            val expenseMonth = "${
                if (month.number < 10) {
                    "0${month.number}"
                } else {
                    month.number
                }
            }${year}"
            val batch = firebaseFirestore.batch()
            val expenseReference =
                firebaseFirestore.collection("EXPENSE").document
            val betReference = firebaseFirestore.collection("COSTS").document(userId)
            val costsResponse = firebaseFirestore.collection("COSTS").document(userId).get()
            val finance = costsResponse.data<Finance>()
            val financeCache = if (finance.month.containsKey(expenseMonth)) {
                finance.copy(
                    month = mapOf(
                        expenseMonth to FinanceMonthDetail(
                            amount = finance.month[expenseMonth]!!.amount + amount,
                            category =
                            if (finance.month[expenseMonth]!!.category.containsKey(category)) {
                                mapOf(
                                    category to FinanceMonthCategoryDetail(
                                        amount = finance.month[expenseMonth]!!.category[category]!!.amount + amount,
                                        count = finance.month[expenseMonth]!!.category[category]!!.count + 1
                                    )
                                )
                            } else {
                                mapOf(
                                    category to FinanceMonthCategoryDetail(
                                        amount = amount,
                                        count = 1
                                    )
                                )
                            }
                        )
                    )
                )
            } else {
                finance.copy(
                    month = mapOf(
                        expenseMonth to FinanceMonthDetail(
                            amount = amount,
                            category = mapOf(
                                category to FinanceMonthCategoryDetail(
                                    amount = amount,
                                    count = 1
                                )
                            )
                        )
                    )
                )
            }

            val expense = Expense(
                amount = amount,
                category = category,
                userId = userId,
                note = note,
                month = expenseMonth
            )
            batch.update(betReference, financeCache)
            batch.set(
                expenseReference,
                expense
            )

            batch.commit()
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
}
