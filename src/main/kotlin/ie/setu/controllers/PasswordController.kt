package ie.setu.controllers

import ie.setu.domain.SaveUser
import ie.setu.domain.repository.PasswordsDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object PasswordController {

    private val passwordsDAO = PasswordsDAO()

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
        val userid = passwordsDAO.savePassword(userdetails)
        if(userid > 0) {
            ctx.json(userdetails)
            ctx.status(201)
        }
        else
            ctx.status(400)


    }

}