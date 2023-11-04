package ie.setu.controllers

import ie.setu.domain.Biometric
import ie.setu.domain.repository.BiometricsDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object BiometricController {
    private val userDao = UserDAO()
    private var biometricsDAO = BiometricsDAO()
    fun addBiometric(ctx: Context) {
        val biometric: Biometric = jsonToObject(ctx.body())
        val userId = userDao.findById(biometric.userId)
        if (userId != null) {
            val bmiId = biometricsDAO.save(biometric)
            biometric.id = bmiId
            ctx.json(biometric)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun getAll(ctx: Context) {
        val bimes = biometricsDAO.getAll()
        if (bimes.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(bimes)
    }

    fun getById(ctx: Context) {
        val bimes = biometricsDAO.findById((ctx.pathParam("bmi-id").toInt()))
        if (bimes != null) {
            ctx.json(bimes)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
    fun deleteById(ctx: Context){
        if (biometricsDAO.deleteById(ctx.pathParam("bmi-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    fun getByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val bmies = biometricsDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (bmies.isNotEmpty()) {
                ctx.json(bmies)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }
    fun deleteByUserId(ctx: Context){
        if (biometricsDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}