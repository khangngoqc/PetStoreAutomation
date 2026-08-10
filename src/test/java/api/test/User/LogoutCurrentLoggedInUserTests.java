package api.test.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class LogoutCurrentLoggedInUserTests {

	Faker faker;
	User userPayload;

	public Logger logger;

	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

	@BeforeClass
	public void Setup() {

		faker = new Faker();

		userPayload = new User(faker.number().randomDigit(), faker.name().username(), faker.name().firstName(),
				faker.name().lastName(), faker.internet().emailAddress(), faker.internet().password(),
				faker.phoneNumber().cellPhone(), faker.random().nextInt(0, 2));

		// logs
		logger = LogManager.getLogger(this.getClass());
	}

	@Test(priority = 1)
	public void MainFunctionality() {

		logger.info("******Starting TC_US_LGO_01******");

		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		logger.info("***Send logout request***");
		
		Response res = UserEndPoints.logoutUser();
		res.then().log().all();
		
		Assert.assertEquals(res.getStatusCode(), 200, "Incorrect expected status code! | ");
		Assert.assertTrue(res.getBody() != null, "Response body is null! | ");
		
		logger.info("******Finished TC_US_LGO_01******");

	}
	
	@Test
	public void ResponseBody() {
		
		logger.info("******Starting TC_US_LGO_02******");

		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		logger.info("***Send logout request***");
		
		Response res = UserEndPoints.logoutUser();
		res.then().log().body();
		
		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		
		logger.info("******Finished TC_US_LGO_02******");
	}
	
	
	@Test
	public void Validation_SendRequestWithoutLogin() {
		
		logger.info("******Starting TC_US_LGO_03******");

		/*
		 * logger.info("***Log user in the system***");
		 * UserEndPoints.loginUser(this.userPayload.getUsername(),
		 * this.userPayload.getPassword());
		 */
		
		logger.info("***Send logout request***");
		
		Response res = UserEndPoints.logoutUser();
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect expected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null, "Response body is null! | ");
		
		logger.info("******Finished TC_US_LGO_03******");
	}
	
	
	
	

}
