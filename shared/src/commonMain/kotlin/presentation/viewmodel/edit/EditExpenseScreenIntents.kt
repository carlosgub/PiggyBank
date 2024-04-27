package presentation.viewmodel.edit

import kotlinx.coroutines.Job
import model.CategoryEnum
import model.FinanceEnum

interface EditExpenseScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun edit(): Job
    fun delete(): Job

    fun getExpense(
        id: Long
    ): Job
}
