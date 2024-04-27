package presentation.viewmodel.editincome

import kotlinx.coroutines.Job
import model.CategoryEnum

interface EditIncomeScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun edit(): Job
    fun delete(): Job

    fun getIncome(
        id: Long,
    ): Job
}
