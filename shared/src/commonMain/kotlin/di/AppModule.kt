package di

import org.koin.dsl.module
import presentation.viewmodel.HomeViewModel

val homeModule = module {
    single { HomeViewModel(get()) }
}