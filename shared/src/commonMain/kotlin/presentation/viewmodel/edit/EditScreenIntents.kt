package presentation.viewmodel.edit

import kotlinx.coroutines.Job
import model.CategoryEnum
import model.ExpenseScreenModel
import model.FinanceEnum

interface EditScreenIntents {
    fun setCategory(categoryEnum: CategoryEnum): Job
    fun setAmount(textFieldValue: String): Job
    fun showDateError(boolean: Boolean): Job
    fun showNoteError(boolean: Boolean): Job
    fun showError(boolean: Boolean): Job
    fun setNote(note: String): Job
    fun setDate(date: Long): Job
    fun create(financeEnum: FinanceEnum, id: Long): Job
    fun delete(
        id: Long,
        financeEnum: FinanceEnum,
        monthKey: String
    ): Job

    fun updateValues(
        expenseScreenModel: ExpenseScreenModel
    ): Job
}
