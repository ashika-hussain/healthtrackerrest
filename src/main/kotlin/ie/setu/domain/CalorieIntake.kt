package ie.setu.domain

import org.joda.time.DateTime

data class CalorieIntake(
    var id: Int,
    var food: String,
    var calorie: Int,
    var mealType: String,
    var number: Int,
    var recordedon: DateTime,
    var userId: Int
)