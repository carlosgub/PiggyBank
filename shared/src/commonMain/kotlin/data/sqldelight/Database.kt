package data.sqldelight

import com.carlosgub.myfinance.app.Database

class SharedDatabase(
    private val driverProvider: DatabaseDriverFactory,
) {
    private var database: Database? = null

    private suspend fun initDatabase() {
        if (database == null) {
            database = Database.invoke(
                driverProvider.createDriver()
            )
        }
    }

    suspend operator fun <R> invoke(block: suspend (Database) -> R): R {
        initDatabase()
        return block(database!!)
    }
}