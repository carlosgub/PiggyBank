package data.firebase

import core.network.ResponseResult
import dev.gitlive.firebase.firestore.FirebaseFirestore
import model.Finance

class FirebaseFinance constructor(
    private val firebaseFirestore: FirebaseFirestore
) {
    suspend fun getFinance(): ResponseResult<Finance> =
        try {
            val betResponse = firebaseFirestore.collection("COSTS").document("123").get()
            val betFirestore = betResponse.data<Finance>()
            ResponseResult.Success(betFirestore)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
}
