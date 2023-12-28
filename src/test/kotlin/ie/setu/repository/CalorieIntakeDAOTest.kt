package ie.setu.repository

import ie.setu.domain.CalorieIntake
import ie.setu.domain.db.CalorieIntakes
import ie.setu.helpers.calorieIntakes
import ie.setu.helpers.populateCalorieIntakeTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val calorieIntake1 = calorieIntakes[0]
private val calorieIntake2 = calorieIntakes[1]
private val calorieIntake3 = calorieIntakes[2]
class CalorieIntakeDAOTest {
    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateFoodItems {

        @Test
        fun `multiple calorieIntakes added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val calorieIntakeDAO = populateCalorieIntakeTable()
                assertEquals(mutableListOf(calorieIntake1), calorieIntakeDAO.findByUserId(calorieIntake1.userId))
                assertEquals(mutableListOf(calorieIntake2), calorieIntakeDAO.findByUserId(calorieIntake2.userId))
                assertEquals(mutableListOf(calorieIntake3), calorieIntakeDAO.findByUserId(calorieIntake3.userId))
            }
        }
    }

    @Nested
    inner class ReadFoodItems {

        @Test
        fun `get calorieIntake by user id that has no calorieIntakes, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val calorieIntakeDAO = populateCalorieIntakeTable()
                assertEquals(0, calorieIntakeDAO.findByUserId(3789).size)
            }
        }

        @Test
        fun `get calorieIntake by user id that exists, results in a correct calorieIntakes returned`() {
            transaction {
                val userDAO = populateUserTable()
                val calorieIntakeDAO = populateCalorieIntakeTable()
                assertEquals(calorieIntake1, calorieIntakeDAO.findByUserId(1).get(0))
                assertEquals(calorieIntake2, calorieIntakeDAO.findByUserId(2).get(0))
                assertEquals(calorieIntake3, calorieIntakeDAO.findByUserId(3).get(0))
            }
        }

    }

    @Nested
    inner class UpdateFoodItems {

        @Test
        fun `updating existing calorieIntake in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()
                val calorieIntakeDAO = populateCalorieIntakeTable()

                val calorieIntake3updated = CalorieIntake(id = 3, food = "A", mealType = "lunch", calorie = 100, number = 100, userId = 3)
                calorieIntakeDAO.updateById(calorieIntake3updated.id, calorieIntake3updated)
                assertEquals(mutableListOf(calorieIntake3updated), calorieIntakeDAO.findByUserId(3))
            }
        }

    }

    @Nested
    inner class DeleteFoodItems {

        @Test
        fun `deleting calorieIntakes when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three calorieIntakes
                val userDAO = populateUserTable()
                val calorieIntakeDAO = populateCalorieIntakeTable()

                //Act & Assert
                assertEquals(1, calorieIntakeDAO.findByUserId(3).size)
                calorieIntakeDAO.deleteById(3)
                assertEquals(0, calorieIntakeDAO.findByUserId(3).size)
            }
        }
    }
}