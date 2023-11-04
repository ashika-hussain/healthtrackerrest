package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Passwords : Table("passwords"){
    val id = integer("id").autoIncrement().primaryKey()
    val userid = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val salt = varchar("salt", 255)
    var password = varchar("password", 255)
    var role = varchar("role",100)
}