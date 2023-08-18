package data.firebase

import core.network.ResponseResult
import dev.gitlive.firebase.firestore.FirebaseFirestore
import model.Expense
import model.Finance
import model.FinanceMonthCategoryDetail
import utils.COLLECTION_COSTS
import utils.COLLECTION_EXPENSE
import utils.COLLECTION_MONTH
import utils.getCurrentMonthKey

class FirebaseFinance constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    val userId = "123"
    suspend fun getFinance(): ResponseResult<Finance> =
        try {
            val costsResponse =
                firebaseFirestore.collection(COLLECTION_COSTS).document(userId)
                    .collection(COLLECTION_MONTH)
                    .document(getCurrentMonthKey()).get()
            if(costsResponse.exists){
                val finance = costsResponse.data<Finance>()
                ResponseResult.Success(finance)
            }else{
                ResponseResult.Success(Finance())
            }

        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String
    ): ResponseResult<Unit> =
        try {
            val currentMonthKey = getCurrentMonthKey()
            val batch = firebaseFirestore.batch()
            val expenseReference =
                firebaseFirestore.collection(COLLECTION_EXPENSE).document
            val betReference = firebaseFirestore.collection(COLLECTION_COSTS).document(userId)
                .collection(COLLECTION_MONTH)
                .document(getCurrentMonthKey())
            val costsResponse =
                firebaseFirestore.collection(COLLECTION_COSTS).document(userId)
                    .collection(COLLECTION_MONTH)
                    .document(getCurrentMonthKey()).get()
            val financeExist: Boolean
            val financeCache = if (costsResponse.exists) {
                financeExist = true
                val finance = costsResponse.data<Finance>()
                finance.copy(
                    expenseAmount = finance.expenseAmount + amount,
                    category =
                    finance.category.toMutableMap().apply {
                        if (finance.category.containsKey(category)) {
                            put(
                                category,
                                FinanceMonthCategoryDetail(
                                    amount = finance.category[category]!!.amount + amount,
                                    count = finance.category[category]!!.count + 1
                                )
                            )
                        } else {
                            put(
                                category,
                                FinanceMonthCategoryDetail(
                                    amount = amount,
                                    count = 1
                                )
                            )
                        }
                    }
                )
            } else {
                financeExist = false
                Finance(
                    expenseAmount = amount,
                    category = mapOf(
                        category to FinanceMonthCategoryDetail(
                            amount = amount,
                            count = 1
                        )
                    )
                )
            }

            val expense = Expense(
                amount = amount,
                category = category,
                userId = userId,
                note = note,
                month = currentMonthKey
            )
            if(financeExist){
                batch.update(betReference, financeCache)
            }else{
                batch.set(betReference, financeCache)
            }
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
