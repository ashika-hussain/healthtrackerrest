package ie.setu.domain.repository

import ie.setu.domain.Goal
import ie.setu.domain.db.Goals
import ie.setu.utils.mapToGoal
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class GoalDAO {

    fun findByUserId(userId: Int): List<Goal> {
        return transaction {
            Goals
                .select { Goals.userId eq userId}
                .map { mapToGoal(it) }
        }
    }

    fun save(goal: Goal) : Int?{
        return transaction {
            Goals.insert {
                it[targetWeight] = goal.targetWeight
                it[targetLevel] = goal.targetLevel
                it[date] = goal.date
                it[userId] = goal.userId
            } get Goals.id
        }
    }

    fun updateById(id: Int, goalDTO: Goal): Int {
        return transaction {
            Goals.update ({
                Goals.userId eq id}) {
                it[targetWeight] = goalDTO.targetWeight
                it[targetLevel] = goalDTO.targetLevel
                it[date] = goalDTO.date
                it[userId] = goalDTO.userId
            }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Goals.deleteWhere { Goals.userId eq userId }
        }
    }
}