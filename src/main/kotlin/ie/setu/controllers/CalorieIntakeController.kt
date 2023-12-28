package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.CalorieIntake
import ie.setu.domain.repository.CalorieIntakeDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object CalorieIntakeController {

    private val userDao = UserDAO()
    private val calorieIntakeDAO = CalorieIntakeDAO()
    
    fun getByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val calorieIntake = calorieIntakeDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (calorieIntake.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(calorieIntake))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }

    }
    
    fun addCalorieIntake(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val calorieIntake : CalorieIntake = jsonToObject(ctx.body())
        val calorieIntakeId = calorieIntakeDAO.save(calorieIntake)
        if (calorieIntakeId != null) {
            calorieIntake.id = calorieIntakeId
            ctx.json(mapper.writeValueAsString(calorieIntake))
            ctx.status(201)
        }
    }
    
    fun deleteCalorieIntakesById(ctx: Context){
        if (calorieIntakeDAO.deleteById(ctx.pathParam("calorie-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateCalorieIntakesById(ctx: Context){
        val calorieIntake : CalorieIntake = jsonToObject(ctx.body())
        if (calorieIntakeDAO.updateById(ctx.pathParam("calorie-id").toInt(), calorieIntake) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}