package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CalorieIntakes : Table("foodItems") {
    val id = integer("id").autoIncrement().primaryKey()
    val food = varchar("food", 100)
    val mealType = varchar("mealType", 100)
    val calorie = integer("calorie")
    val number = integer("number")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}