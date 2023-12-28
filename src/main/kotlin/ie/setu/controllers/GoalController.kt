package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import ie.setu.utils.jsonToObject
import ie.setu.domain.Goal
import ie.setu.domain.repository.GoalDAO

object GoalController {

    private val userDao = UserDAO()
    private val goalDao = GoalDAO()


    fun getGoalsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val goals = goalDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (goals.isNotEmpty()) {
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(goals))
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


    fun addGoal(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val goal : Goal = jsonToObject(ctx.body())
        val goalId = goalDao.save(goal)
        if (goalId != null) {
            goal.id = goalId
            ctx.json(mapper.writeValueAsString(goal))
            ctx.status(201)
        }
    }

    fun updateGoalsByUserId(ctx: Context){
        val goal : Goal = jsonToObject(ctx.body())
        if (goalDao.updateById(
                id=ctx.pathParam("user-id").toInt(),
                goalDTO=goal) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteGoalsByUserId(ctx: Context){
        if (goalDao.deleteByUserId(ctx.pathParam("user-id").toInt()) !=0)
            ctx.status(204)
        else
            ctx.status(404)
    }




}