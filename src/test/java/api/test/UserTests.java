package api.test;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setupData() {
		
		faker = new Faker();
		userPayload = new User();
		userPayload.setId(faker.number().randomDigit());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().phoneNumber());
		
		//logs
		logger = LogManager.getLogger(this.getClass());
		
		
	}
	
	@Test(priority = 1)
	public void testPostUser() {
		logger.info("***Creating user***");
		Response res = UserEndPoints.createUser(userPayload);
		res.then().log().all();
		
		Assert.assertEquals(res.getStatusCode(), 200);
		
		logger.info("***User info created***");
	}
	

	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("***Reading user info***");
		Response res = UserEndPoints.readUser(this.userPayload.getUsername());

		res.then().log().all();
		
		assertEquals(res.getStatusCode(), 200);
		
	}
	
	@Test(priority = 3)
	public void testUpdateUserByName() {
		logger.info("***Updating user***");
		
		//Update data using payload
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		
		Response res = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);

		res.then().log().body();
		
		assertEquals(res.getStatusCode(), 200);
		
		logger.info("***User updated***");
		//Checking data after update
		Response resAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		assertEquals(resAfterUpdate.getStatusCode(), 200);
		
	}
	
	@Test(priority = 4)
	public void testDeleteUserByName() {
		logger.info("***Deleting user***");
		Response res = UserEndPoints.deleteUser(this.userPayload.getUsername());
		
		assertEquals(res.getStatusCode(), 200);
		
		logger.info("***User Deleted***");
	}
	
	
	
	
	
	
}

