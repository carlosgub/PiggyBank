package data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.carlosgub.myfinance.app.Database

class SharedDatabase(
    private val driverProvider: DatabaseDriverFactory,
) {
    private var database: Database? = null

    suspend fun initDatabase() {
        if (database == null) {
            database = driverProvider.createDriver(Database.Schema).createDatabase()
        }
    }

    suspend operator fun invoke(): Database {
        initDatabase()
        return database!!
    }

    private fun SqlDriver.createDatabase(): Database = Database(this)
}
