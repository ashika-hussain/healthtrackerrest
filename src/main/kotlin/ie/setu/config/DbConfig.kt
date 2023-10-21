package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database

class DbConfig{

    private val logger = KotlinLogging.logger {}
    fun getDbConnection() : Database {

        logger.info{"Starting DB Connection..."}

        val PGHOST = "trumpet.db.elephantsql.com"
        val PGPORT = "5432"
        val PGUSER = "jjpsxmns"
        val PGPASSWORD = "TUh5HYND7zGLqih0QUhY-RAix76vB0yR"
        val PGDATABASE = "jjpsxmns"

        //url format should be jdbc:postgresql://host:port/database
        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        val dbConfig = Database.connect(
            url = dbUrl,
            driver="org.postgresql.Driver",
            user = PGUSER,
            password = PGPASSWORD
        )

        return dbConfig
    }

}