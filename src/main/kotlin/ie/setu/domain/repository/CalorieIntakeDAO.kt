package ie.setu.domain.repository

import ie.setu.domain.CalorieIntake
import ie.setu.domain.db.CalorieIntakes
import ie.setu.utils.mapTOCalorieIntake
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CalorieIntakeDAO {

    fun findByUserId(userId: Int): List<CalorieIntake>{
        return transaction {
            CalorieIntakes.select { CalorieIntakes.userId eq userId}
                .map { mapTOCalorieIntake(it) }
        }
    }

    fun save(calorieintake: CalorieIntake) : Int?{
        return transaction {
            CalorieIntakes.insert {
                it[food] = calorieintake.food
                it[calorie] = calorieintake.calorie
                it[mealType] = calorieintake.mealType
                it[number] = calorieintake.number
                it[userId] = calorieintake.userId
            } get CalorieIntakes.id
        }
    }
    fun updateById(foodId: Int, intakeDTO: CalorieIntake): Int {
        return transaction {
            CalorieIntakes.update ({
                CalorieIntakes.id eq foodId}) {
                it[food] = intakeDTO.food
                it[calorie] = intakeDTO.calorie
                it[mealType] = intakeDTO.mealType
                it[number] = intakeDTO.number
                it[userId] = intakeDTO.userId
            }
        }
    }

    fun deleteById (id: Int): Int{
        return transaction{
            CalorieIntakes.deleteWhere { CalorieIntakes.id eq id }
        }
    }
}