package data.sqldelight

import com.carlosgub.myfinance.app.Database

class SharedDatabase(
    private val driverProvider: DatabaseDriverFactory,
) {
    private var database: Database? = null

    private suspend fun initDatabase() {
        if (database == null) {
            database = Database(
                driverProvider.createDriver()
            )
        }
    }

    suspend operator fun invoke(): Database {
        initDatabase()
        return database!!
    }
}