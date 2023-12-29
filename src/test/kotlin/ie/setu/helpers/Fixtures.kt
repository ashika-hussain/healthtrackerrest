package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

const val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"
const val validdob = "15/08/1995"

//Arrange - creating some text fixture data
const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"
const val updateddob = "Updated Dob"


val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")

val updatedtargetweight = 22
val updatedtargetlevel = 7
val updateddate = DateTime.parse("2020-06-11T05:59:27.258Z")




val users = arrayListOf(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1,dob = "12/12/1996"),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2, dob= "01/02/1994"),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3, dob = "02/04/1992"),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4, dob ="05/07/1991")
)

val activities = arrayListOf(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

val biometrics = arrayListOf(
    Biometric(id = 1, userId = 1,  weight = 45.0, height = 167.0, bmi = 16.1, recordedon = DateTime.now()),
    Biometric(id = 2, userId = 2, weight = 55.0, height = 157.0, bmi = 22.3, recordedon = DateTime.now()),
    Biometric(id = 3, userId = 3, weight = 28.0, height = 147.0, bmi = 13.0 , recordedon = DateTime.now()),

)
val userDetails =  arrayListOf(
    SaveUser(name = "Alice Wonderland", email = "alice@wonderland.com",  password = "alice123", role = "admin", dob = "12/12/1996"),
    SaveUser(name = "Bob Cat", email = "bob@cat.ie", password = "bob123", role = "candidate", dob = "12/12/1996"),
    SaveUser(name = "Mary Contrary", email = "mary@contrary.com", password = "mary123", role = "candidate", dob = "12/12/1996"),
)
val calorieIntakes = arrayListOf<CalorieIntake>(
    CalorieIntake(id = 1, food = "A",  calorie = 100, mealType = "Breakfast", number = 100, userId = 1, recordedon = DateTime.now()),
    CalorieIntake(id = 2, food = "B",  calorie = 100, mealType = "Breakfast",number = 100, userId = 2, recordedon = DateTime.now()),
    CalorieIntake(id = 3, food = "C", calorie = 100, mealType = "Breakfast" ,number = 100, userId = 3, recordedon = DateTime.now())
)

val goals = arrayListOf<Goal>(
    Goal(id = 1, targetWeight = 30, targetLevel = 4, date = DateTime.now(), userId = 1),
    Goal(id = 2, targetWeight = 70, targetLevel = 2, date = DateTime.now(), userId = 2),
    Goal(id = 3, targetWeight = 40, targetLevel = 1,  date = DateTime.now(), userId = 3)
)

val levels = arrayListOf<Level>(
    Level(id = 1,  level = 2, date = DateTime.now(), userId = 3),
    Level(id = 2,  level = 1, date = DateTime.now(), userId = 1),
    Level(id = 3, level = 3, date = DateTime.now(), userId = 2)
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

fun populateCalorieIntakeTable(): CalorieIntakeDAO {
    SchemaUtils.create(CalorieIntakes)
    val calorieIntakeDAO = CalorieIntakeDAO()
    calorieIntakeDAO.save(calorieIntakes[0])
    calorieIntakeDAO.save(calorieIntakes[1])
    calorieIntakeDAO.save(calorieIntakes[2])
    return calorieIntakeDAO
}
fun populateGoalTable(): GoalDAO {
    SchemaUtils.create(Goals)
    val goalDAO = GoalDAO()
    goalDAO.save(goals[0])
    goalDAO.save(goals[1])
    goalDAO.save(goals[2])
    return goalDAO
}

fun populateLevelTable(): LevelDAO {
    SchemaUtils.create(Levels)
    val levelDAO = LevelDAO()
    levelDAO.save(levels[0])
    levelDAO.save(levels[1])
    levelDAO.save(levels[2])
    return levelDAO
}
