package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Biometrics : Table("biometrics") {
    val id = integer("id").autoIncrement().primaryKey()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val age = integer("age")
    val weight = double("weight")
    val height = double("height")
    val bmi = varchar("bmi", 100)
    val recordedon = datetime("recorded_on")
}