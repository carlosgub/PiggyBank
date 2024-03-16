package presentation.viewmodel.create

import kotlinx.coroutines.Job
import model.CategoryEnum
import model.FinanceEnum

interface CreateScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun create(financeEnum: FinanceEnum): Job
}
