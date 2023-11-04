package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.Biometric
import ie.setu.domain.Password
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Biometrics
import ie.setu.domain.db.Passwords
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow


enum class Role(val key: String) {
    ADMIN("admin"),
    CANDIDATE("candidate")
}


fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
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
    age = it[Biometrics.age],
    height = it[Biometrics.height],
    weight = it[Biometrics.weight],
    bmi = it[Biometrics.bmi],
    recordedon = it[Biometrics.recordedon]

)