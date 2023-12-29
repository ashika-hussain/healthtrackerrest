package ie.setu.domain.repository

import ie.setu.domain.Level
import ie.setu.domain.db.Goals
import ie.setu.domain.db.Levels
import ie.setu.utils.mapToLevels
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime


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

    fun saveByUserId(user: Int) {
        transaction {
            Levels.insert {
                it[level] = 1
                it[date] = DateTime.now()
                it[userId] = user
            } get Levels.id
        }
    }

    fun updateByUserId(l: Level) : Int{
        return transaction {
            Levels.update ({
                Levels.id eq l.id}) {
                it[level] = l.level
                it[date] = l.date
                it[userId] = l.userId
            }
        }
    }
}