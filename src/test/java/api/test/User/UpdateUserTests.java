package api.test.User;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.Routes;
import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class UpdateUserTests {

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

		// logs
		logger = LogManager.getLogger(this.getClass());
		
		CreateTestUser();

	}

	public void CreateTestUser() {
		logger.info("***Creating user***");
		Response res = UserEndPoints.createUser(userPayload);
		res.then().log().all();

		Assert.assertEquals(res.getStatusCode(), 200);
		
		Response res1 = UserEndPoints.readUser(this.userPayload.getUsername());
		res1.then().log().body();

		logger.info("***User info created***");
	}

	@Test(priority = 2)
	public void testUpdateUserByName_MainFunctionality() {

		logger.info("******Starting TC_US_UPS_001******");

		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());

		logger.info("***Updating user***");

		// Update data using payload
		User userPayload2 = new User();
		userPayload2.setFirstname(faker.name().firstName());
		userPayload2.setLastname(faker.name().lastName());
		userPayload2.setEmail(faker.internet().emailAddress());

		Response res = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload2);

		res.then().log().body();

		Assert.assertEquals(res.getStatusCode(), 200);
		Assert.assertTrue(res.getBody() != null);

		logger.info("***User updated***");

		// Checking data after update
		Response resAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		resAfterUpdate.then().log().body();
		
		assertEquals(resAfterUpdate.getStatusCode(), 200);

		logger.info("******Finished TC_US_UPS_001******");
	}

	@Test(priority = 3)
	public void ResponseBody() {
		logger.info("******Starting TC_US_CRU_02******");

		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());

		
		Response res = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);

		String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

		res.then().log().body();

		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));

		logger.info("******Finished TC_US_CRU_02******");
	}
	
	@Test(priority = 4)
	public void Validation_CheckUpdatedDetailGetAPI() {
		
		logger.info("******Starting TC_US_UPS_003******");
		Response resAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		
		resAfterUpdate.then().log().body();
		
		assertEquals(resAfterUpdate.getStatusCode(), 200);
		
		/*
		 * String updatedFirstName =
		 * resAfterUpdate.jsonPath().get("firstname").toString(); String updatedLastName
		 * = resAfterUpdate.jsonPath().get("lastname").toString();
		 */
		String updatedEmail = resAfterUpdate.jsonPath().get("email").toString();
		
		/*
		 * Assert.assertTrue(updatedFirstName.equals(this.userPayload.getFirstname()));
		 * Assert.assertTrue(updatedLastName.equals(this.userPayload.getLastname()));
		 */
		
		System.out.println(updatedEmail);
		System.out.println(this.userPayload.getEmail());
		
		Assert.assertTrue(updatedEmail.contains(this.userPayload.getEmail()));
		
		logger.info("******Starting TC_US_UPS_003******");

	}
	
	@Test(priority = 5)
	public void Validation_SendUpdateUserRequestWithoutLogin() {

		logger.info("******Starting TC_US_UPS_004******");


		logger.info("***Updating user***");

		// Update data using payload
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());

		Response res = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode()  >= 400);
		Assert.assertTrue(res.getBody() != null);

		logger.info("***User updated***");

		// Checking data after update
		Response resAfterUpdate = UserEndPoints.readUser(this.userPayload.getUsername());
		resAfterUpdate.then().log().body();
		
		assertEquals(resAfterUpdate.getStatusCode(), 200);

		logger.info("******Finished TC_US_UPS_004******");
	}
	
	@Test(priority = 6)
	public void Validation_SendRequestWithoutUsernameParam() {
		logger.info("******Starting TC_US_UPS_005******");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(this.userPayload)
			.pathParam("username", "")
			
		.when()
			.put(Routes.update_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_UPS_005******");
	}
	
	@Test(priority = 8)
	public void Validation_SendRequestWithoutBody() {
		logger.info("******Starting TC_US_UPS_006******");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("username", this.userPayload.getUsername())
			
		.when()
			.put(Routes.update_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_UPS_006******");
	}
	
	@Test(priority = 9)
	public void Validation_SendRequestWithInexistedUsernameParam() {
		logger.info("******Starting TC_US_UPS_007******");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(this.userPayload)
			.pathParam("username", "inexisted-username-input")
			
		.when()
			.put(Routes.update_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_UPS_007******");
	}
	
	@Test(priority = 10)
	public void Validation_SendRequestWithInvalidUsernameParam() {
		logger.info("******Starting TC_US_UPS_008******");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(this.userPayload)
			.pathParam("username", 4124514)
			
		.when()
			.put(Routes.update_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_UPS_008******");
	}
	
	@Test(priority = 11)
	public void Validation_SendRequestWithInvalidDataType() {
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		logger.info("******Starting TC_US_UPS_010******");
		
		HashMap data = new HashMap();
		data.put("id", true);
		data.put("username", null);
		data.put("firstName", "");
		data.put("lastName", 3);
		data.put("password", true);
		data.put("email", 2);
		data.put("phone", true);
		data.put("userStatus", "string");
		
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("username", this.userPayload.getUsername())
			.body(data)
			
		.when()
			.put(Routes.update_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_UPS_010******");
	}
	

}
