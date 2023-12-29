package ie.setu.helpers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime

object TestUtilities {

    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()
    //helper function to add a test user to the database
    fun addUser (name: String, email: String, dob: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\", \"dob\":\"$dob\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id").asString()
    }

    //helper function to retrieve a test user from the database by email
    fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    //helper function to retrieve a test user from the database by id
    fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    //helper function to add a test user to the database
    fun updateUser (id: Int, name: String, email: String, dob: String): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\", \"dob\":\"$dob\"}")
            .asJson()
    }

    //helper function to retrieve all activities
    fun retrieveAllActivities(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/activities").asJson()
    }

    //helper function to retrieve activities by user id
    fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/activities").asJson()
    }

    //helper function to retrieve activity by activity id
    fun retrieveActivityByActivityId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities/${id}").asJson()
    }

    //helper function to delete an activity by activity id
    fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/activities/$id").asString()
    }

    //helper function to delete an activity by activity id
    fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id/activities").asString()
    }

    //helper function to add a test user to the database
    fun updateActivity(id: Int, description: String, duration: Double, calories: Int,
                               started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/activities/$id")
            .body("""
                {
                  "description":"$description",
                  "duration":$duration,
                  "calories":$calories,
                  "started":"$started",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    //helper function to add an activity
    fun addActivity(description: String, duration: Double, calories: Int,
                            started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/activities")
            .body("""
                {
                   "description":"$description",
                   "duration":$duration,
                   "calories":$calories,
                   "started":"$started",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    fun addBiometric(height: Double, weight: Double, bmi: Double,recordedon: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/biometrics")
            .body("""
                {
                   "weight":"$weight",
                   "height":$height,
                   "bmi":$bmi,
                   "recordedon":"$recordedon",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }
    fun retrieveBiometricByBiometricId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/biometrics/${id}").asJson()
    }

    fun retrieveBiometricsByUserId(id: Int): HttpResponse<JsonNode>{
        return Unirest.get(origin + "/api/users/${id}/biometric").asJson()
    }

    //helper function to delete an activity by activity id
    fun deleteBiometricByBiometricId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/biometrics/$id").asString()
    }

    //helper function to delete an activity by activity id
    fun deleteBiometricsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id/biometrics").asString()
    }

    fun updateBiometric(id:Int, height: Double, weight: Double, bmi: Double,recordedon: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.put("$origin/api/biometrics")
            .body("""
                {
                   "weight":"$weight",
                   "height":$height,
                   "bmi":$bmi,
                   "recordedon":"$recordedon",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    fun retrivesAllBiometrics(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/biometrics").asJson()
    }


    fun addGoal( targetWeight: Int, targetLevel: Int,
                   date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users/${userId}/goals")
            .body("""
            {
               
               "targetWeight":$targetWeight,
               "targetLevel":$targetLevel,
               "date":"$date",
               "userId":$userId
            }
        """.trimIndent())
            .asJson()
    }
    fun retrieveGoalsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/users/${id}/goals").asJson()
    }
    fun deleteGoalsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.delete("$origin/api/users/${id}/goals").asJson()
    }
    fun updateGoal(id: Int, targetWeight: Int, targetLevel: Int,
                   date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users/${userId}/goals/$id")
            .body("""
            {
               "id":$id
               "targetWeight":$targetWeight,
               "targetLevel":$targetLevel,
               "date":"$date",
               "userId":$userId
            }
        """.trimIndent())
            .asJson()
    }


}