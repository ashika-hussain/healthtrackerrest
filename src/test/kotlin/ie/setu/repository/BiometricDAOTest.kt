package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.Biometric
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Biometrics
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.BiometricsDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


//retrieving some test data from Fixtures
private val biometric1 = biometrics[0]
private val biometric2 = biometrics[1]
private val biometric3 = biometrics[2]
class BiometricDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateBiometrics {

        @Test
        fun `multiple biometrics added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()
                //Act & Assert
                assertEquals(3, biometricsDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class ReadBiometrics {

        @Test
        fun `read  all entries from biometrics`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()
                //Act & Assert
                assertEquals(3, biometricsDAO.getAll().size)
            }
        }


        @Test
        fun `get biometrics by user id that has no entry, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()
                //Act & Assert
                assertEquals(0, biometricsDAO.findByUserId(4).size)
            }
        }

        @Test
        fun `get biometrics by user id that exists, results in biometrics returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()
                //Act & Assert
                assertEquals(biometric1, biometricsDAO.findByUserId(1)[0])
                assertEquals(biometric2, biometricsDAO.findByUserId(2)[0])
                assertEquals(biometric3, biometricsDAO.findByUserId(3)[0])
            }
        }

        @Test
        fun `get all biometrics from an empty table`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Biometrics)
                val biometricsDAO = BiometricsDAO()

                //Act & Assert
                assertEquals(0, biometricsDAO.getAll().size)
            }
        }

        @Test
        fun `get  by id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()
                //Act & Assert
                kotlin.test.assertEquals(null, biometricsDAO.findById(4))
            }
        }

        @Test
        fun `get by  id that exists, right entry returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()                //Act & Assert
                kotlin.test.assertEquals(biometric1, biometricsDAO.findById(1))
                kotlin.test.assertEquals(biometric2, biometricsDAO.findById(2))
            }
        }

    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existant entry (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()

                //Act & Assert
                assertEquals(3, biometricsDAO.getAll().size)
                biometricsDAO.deleteById(4)
                assertEquals(3, biometricsDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()

                //Act & Assert
                kotlin.test.assertEquals(3, biometricsDAO.getAll().size)
                biometricsDAO.deleteById(biometric1.id)
                kotlin.test.assertEquals(2, biometricsDAO.getAll().size)
            }
        }



        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val biometricsDAO = populateBiometricsTable()

                //Act & Assert
                assertEquals(3, biometricsDAO.getAll().size)
                biometricsDAO.deleteByUserId(1)
                kotlin.test.assertEquals(2, biometricsDAO.getAll().size)
            }
        }
    }


}