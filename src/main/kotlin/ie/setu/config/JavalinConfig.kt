package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create{
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 : Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/login") {
                post(PasswordController :: login)
                path("users") {
                    post(PasswordController::saveUserDetails)
                }
            }
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}"){
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("biometric"){
                        get(BiometricController::getByUserId)
                        delete(BiometricController::deleteByUserId)
                    }
                    path("activities"){
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                    path("goals"){
                        post(GoalController::addGoal)
                        get(GoalController::getGoalsByUserId)
                        delete(GoalController::deleteGoalsByUserId)
                        patch(GoalController::updateGoalsByUserId)
                    }
                    path("calories"){
                        get(CalorieIntakeController::getByUserId)
                    }
                    path("levels"){
                        get(LevelController:: getLevelsByUserId)
                    }
                }
                path("/email/{email}"){
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivitiesByActivityId)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }

            path("/api/biometrics") {
                get(BiometricController:: getAll)
                post(BiometricController:: addBiometric)
                path("{biometrics-id}"){
                    get(BiometricController::getById)
                    delete(BiometricController::deleteById)
                }
            }

            path("/api/calorie") {
                post(CalorieIntakeController::addCalorieIntake)
                path("{calorie-id}") {
                    delete(CalorieIntakeController::deleteCalorieIntakesById)
                    patch(CalorieIntakeController::updateCalorieIntakesById)
                }
            }

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/users/{user-id}/biometrics", VueComponent("<user-biometrics-overview></user-biometrics-overview>"))
            get("/users/{user-id}/calories", VueComponent("<user-calorie-intake></user-calorie-intake>"))
            get("/users/{user-id}/goals", VueComponent("<user-goals></user-goals>"))
            get("/login", VueComponent("<user-login></user-login>"))
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }
}