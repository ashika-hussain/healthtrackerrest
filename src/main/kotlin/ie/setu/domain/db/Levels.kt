package ie.setu.domain.db

import ie.setu.domain.db.Activities.autoIncrement
import ie.setu.domain.db.Activities.datetime
import ie.setu.domain.db.Activities.integer
import ie.setu.domain.db.Activities.primaryKey
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Levels : Table("levels") {
    val id = integer("id").autoIncrement().primaryKey()
    val level = integer("level")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}