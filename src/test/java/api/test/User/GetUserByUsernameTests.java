package api.test.User;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetUserByUsernameTests {

	Faker faker;
	User userPayload;
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

	public Logger logger;

	public void CreateTestUser() {
		logger.info("***Creating user***");
		Response res = UserEndPoints.createUser(userPayload);
		res.then().log().all();

		Assert.assertEquals(res.getStatusCode(), 200);

		logger.info("***User info created***");
	}

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

	@Test(priority = 1)
	public void mainFunctionality() {

		logger.info("******Starting TC_US_GUN_001******");

		Response res = UserEndPoints.readUser(this.userPayload.getUsername());
		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() == 200, "Inccorect expected status code");
		Assert.assertTrue(res.getBody() != null, "Response body is null!");

		logger.info("******Finished TC_US_GUN_001******");

	}

	@Test(priority = 2)
	public void responseBody() {
		logger.info("******Starting TC_US_GUN_002******");

		Response res = UserEndPoints.readUser(this.userPayload.getUsername());
		res.then().log().body();

		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		
		res.then().extract().response();
		
		String jsonBody = res.asString();
		JsonPath jsonPath = res.jsonPath();

		String firstName = jsonPath.getString("firstname");
		String lastName = jsonPath.getString("lastname");
		String email = jsonPath.getString("email");
		String password = jsonPath.getString("password");
		String phone = jsonPath.getString("phone");
		int userStatus = jsonPath.getInt("userStatus");
		
		assertEquals(firstName, this.userPayload.getFirstname(), "Incorrect first name!");
		assertEquals(lastName, this.userPayload.getLastname(), "Incorrect last name!");
		assertEquals(email, this.userPayload.getEmail(), "Incorrect email!");
		assertEquals(password, this.userPayload.getPassword());
		assertEquals(phone, this.userPayload.getPhone());
		Assert.assertTrue(userStatus == 0, "Incorrect userStatus");
		

		logger.info("******Finished TC_US_GUN_002******");
	}
	
	@Test(priority = 3)
	public void Validation_SendRequestWithInexistedUsernameParam() {
		logger.info("******Starting TC_US_GUN_003******");
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(this.userPayload)
			.pathParam("username", "inexisting-username-input")
			
		.when()
			.get(Routes.get_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_GUN_003******");
	}
	
	@Test(priority = 4)
	public void Validation_SendRequestWithoutUsernameParam() {
		logger.info("******Starting TC_US_GUN_004******");
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(this.userPayload)
			.pathParam("username", "")
			
		.when()
			.get(Routes.get_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_GUN_004******");
	}
	
	
	@Test(priority = 5)
	public void Validation_SendRequestInvalidUsernameParam() {
		logger.info("******Starting TC_US_GUN_005******");
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(this.userPayload)
			.pathParam("username", 123)
			
		.when()
			.get(Routes.get_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_GUN_005******");
	}

}
