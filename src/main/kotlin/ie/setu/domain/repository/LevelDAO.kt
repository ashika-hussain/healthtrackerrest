package ie.setu.domain.repository

import ie.setu.domain.Level
import ie.setu.domain.db.Levels
import ie.setu.utils.mapToLevels
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select


class LevelDAO {
    fun findByUserId(userId: Int): List<Level>{
        return transaction {
            Levels
                .select { Levels.userId eq userId}
                .map { mapToLevels(it) }
        }
    }

    fun save(currentlevel: Level) {
        transaction {
            Levels.insert {
                it[level] = currentlevel.level
                it[date] = currentlevel.date
                it[userId] = currentlevel.userId
            } get Levels.id
        }
    }
}