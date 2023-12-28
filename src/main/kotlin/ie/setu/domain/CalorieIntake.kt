package ie.setu.domain

data class CalorieIntake(
    var id: Int,
    var food: String,
    var calorie: Int,
    var mealType: String,
    var number: Int,
    var userId: Int
)