package com.carlosgub.myfinances.presentation.viewmodel.createincome

import kotlinx.coroutines.Job

interface CreateIncomeScreenIntents {
    fun setAmount(textFieldValue: String): Job

    fun showDateError(boolean: Boolean): Job

    fun showNoteError(boolean: Boolean): Job

    fun showError(boolean: Boolean): Job

    fun setNote(note: String): Job

    fun setDate(date: Long): Job

    fun create(): Job
}
