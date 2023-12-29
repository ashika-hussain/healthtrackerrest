package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ActivityControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateActivities {
        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do delete on
            val addedUser: User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())

            val addActivityResponse = TestUtilities.addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            TestUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `add an activity when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            Assertions.assertEquals(404, TestUtilities.retrieveUserById(userId).status)

            val addActivityResponse = TestUtilities.addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, userId
            )
            Assertions.assertEquals(404, addActivityResponse.status)
        }
    }

    @Nested
    inner class ReadActivities {
        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = TestUtilities.retrieveAllActivities()
            if (response.status == 200){
                val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
                Assertions.assertNotEquals(0, retrievedActivities.size)
            }
            else{
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            TestUtilities.addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            TestUtilities.addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id
            )
            TestUtilities.addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id
            )

            //Assert and Act - retrieve the three added activities by user id
            val response = TestUtilities.retrieveActivitiesByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)
            val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
            Assertions.assertEquals(3, retrievedActivities.size)

            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = TestUtilities.retrieveActivitiesByUserId(addedUser.id)
            Assertions.assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = TestUtilities.retrieveActivitiesByUserId(userId)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get activity by activity id when no activity exists returns 404 response`() {
            //Arrange
            val activityId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = TestUtilities.retrieveActivityByActivityId(activityId)
            Assertions.assertEquals(404, response.status)
        }


        @Test
        fun `get activity by activity id when activity exists returns 200 response`() {
            //Arrange - add a user and associated activity
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addActivityResponse = TestUtilities.addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            //Act & Assert - retrieve the activity by activity id
            val response = TestUtilities.retrieveActivityByActivityId(addedActivity.id)
            Assertions.assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)
        }
    }

    @Nested
    inner class UpdateActivities {
        @Test
        fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1

            //Arrange - check there is no user for -1 id
            Assertions.assertEquals(404, TestUtilities.retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            Assertions.assertEquals(
                404, TestUtilities.updateActivity(
                    activityID, updatedDescription, updatedDuration,
                    updatedCalories, updatedStarted, userId
                ).status
            )
        }

        @Test
        fun `updating an activity by activity id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do an update on
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addActivityResponse = TestUtilities.addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            //Act & Assert - update the added activity and assert a 204 is returned
            val updatedActivityResponse = TestUtilities.updateActivity(
                addedActivity.id, updatedDescription,
                updatedDuration, updatedCalories, updatedStarted, addedUser.id
            )
            Assertions.assertEquals(204, updatedActivityResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedActivityResponse = TestUtilities.retrieveActivityByActivityId(addedActivity.id)
            val updatedActivity = jsonNodeToObject<Activity>(retrievedActivityResponse)
            Assertions.assertEquals(updatedDescription, updatedActivity.description)
            Assertions.assertEquals(updatedDuration, updatedActivity.duration, 0.1)
            Assertions.assertEquals(updatedCalories, updatedActivity.calories)
            Assertions.assertEquals(updatedStarted, updatedActivity.started)

            //After - delete the user
            TestUtilities.deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteActivities {
        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, TestUtilities.deleteActivityByActivityId(-1).status)
        }

        @Test
        fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, TestUtilities.deleteActivitiesByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addActivityResponse = TestUtilities.addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse.status)

            //Act & Assert - delete the added activity and assert a 204 is returned
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)
            Assertions.assertEquals(204, TestUtilities.deleteActivityByActivityId(addedActivity.id).status)

            //After - delete the user
            TestUtilities.deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all activities by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
            val addedUser : User = jsonToObject(TestUtilities.addUser(validName, validEmail, validdob).body.toString())
            val addActivityResponse1 = TestUtilities.addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse1.status)
            val addActivityResponse2 = TestUtilities.addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse2.status)
            val addActivityResponse3 = TestUtilities.addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id
            )
            Assertions.assertEquals(201, addActivityResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, TestUtilities.deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted activities
            val addedActivity1 = jsonNodeToObject<Activity>(addActivityResponse1)
            val addedActivity2 = jsonNodeToObject<Activity>(addActivityResponse2)
            val addedActivity3 = jsonNodeToObject<Activity>(addActivityResponse3)
            Assertions.assertEquals(404, TestUtilities.retrieveActivityByActivityId(addedActivity1.id).status)
            Assertions.assertEquals(404, TestUtilities.retrieveActivityByActivityId(addedActivity2.id).status)
            Assertions.assertEquals(404, TestUtilities.retrieveActivityByActivityId(addedActivity3.id).status)
        }
    }

}