package com.carlosgub.myfinances.navigation.di

import com.carlosgub.myfinances.data.database.DatabaseFinanceDataSource
import com.carlosgub.myfinances.data.database.impl.DatabaseFinanceDataSourceImpl
import com.carlosgub.myfinances.data.repository.FinanceRepositoryImpl
import com.carlosgub.myfinances.data.sqldelight.SharedDatabase
import com.carlosgub.myfinances.navigation.impl.AppNavigationImpl
import domain.repository.FinanceRepository
import domain.usecase.CreateExpenseUseCase
import domain.usecase.CreateIncomeUseCase
import domain.usecase.DeleteExpenseUseCase
import domain.usecase.DeleteIncomeUseCase
import domain.usecase.EditExpenseUseCase
import domain.usecase.EditIncomeUseCase
import domain.usecase.GetExpenseMonthDetailUseCase
import domain.usecase.GetExpenseUseCase
import domain.usecase.GetFinanceUseCase
import domain.usecase.GetIncomeMonthDetailUseCase
import domain.usecase.GetIncomeUseCase
import domain.usecase.GetMonthsUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import presentation.navigation.AppNavigation
import presentation.viewmodel.categorymonthdetail.CategoryMonthDetailViewModel
import presentation.viewmodel.createexpense.CreateExpenseViewModel
import presentation.viewmodel.createincome.CreateIncomeViewModel
import presentation.viewmodel.editexpense.EditExpenseViewModel
import presentation.viewmodel.editincome.EditIncomeViewModel
import presentation.viewmodel.home.HomeViewModel
import presentation.viewmodel.months.MonthsViewModel

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
            CategoryMonthDetailViewModel(
                getExpenseMonthDetailUseCase = get(),
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
