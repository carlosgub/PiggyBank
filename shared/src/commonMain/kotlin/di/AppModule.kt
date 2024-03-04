package di

import data.repository.FinanceRepositoryImpl
import data.source.database.DatabaseFinance
import data.sqldelight.SharedDatabase
import domain.repository.FinanceRepository
import domain.usecase.CreateExpenseUseCase
import domain.usecase.CreateIncomeUseCase
import domain.usecase.DeleteUseCase
import domain.usecase.EditExpenseUseCase
import domain.usecase.EditIncomeUseCase
import domain.usecase.GetExpenseMonthDetailUseCase
import domain.usecase.GetFinanceUseCase
import domain.usecase.GetIncomeMonthDetailUseCase
import domain.usecase.GetMonthsUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import presentation.viewmodel.monthDetail.CategoryMonthDetailViewModel
import presentation.viewmodel.create.CreateViewModel
import presentation.viewmodel.edit.EditViewModel
import presentation.viewmodel.home.HomeViewModel
import presentation.viewmodel.months.MonthsScreenViewModel

val homeModule = module {
    /*region ViewModels*/
    factory {
        HomeViewModel(
            getFinanceUseCase = get()
        )
    }

    factory {
        CreateViewModel(
            createExpenseUseCase = get(),
            createIncomeUseCase = get()
        )
    }

    factory {
        EditViewModel(
            editExpenseUseCase = get(),
            editIncomeUseCase = get(),
            deleteUseCase = get()
        )
    }

    factory {
        CategoryMonthDetailViewModel(
            getCategoryMonthDetailUseCase = get(),
            getIncomeMonthDetailUseCase = get()
        )
    }

    factory {
        MonthsScreenViewModel(
            getMonthsUseCase = get()
        )
    }
    /*endregion*/

    /*region Use Cases*/
    factory {
        GetFinanceUseCase(
            financeRepository = get()
        )
    }

    factory {
        CreateExpenseUseCase(
            financeRepository = get()
        )
    }

    factory {
        CreateIncomeUseCase(
            financeRepository = get()
        )
    }

    factory {
        EditIncomeUseCase(
            financeRepository = get()
        )
    }

    factory {
        EditExpenseUseCase(
            financeRepository = get()
        )
    }

    factory {
        DeleteUseCase(
            financeRepository = get()
        )
    }

    factory {
        GetExpenseMonthDetailUseCase(
            financeRepository = get()
        )
    }

    factory {
        GetIncomeMonthDetailUseCase(
            financeRepository = get()
        )
    }

    factory {
        GetMonthsUseCase(
            financeRepository = get()
        )
    }
    /*endregion*/

    factory<FinanceRepository> {
        FinanceRepositoryImpl(
            databaseFinance = get()
        )
    }

    single {
        DatabaseFinance(
            sharedDatabase = get()
        )
    }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            homeModule,
            sqlDelightModule,
            platformModule()
        )
    }

val sqlDelightModule = module {
    single { SharedDatabase(get()) }
}

fun initKoin() = initKoin {}
