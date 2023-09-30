package di

import data.firebase.FirebaseFinance
import data.repository.FinanceRepositoryImpl
import domain.repository.FinanceRepository
import domain.usecase.CreateExpenseUseCase
import domain.usecase.CreateIncomeUseCase
import domain.usecase.GetCategoryMonthDetailUseCase
import domain.usecase.GetFinanceUseCase
import domain.usecase.GetMonthsUseCase
import org.koin.dsl.module
import presentation.viewmodel.CategoryMonthDetailViewModel
import presentation.viewmodel.CreateExpenseViewModel
import presentation.viewmodel.CreateIncomeViewModel
import presentation.viewmodel.HomeViewModel
import presentation.viewmodel.MonthsScreenViewModel

val homeModule = module {
    /*region ViewModels*/
    factory {
        HomeViewModel(
            getFinanceUseCase = get()
        )
    }

    factory {
        CreateExpenseViewModel(
            createExpenseUseCase = get()
        )
    }

    factory {
        CreateIncomeViewModel(
            createIncomeUseCase = get()
        )
    }

    factory {
        CategoryMonthDetailViewModel(
            getCategoryMonthDetailUseCase = get()
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
        GetCategoryMonthDetailUseCase(
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
            firebaseFinance = get()
        )
    }

    single {
        FirebaseFinance(
            firebaseFirestore = get()
        )
    }
}
