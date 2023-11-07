package ie.setu.domain.repository

import ie.setu.domain.Biometric
import ie.setu.domain.db.Biometrics
import ie.setu.utils.mapTOBiometrics
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BiometricsDAO {

    // Save a new BMI entry to the database
    fun save(biometrics: Biometric): Int {
        val bmiCalculated = calculateBmi(biometrics.weight, biometrics.height)

        return transaction {
            Biometrics.insert {
                it[age] = biometrics.age
                it[weight] = biometrics.weight
                it[height] = biometrics.height
                it[bmi] = bmiCalculated
                it[userId] = biometrics.userId
                it[recordedon] = biometrics.recordedon
            }
        } get Biometrics.id
    }

    // Get all BMI entries for a specific user by user ID
    fun findByUserId(userId: Int): List<Biometric> {
        return transaction {
            Biometrics
                .select { Biometrics.userId eq userId }
                .map { mapTOBiometrics(it) }
        }
    }

    // Get a specific BMI entry by ID
    fun findById(id: Int): Biometric? {
        return transaction {
            Biometrics
                .select { Biometrics.id eq id }
                .map { mapTOBiometrics(it) }
                .firstOrNull()
        }
    }

    // Get all BMI entries
    fun getAll(): ArrayList<Biometric> {
        val biometricList: ArrayList<Biometric> = arrayListOf()
        transaction {
            Biometrics.selectAll().map {
                biometricList.add(mapTOBiometrics(it))
            }
        }
        return biometricList
    }

    fun deleteById(bmiId: Int): Int {
        return transaction {
            Biometrics.deleteWhere { Biometrics.id eq bmiId }
        }
    }

    fun deleteByUserId(userId: Int): Int {
        return transaction {
            Biometrics.deleteWhere { Biometrics.userId eq userId }
        }
    }

    private fun calculateBmi(weight: Double, height: Double): Double {
        if (weight > 0 || height > 0) {
            val heightInMeters = height / 100.0
            return "%.1f".format(weight / (heightInMeters * heightInMeters)).toDouble()
        }
        return 0.0
    }
}