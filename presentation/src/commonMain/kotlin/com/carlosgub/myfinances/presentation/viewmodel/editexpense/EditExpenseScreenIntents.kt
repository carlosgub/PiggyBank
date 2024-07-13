package com.carlosgub.myfinances.presentation.viewmodel.editexpense

import domain.model.CategoryEnum
import kotlinx.coroutines.Job

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

    fun getExpense(id: Long): Job
}
