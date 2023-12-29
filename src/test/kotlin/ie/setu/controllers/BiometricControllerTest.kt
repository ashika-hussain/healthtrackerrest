package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Biometric
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BiometricControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateBiometrics {
        @Test
        fun     `add a biometric when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated biometric that we plan to do delete on
            val addedUser: User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())

            val addBiometricResponse = TestUtilities.addBiometric(
                biometrics[0].weight, biometrics[0].height,
                biometrics[0].bmi, biometrics[0].recordedon, addedUser.id
            )
            Assertions.assertEquals(201, addBiometricResponse.status)

            //After - delete the user (Biometric will cascade delete in the database)
            TestUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add an biometric when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            Assertions.assertEquals(404, TestUtilities.retrieveUserById(userId).status)

            val addBiometricResponse = TestUtilities.addBiometric(
                biometrics[0].weight, biometrics[0].height,
                biometrics[0].bmi, biometrics[0].recordedon, userId
            )
            Assertions.assertEquals(404, addBiometricResponse.status)
        }
    }

    @Nested
    inner class ReadBiometrics {
        @Test
        fun `get all biometrics from the database returns 200 or 404 response`() {
            val response = TestUtilities.retrivesAllBiometrics()
            if (response.status == 200){
                val retrievedBiometrics = jsonNodeToObject<Array<Biometric>>(response)
                Assertions.assertNotEquals(0, retrievedBiometrics.size)
            }
            else{
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all biometrics by user id when user and biometrics exists returns 200 response`() {
            //Arrange - add a user and 3 associated biometrics that we plan to retrieve
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            TestUtilities.addBiometric(
                biometrics[0].weight, biometrics[0].height,
                biometrics[0].bmi, biometrics[0].recordedon, addedUser.id
            )
            TestUtilities.addBiometric(
                biometrics[1].weight, biometrics[1].height,
                biometrics[1].bmi, biometrics[1].recordedon, addedUser.id
            )
            TestUtilities.addBiometric(
                biometrics[2].weight, biometrics[2].height,
                biometrics[2].bmi, biometrics[2].recordedon, addedUser.id
            )

            //Assert and Act - retrieve the three added biometrics by user id
            val response = TestUtilities.retrieveBiometricsByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)
            val retrievedBiometrics = jsonNodeToObject<Array<Biometric>>(response)
            Assertions.assertEquals(3, retrievedBiometrics.size)

            //After - delete the added user and assert a 204 is returned (biometrics are cascade deleted)
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all biometrics by user id when no biometrics exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())

            //Assert and Act - retrieve the biometrics by user id
            val response = TestUtilities.retrieveActivitiesByUserId(addedUser.id)
            Assertions.assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all biometrics by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve biometrics by user id
            val response = TestUtilities.retrieveActivitiesByUserId(userId)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get biometric by biometric id when no biometric exists returns 404 response`() {
            //Arrange
            val biometricId = -1
            //Assert and Act - attempt to retrieve the biometric by biometric id
            val response = TestUtilities.retrieveBiometricByBiometricId(biometricId)
            Assertions.assertEquals(404, response.status)
        }


        @Test
        fun `get biometric by biometric id when biometric exists returns 200 response`() {
            //Arrange - add a user and associated biometric
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addBiometricResponse = TestUtilities.addBiometric(
                biometrics[0].weight,
                biometrics[0].height, biometrics[0].bmi,
                biometrics[0].recordedon, addedUser.id
            )
            Assertions.assertEquals(201, addBiometricResponse.status)
            val addedBiometric = jsonNodeToObject<Biometric>(addBiometricResponse)

            //Act & Assert - retrieve the biometric by biometric id
            val response = TestUtilities.retrieveBiometricByBiometricId(addedBiometric.id)
            Assertions.assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)
        }
    }


    @Nested
    inner class DeleteBiometrics {
        @Test
        fun `deleting an biometric by biometric id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, TestUtilities.deleteBiometricByBiometricId(-1).status)
        }

        @Test
        fun `deleting biometrics by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, TestUtilities.deleteActivitiesByUserId(-1).status)
        }

        @Test
        fun `deleting an biometric by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated biometric that we plan to do a delete on
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addBiometricResponse = TestUtilities.addBiometric(
                biometrics[0].weight, biometrics[0].height,
                biometrics[0].bmi, biometrics[0].recordedon, addedUser.id
            )
            Assertions.assertEquals(201, addBiometricResponse.status)

            //Act & Assert - delete the added biometric and assert a 204 is returned
            val addedBiometric = jsonNodeToObject<Biometric>(addBiometricResponse)
            Assertions.assertEquals(204, TestUtilities.deleteBiometricByBiometricId(addedBiometric.id).status)

            //After - delete the user
            TestUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all biometrics by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated biometrics that we plan to do a cascade delete
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addBiometricResponse1 = TestUtilities.addBiometric(
                biometrics[0].weight, biometrics[0].height,
                biometrics[0].bmi, biometrics[0].recordedon, addedUser.id
            )
            Assertions.assertEquals(201, addBiometricResponse1.status)
            val addBiometricResponse2 = TestUtilities.addBiometric(
                biometrics[1].weight, biometrics[1].height,
                biometrics[1].bmi, biometrics[1].recordedon, addedUser.id
            )
            Assertions.assertEquals(201, addBiometricResponse2.status)
            val addBiometricResponse3 = TestUtilities.addBiometric(
                biometrics[2].weight, biometrics[2].height,
                biometrics[2].bmi, biometrics[2].recordedon, addedUser.id
            )
            Assertions.assertEquals(201, addBiometricResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted biometrics
            val addedBiometric1 = jsonNodeToObject<Biometric>(addBiometricResponse1)
            val addedBiometric2 = jsonNodeToObject<Biometric>(addBiometricResponse2)
            val addedBiometric3 = jsonNodeToObject<Biometric>(addBiometricResponse3)
            Assertions.assertEquals(404, TestUtilities.retrieveBiometricByBiometricId(addedBiometric1.id).status)
            Assertions.assertEquals(404, TestUtilities.retrieveBiometricByBiometricId(addedBiometric2.id).status)
            Assertions.assertEquals(404, TestUtilities.retrieveBiometricByBiometricId(addedBiometric3.id).status)
        }
    }
}