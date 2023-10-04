package data.firebase

import core.network.ResponseResult
import data.model.Expense
import data.model.Month
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where
import kotlinx.datetime.LocalDate
import model.CategoryEnum
import model.FinanceEnum
import model.MonthModel
import utils.COLLECTION_EXPENSE
import utils.COLLECTION_INCOME
import utils.COLLECTION_MONTH
import utils.toLocalDate
import utils.toMonthString

class FirebaseFinance constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    private val userId = "123"

    suspend fun createExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long
    ): ResponseResult<Unit> =
        try {
            create(
                collection = COLLECTION_EXPENSE,
                dateInMillis = dateInMillis,
                category = category,
                amount = amount,
                note = note
            )
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun createIncome(
        amount: Int,
        note: String,
        dateInMillis: Long
    ): ResponseResult<Unit> =
        try {
            val category = CategoryEnum.WORK.name
            create(
                collection = COLLECTION_INCOME,
                dateInMillis = dateInMillis,
                category = category,
                amount = amount,
                note = note
            )
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    private suspend fun create(
        collection: String,
        dateInMillis: Long,
        category: String,
        amount: Int,
        note: String
    ): ResponseResult<Unit> {
        val date: LocalDate = dateInMillis.toLocalDate()
        val currentMonthKey = "${date.month.toMonthString()}${date.year}"
        val batch = firebaseFirestore.batch()
        val firebaseReference =
            firebaseFirestore.collection(collection).document
        val monthReference =
            firebaseFirestore.collection(COLLECTION_MONTH).document(
                userId
            )
        val expense = Expense(
            amount = amount,
            category = category,
            userId = userId,
            note = note,
            month = currentMonthKey,
            dateInMillis = dateInMillis
        )
        batch.set(
            firebaseReference,
            expense.toMap()
        )
        batch.set(
            documentRef = monthReference,
            data = mapOf(
                "months" to mapOf(currentMonthKey to null)
            ),
            merge = true
        )

        batch.commit()
        return ResponseResult.Success(Unit)
    }

    suspend fun editExpense(
        amount: Int,
        category: String,
        note: String,
        dateInMillis: Long,
        id: String
    ): ResponseResult<Unit> =
        try {
            edit(
                collection = COLLECTION_EXPENSE,
                dateInMillis = dateInMillis,
                category = category,
                amount = amount,
                note = note,
                id = id
            )
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun editIncome(
        amount: Int,
        note: String,
        dateInMillis: Long,
        id: String
    ): ResponseResult<Unit> =
        try {
            val category = CategoryEnum.WORK.name
            edit(
                collection = COLLECTION_INCOME,
                dateInMillis = dateInMillis,
                category = category,
                amount = amount,
                note = note,
                id = id
            )
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    private suspend fun edit(
        collection: String,
        dateInMillis: Long,
        category: String,
        amount: Int,
        note: String,
        id: String
    ): ResponseResult<Unit> {
        val date: LocalDate = dateInMillis.toLocalDate()
        val currentMonthKey = "${date.month.toMonthString()}${date.year}"
        val batch = firebaseFirestore.batch()
        val firebaseReference =
            firebaseFirestore.collection(collection).document(id)
        val monthReference =
            firebaseFirestore.collection(COLLECTION_MONTH).document(
                userId
            )
        val expense = Expense(
            amount = amount,
            category = category,
            userId = userId,
            note = note,
            month = currentMonthKey,
            dateInMillis = dateInMillis
        )
        batch.update(
            firebaseReference,
            expense.toMap()
        )
        batch.set(
            documentRef = monthReference,
            data = mapOf(
                "months" to mapOf(currentMonthKey to null)
            ),
            merge = true
        )

        batch.commit()
        return ResponseResult.Success(Unit)
    }


    suspend fun delete(
        financeEnum: FinanceEnum,
        id: String
    ): ResponseResult<Unit> =
        try {
            val collection = if (financeEnum == FinanceEnum.EXPENSE) {
                COLLECTION_EXPENSE
            } else {
                COLLECTION_INCOME
            }
            firebaseFirestore.collection(collection).document(id).delete()
            ResponseResult.Success(Unit)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getCategoryMonthDetail(
        categoryEnum: CategoryEnum,
        monthKey: String
    ): ResponseResult<List<Expense>> =
        try {
            val collection = if (categoryEnum.type == FinanceEnum.EXPENSE) {
                COLLECTION_EXPENSE
            } else {
                COLLECTION_INCOME
            }
            val costsResponse =
                firebaseFirestore.collection(collection)
                    .where("userId", userId)
                    .where("month", monthKey)
                    .where("category", categoryEnum.name)
                    .orderBy("timestamp", Direction.DESCENDING).get()

            val list = costsResponse.documents.map {
                it.data<Expense>().copy(
                    id = it.id
                )
            }
            ResponseResult.Success(list)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getAllMonthExpenses(
        monthKey: String
    ): ResponseResult<List<Expense>> =
        try {
            val costsResponse =
                firebaseFirestore.collection(COLLECTION_EXPENSE)
                    .where("month", monthKey)
                    .where("userId", userId)
                    .orderBy("timestamp", Direction.DESCENDING).get()

            val list = costsResponse.documents.map {
                it.data<Expense>()
            }
            ResponseResult.Success(list)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getAllMonthIncome(
        monthKey: String
    ): ResponseResult<List<Expense>> =
        try {
            val costsResponse =
                firebaseFirestore.collection(COLLECTION_INCOME)
                    .where("month", monthKey)
                    .where("userId", userId)
                    .orderBy("timestamp", Direction.DESCENDING).get()

            val list = costsResponse.documents.map {
                it.data<Expense>()
            }
            ResponseResult.Success(list)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }

    suspend fun getMonths(): ResponseResult<List<MonthModel>> =
        try {
            val monthResponse =
                firebaseFirestore.collection(COLLECTION_MONTH).document(userId).get()
            if (monthResponse.exists) {
                val monthList = monthResponse.data<Month>().months.map { monthKey ->
                    MonthModel(
                        id = monthKey.key,
                        month = monthKey.key.take(2),
                        year = monthKey.key.takeLast(4)
                    )
                }.sortedByDescending { it.month }
                ResponseResult.Success(monthList)
            } else {
                ResponseResult.Error(Throwable("No hay meses"))
            }
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
}
