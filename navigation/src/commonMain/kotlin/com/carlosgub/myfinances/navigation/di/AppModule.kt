package com.carlosgub.myfinances.navigation.di

import com.carlosgub.myfinances.data.database.DatabaseFinanceDataSource
import com.carlosgub.myfinances.data.database.impl.DatabaseFinanceDataSourceImpl
import com.carlosgub.myfinances.data.repository.FinanceRepositoryImpl
import com.carlosgub.myfinances.data.sqldelight.SharedDatabase
import com.carlosgub.myfinances.domain.repository.FinanceRepository
import com.carlosgub.myfinances.domain.usecase.CreateExpenseUseCase
import com.carlosgub.myfinances.domain.usecase.CreateIncomeUseCase
import com.carlosgub.myfinances.domain.usecase.DeleteExpenseUseCase
import com.carlosgub.myfinances.domain.usecase.DeleteIncomeUseCase
import com.carlosgub.myfinances.domain.usecase.EditExpenseUseCase
import com.carlosgub.myfinances.domain.usecase.EditIncomeUseCase
import com.carlosgub.myfinances.domain.usecase.GetExpenseMonthDetailUseCase
import com.carlosgub.myfinances.domain.usecase.GetExpenseUseCase
import com.carlosgub.myfinances.domain.usecase.GetFinanceUseCase
import com.carlosgub.myfinances.domain.usecase.GetIncomeMonthDetailUseCase
import com.carlosgub.myfinances.domain.usecase.GetIncomeUseCase
import com.carlosgub.myfinances.domain.usecase.GetMonthsUseCase
import com.carlosgub.myfinances.navigation.impl.AppNavigationImpl
import com.carlosgub.myfinances.presentation.navigation.AppNavigation
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailexpense.CategoryMonthDetailExpenseViewModel
import com.carlosgub.myfinances.presentation.viewmodel.categorymonthdetailincome.CategoryMonthDetailIncomeViewModel
import com.carlosgub.myfinances.presentation.viewmodel.createexpense.CreateExpenseViewModel
import com.carlosgub.myfinances.presentation.viewmodel.createincome.CreateIncomeViewModel
import com.carlosgub.myfinances.presentation.viewmodel.editexpense.EditExpenseViewModel
import com.carlosgub.myfinances.presentation.viewmodel.editincome.EditIncomeViewModel
import com.carlosgub.myfinances.presentation.viewmodel.home.HomeViewModel
import com.carlosgub.myfinances.presentation.viewmodel.months.MonthsViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val homeModule =
    module {
        // region Navigation
        single<AppNavigation> {
            AppNavigationImpl()
        }
        // endregion*

        // region ViewModels
        factory {
            HomeViewModel(
                getFinanceUseCase = get(),
            )
        }

        factory {
            CreateExpenseViewModel(
                createExpenseUseCase = get(),
            )
        }

        factory {
            CreateIncomeViewModel(
                createIncomeUseCase = get(),
            )
        }

        factory {
            EditExpenseViewModel(
                editExpenseUseCase = get(),
                deleteExpenseUseCase = get(),
                getExpenseUseCase = get(),
            )
        }

        factory {
            EditIncomeViewModel(
                editIncomeUseCase = get(),
                deleteUseCase = get(),
                getIncomeUseCase = get(),
            )
        }

        factory {
            CategoryMonthDetailExpenseViewModel(
                getExpenseMonthDetailUseCase = get(),
            )
        }

        factory {
            CategoryMonthDetailIncomeViewModel(
                getIncomeMonthDetailUseCase = get(),
            )
        }

        factory {
            MonthsViewModel(
                getMonthsUseCase = get(),
            )
        }
        // endregion

        // region Use Cases
        factory {
            GetFinanceUseCase(
                financeRepository = get(),
            )
        }

        factory {
            GetIncomeUseCase(
                financeRepository = get(),
            )
        }

        factory {
            GetExpenseUseCase(
                financeRepository = get(),
            )
        }

        factory {
            CreateExpenseUseCase(
                financeRepository = get(),
            )
        }

        factory {
            CreateIncomeUseCase(
                financeRepository = get(),
            )
        }

        factory {
            EditIncomeUseCase(
                financeRepository = get(),
            )
        }

        factory {
            EditExpenseUseCase(
                financeRepository = get(),
            )
        }

        factory {
            DeleteIncomeUseCase(
                financeRepository = get(),
            )
        }

        factory {
            DeleteExpenseUseCase(
                financeRepository = get(),
            )
        }

        factory {
            GetExpenseMonthDetailUseCase(
                financeRepository = get(),
            )
        }

        factory {
            GetIncomeMonthDetailUseCase(
                financeRepository = get(),
            )
        }

        factory {
            GetMonthsUseCase(
                financeRepository = get(),
            )
        }
        // endregion

        // region Repository
        factory<FinanceRepository> {
            FinanceRepositoryImpl(
                databaseFinance = get(),
            )
        }

        single<DatabaseFinanceDataSource> {
            DatabaseFinanceDataSourceImpl(
                sharedDatabase = get(),
            )
        }
        // endregion
    }

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            homeModule,
            sqlDelightModule,
            platformModule(),
        )
    }

val sqlDelightModule =
    module {
        single { SharedDatabase(get()) }
    }

fun initKoin() = initKoin {}
