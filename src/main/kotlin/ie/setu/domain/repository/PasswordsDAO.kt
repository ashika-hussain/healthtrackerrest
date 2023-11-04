package ie.setu.domain.repository

import ie.setu.controllers.PasswordController
import ie.setu.domain.Password
import ie.setu.domain.SaveUser
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Passwords
import ie.setu.utils.mapTOPassword
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class PasswordsDAO {

    private val userDao = UserDAO()
    class LoginData(
        var role: String?,
        var result : String?

    )

    fun savePassword(userDetails: SaveUser) : Int{
        val userId = userDao.save(userDetails)
        val generatedsalt = BCrypt.gensalt(12)
        val generatedpassword = BCrypt.hashpw(userDetails.password,generatedsalt)
        return transaction {
            Passwords.insert {
                it[userid] = userId
                it[salt] = generatedsalt
                it[password] = generatedpassword
                it[role] = userDetails.role
            }
        } get Passwords.id
    }

    // Authenticate the user by hashing the inputted password using the stored salt,
    // then comparing the generated hashed password to the stored hashed password
    fun authenticate(username: String?, password: String?): LoginData {
        val loginData = LoginData(role = null, result = "Incorrect Credentials")
        if (username == null || password == null) {
            return loginData
        }
        val user: User = userDao.findByEmail(username) ?: return loginData
        val securitydetail : Password = getPasswordbyUserId(user.id) ?: return loginData
        return if( BCrypt.checkpw(password , securitydetail.password)) {
            loginData.role = securitydetail.role
            loginData.result = " Login successfull"
            loginData
        } else
            loginData
    }

    private fun getPasswordbyUserId(userid: Int): Password?{
        return transaction {
            Passwords
                .select { Passwords.userid eq userid}
                .map{ mapTOPassword(it) }
                .firstOrNull()
        }
    }
}