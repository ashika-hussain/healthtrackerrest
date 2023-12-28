package ie.setu.repository

import ie.setu.helpers.levels
import ie.setu.helpers.levels
import ie.setu.helpers.populateLevelTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val level1 = levels[0]
private val level2 = levels[1]
private val level3 = levels[2]
class LevelDAOTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateBadges {

        @Test
        fun `multiple levels added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val levelDAO = populateLevelTable()
                assertEquals(mutableListOf(level1), levelDAO.findByUserId(level1.userId))
                assertEquals(mutableListOf(level2), levelDAO.findByUserId(level2.userId))
                assertEquals(mutableListOf(level3), levelDAO.findByUserId(level3.userId))
            }
        }
    }

    @Nested
    inner class ReadBadges {

        @Test
        fun `get levels by user id that has no levels, results in no record returned`() {
            transaction {
                val userDAO = populateUserTable()
                val levelDAO = populateLevelTable()
                assertEquals(0, levelDAO.findByUserId(8999).size)
            }
        }

        @Test
        fun `get levels by user id that exists, results in correct levels returned`() {
            transaction {
                val userDAO = populateUserTable()
                val levelDAO = populateLevelTable()
                assertEquals(level1, levelDAO.findByUserId(1).get(0))
                assertEquals(level2, levelDAO.findByUserId(3).get(0))
                assertEquals(level3, levelDAO.findByUserId(2).get(0))
            }
        }
    }
}