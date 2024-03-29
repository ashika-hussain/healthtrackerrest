package ie.setu.domain.repository

import ie.setu.domain.SaveUser
import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Manages the database transactions and returns the result of transaction
 */
class UserDAO {

    fun getAllUsers(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it)) }
        }
        return userList
    }

    fun findById(id: Int): User?{
        return transaction {
            Users.select() {
                Users.id eq id}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    fun save(user: User) : Int{
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[dob] = user.dob
            }
        } get Users.id
    }

    fun findByEmail(email: String): User?{
        return transaction {
            Users.select() {
                Users.email eq email}
                .map{mapToUser(it)}
                .firstOrNull()
        }
    }

    fun delete(id: Int) : Int{
        return transaction{ Users.deleteWhere{
            Users.id eq id
        }
        }
    }

    fun update(id: Int, user: User) : Int{
        return transaction {
            Users.update ({
                Users.id eq id}) {
                it[name] = user.name
                it[email] = user.email
                it[dob] = user.dob
            }
        }
    }

    /**
     * Add a [user] to the User Table
     * @return the id of the user following the add.
     */
    fun save(user: SaveUser) : Int{
        return transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[dob] = user.dob
            }
        } get Users.id
    }
}
