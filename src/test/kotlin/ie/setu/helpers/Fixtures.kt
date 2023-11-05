package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Biometric
import ie.setu.domain.SaveUser
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Biometrics
import ie.setu.domain.db.Passwords
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.BiometricsDAO
import ie.setu.domain.repository.PasswordsDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"

//Arrange - creating some text fixture data
const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"


val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")

val users = arrayListOf(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

val biometrics = arrayListOf(
    Biometric(id = 1, userId = 1, age = 32, weight = 45.0, height = 167.0, bmi = 16.1, recordedon = DateTime.now()),
    Biometric(id = 2, userId = 2, age = 22, weight = 55.0, height = 157.0, bmi = 22.3, recordedon = DateTime.now()),
    Biometric(id = 3, userId = 3, age = 15, weight = 28.0, height = 147.0, bmi = 13.0 , recordedon = DateTime.now()),

)
val userDetails =  arrayListOf(
    SaveUser(name = "Alice Wonderland", email = "alice@wonderland.com",  password = "alice123", role = "admin"),
    SaveUser(name = "Bob Cat", email = "bob@cat.ie", password = "bob123", role = "candidate"),
    SaveUser(name = "Mary Contrary", email = "mary@contrary.com", password = "mary123", role = "candidate"),
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateBiometricsTable(): BiometricsDAO{
    SchemaUtils.create(Biometrics)
    val biometricsDAO = BiometricsDAO()
    biometricsDAO.save(biometrics[0])
    biometricsDAO.save(biometrics[1])
    biometricsDAO.save(biometrics[2])
    return biometricsDAO
}
