package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    @Nested
    inner class ReadUsers {
        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get("$origin/api/users/").asString()
            if (response.status == 200) {
                val retrievedUsers: ArrayList<User> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedUsers.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = TestUtilities.retrieveUserById(id)

            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = TestUtilities.retrieveUserByEmail(nonExistingEmail)
            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            //Arrange - add the user
            TestUtilities.addUser(validName, validEmail, validdob)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = TestUtilities.retrieveUserByEmail(validEmail)
            Assertions.assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser: User = jsonToObject(retrieveResponse.body.toString())
            TestUtilities.deleteUser(retrievedUser.id)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = TestUtilities.addUser(validName, validEmail, validdob)
            val addedUser: User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = TestUtilities.retrieveUserById(addedUser.id)
            Assertions.assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            TestUtilities.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class CreateUsers{
        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = TestUtilities.addUser(validName, validEmail, validdob)
            Assertions.assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= TestUtilities.retrieveUserByEmail(validEmail)
            Assertions.assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser : User = jsonToObject(addResponse.body.toString())
            Assertions.assertEquals(validEmail, retrievedUser.email)
            Assertions.assertEquals(validName, retrievedUser.name)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = TestUtilities.deleteUser(retrievedUser.id)
            Assertions.assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateUser{
        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do an update on
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"
            val addedResponse = TestUtilities.addUser(validName, validEmail, validdob)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            Assertions.assertEquals(204, TestUtilities.updateUser(addedUser.id, updatedName, updatedEmail,updateddob).status)

            //Act & Assert - retrieve updated user and assert details are correct
            val updatedUserResponse = TestUtilities.retrieveUserById(addedUser.id)
            val updatedUser : User = jsonToObject(updatedUserResponse.body.toString())
            Assertions.assertEquals(updatedName, updatedUser.name)
            Assertions.assertEquals(updatedEmail, updatedUser.email)

            //After - restore the db to previous state by deleting the added user
            TestUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            Assertions.assertEquals(404, TestUtilities.updateUser(-1, updatedName, updatedEmail,updateddob).status)
        }

    }

    @Nested
    inner class Deleteuser{
        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, TestUtilities.deleteUser(-1).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do delete on
            val addedResponse = TestUtilities.addUser(validName, validEmail, validdob)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            Assertions.assertEquals(404, TestUtilities.retrieveUserById(addedUser.id).status)
        }
    }
}