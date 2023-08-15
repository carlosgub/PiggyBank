package di

import org.koin.dsl.module
import view.viewmodel.HomeScreenModel

val homeModule = module {
    factory { HomeScreenModel() }
}