package com.carlosgub.myfinances.presentation.viewmodel.months

import kotlinx.coroutines.Job

interface MonthsScreenIntents {
    fun getMonths(): Job

    fun navigateToMonthDetail(monthKey: String): Job
}
