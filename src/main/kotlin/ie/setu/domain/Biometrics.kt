package ie.setu.domain

import org.joda.time.DateTime

data class Biometric(
    var id: Int,
    var userId: Int,
    var weight: Double,
    var height: Double,
    val bmi: Double,
    var recordedon: DateTime
)