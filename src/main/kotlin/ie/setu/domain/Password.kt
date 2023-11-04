package ie.setu.domain

data class Password (
    var id: Int,
    var userid : Int,
    var salt: String,
    var password: String,
    var role:String
)