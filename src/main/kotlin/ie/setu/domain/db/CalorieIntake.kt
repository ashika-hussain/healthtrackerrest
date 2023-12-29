package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CalorieIntakes : Table("calorieIntakes") {
    val id = integer("id").autoIncrement().primaryKey()
    val food = varchar("food", 100)
    val mealType = varchar("mealtype", 100)
    val calorie = integer("calorie")
    val number = integer("number")
    val recordedon = datetime("recorded_on")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}