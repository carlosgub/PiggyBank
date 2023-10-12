package data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import com.carlosgub.myfinance.app.Database
import app.cash.sqldelight.driver.native.NativeSqliteDriver


actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "Database.db")
    }
}