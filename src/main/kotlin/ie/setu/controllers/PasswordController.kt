package ie.setu.controllers

import ie.setu.domain.Password
import ie.setu.domain.SaveUser
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import org.mindrot.jbcrypt.BCrypt
import ie.setu.domain.repository.PasswordsDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object PasswordController {

    private val passwordsDAO = PasswordsDAO()
    private val userDAO = UserDAO()
    class Credentials(
        var username: String?,
        var password: String?
    )

    fun login(ctx: Context) {
        val credentials : Credentials = jsonToObject(ctx.body())
        val loginData= passwordsDAO.authenticate(credentials.username,credentials.password)
        ctx.json(loginData)
        if(loginData.role != null)
            ctx.status(200)
        else {
            ctx.status(401)
        }
    }

    fun saveUserDetails(ctx: Context){
        val userdetails : SaveUser = jsonToObject(ctx.body())
        if(passwordsDAO.savePassword(userdetails) > 0) {
            ctx.json(userdetails)
            ctx.status(201)
        }
        else
            ctx.status(400)


    }

}