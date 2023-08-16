package di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import org.koin.dsl.module

val dataModule = module {

    single {
        Firebase.firestore.apply {
            setSettings(
                persistenceEnabled = false
            )
        }
    }

    single {
        Firebase.initialize()
    }
}