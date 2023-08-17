package di

import data.firebase.FirebaseFinance
import data.repository.FinanceRepositoryImpl
import domain.repository.FinanceRepository
import domain.usecase.GetFinanceUseCase
import org.koin.dsl.module
import presentation.viewmodel.CreateExpenseViewModel
import presentation.viewmodel.HomeViewModel

val homeModule = module {
    single {
        HomeViewModel(
            getFinanceUseCase = get()
        )
    }

    single {
        CreateExpenseViewModel()
    }

    factory {
        GetFinanceUseCase(
            financeRepository = get()
        )
    }

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