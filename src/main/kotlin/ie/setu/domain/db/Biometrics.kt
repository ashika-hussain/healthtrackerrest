package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Biometrics : Table("biometrics") {
    val id = integer("id").autoIncrement().primaryKey()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val weight = double("weight")
    val height = double("height")
    val bmi = double("bmi")
    val recordedon = datetime("recorded_on")
}