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
        val user = userDao.findById(biometric.userId)
        if (user != null) {
            val bmiId = biometricsDAO.save(biometric)
            biometric.id = bmiId
            ctx.json(biometric)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun getAll(ctx: Context) {
        val biometrics = biometricsDAO.getAll()
        if (biometrics.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(biometrics)
    }

    fun getById(ctx: Context) {
        val biometrics = biometricsDAO.findById((ctx.pathParam("biometrics-id").toInt()))
        if (biometrics != null) {
            ctx.json(biometrics)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
    fun deleteById(ctx: Context){
        if (biometricsDAO.deleteById(ctx.pathParam("biometrics-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
    fun getByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val biometrics = biometricsDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (biometrics.isNotEmpty()) {
                ctx.json(biometrics)
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