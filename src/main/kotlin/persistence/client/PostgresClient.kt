package persistence.client

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database


class PostgresClient {

    companion object {
        var connected = false
    }

    init {
        if (!connected) {
            val config = HikariConfig("src/main/resources/hikari.properties")
            config.jdbcUrl="jdbc:postgresql://postgres-ktor:5432/database"
            val ds = HikariDataSource(config)
            Database.connect(ds)
            connected = true
        }
    }
}