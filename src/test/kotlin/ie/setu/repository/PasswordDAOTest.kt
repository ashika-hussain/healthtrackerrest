package ie.setu.repository

import ie.setu.controllers.PasswordController
import ie.setu.domain.SaveUser
import ie.setu.domain.db.Levels
import ie.setu.domain.db.Passwords
import ie.setu.domain.repository.PasswordsDAO
import ie.setu.helpers.activities
import ie.setu.helpers.populateActivityTable
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.userDetails
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val userdetail = userDetails[0]
class PasswordDAOTest {


    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class SavePassword {

        @Test
        fun `save password for a user`() {
            transaction {
                SchemaUtils.create(Passwords)
                SchemaUtils.create(Levels)
                val passwordsDAO = PasswordsDAO()
                val result = passwordsDAO.savePassword(userdetail)
                assertEquals(1, result)
            }
        }
    }

    @Nested
    inner class Authentication{

        @Test
        fun  `verify authentication with right credentials`(){
            transaction {
                SchemaUtils.create(Passwords)
                SchemaUtils.create(Levels)
                val passwordsDAO = PasswordsDAO()
                val result = passwordsDAO.savePassword(userdetail)
                assertEquals(1,result)
                val loginData = passwordsDAO.authenticate(userdetail.email, userdetail.password)
                assertEquals(userdetail.role,loginData.role)
            }
        }

        @Test
        fun  `verify authentication with wrong credentials`(){
            transaction {
                SchemaUtils.create(Passwords)
                SchemaUtils.create(Levels)
                val passwordsDAO = PasswordsDAO()
                val result = passwordsDAO.savePassword(userdetail)
                assertEquals(1,result)
                val loginData = passwordsDAO.authenticate(userdetail.email, "wrongpassword")
                assertEquals(null,loginData.role)
            }
        }
    }
}
