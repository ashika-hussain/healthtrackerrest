package ie.setu.domain

import org.joda.time.DateTime

data class Level (var id: Int,
var level: Int,
var date: DateTime,
var userId: Int
)