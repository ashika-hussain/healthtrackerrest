package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow


enum class Role(val key: String) {
    ADMIN("admin"),
    CANDIDATE("candidate")
}


fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    dob = it[Users.dob]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapTOPassword(it:ResultRow) = Password(
    id =it[Passwords.id],
    userid = it[Passwords.userid],
    salt = it[Passwords.salt],
    password = it[Passwords.password],
    role =  it[Passwords.role]
)

fun mapTOBiometrics(it:ResultRow) = Biometric(
    id = it[Biometrics.id],
    userId = it[Biometrics.userId],
    height = it[Biometrics.height],
    weight = it[Biometrics.weight],
    bmi = it[Biometrics.bmi],
    recordedon = it[Biometrics.recordedon]

)

fun mapTOCalorieIntake(it: ResultRow) = CalorieIntake(
    id = it[CalorieIntakes.id],
    food = it[CalorieIntakes.food],
    calorie = it[CalorieIntakes.calorie],
    mealType = it[CalorieIntakes.mealType],
    number = it[CalorieIntakes.number],
    recordedon = it[CalorieIntakes.recordedon],
    userId = it[CalorieIntakes.userId]
)

fun mapToGoal(it: ResultRow) = Goal(
    id = it[Goals.id],
    targetWeight = it[Goals.targetWeight],
    targetLevel = it[Goals.targetLevel],
    date = it[Goals.date],
    userId = it[Goals.userId]
)

fun mapToLevels(it: ResultRow) = Level(
    id = it[Levels.id],
    level = it[Levels.level],
    date = it[Levels.date],
    userId = it[Levels.userId]
)