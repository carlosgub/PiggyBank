package presentation.viewmodel.createexpense

import kotlinx.coroutines.Job
import domain.model.CategoryEnum
import domain.model.FinanceEnum

interface CreateExpenseScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun create(): Job
}
