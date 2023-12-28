package ie.setu.domain


data class User (
    var id: Int,
    var name:String,
    var email:String,
    var dob: String
    )

data class SaveUser(
    var name:String,
    var email:String,
    var dob: String,
    var password: String,
    var role:String
)