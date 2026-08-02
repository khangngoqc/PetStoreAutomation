package api.test.User;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.Routes;
import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class DeleteUserTests {
	
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

	@BeforeMethod
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
	public void MainFunctionality() {
		logger.info("***Starting TC_US_DLU_01***");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		Response res = UserEndPoints.deleteUser(this.userPayload.getUsername());
		
		assertEquals(res.getStatusCode(), 200);
		
		logger.info("***Finished TC_US_DLU_01***");
	}
	
	@Test(priority = 2)
	public void ResponseBody() {
		logger.info("***Starting TC_US_DLU_02**");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
		
		Response res = UserEndPoints.deleteUser(this.userPayload.getUsername());
		
		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		Assert.assertTrue(res.getBody() != null);

		
		logger.info("***Finished TC_US_DLU_02***");
	}
	
	@Test(priority = 3)
	public void Validation_SendRequestWithoutLogin() {
		logger.info("***Starting TC_US_DLU_03**");
				
		Response res = UserEndPoints.deleteUser(this.userPayload.getUsername());
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code! Found: " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_US_DLU_03***");
	}
	
	@Test(priority = 4)
	public void Validation_SendRequestWithoutUsernameParam() {
		logger.info("***Starting TC_US_DLU_04**");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
				
		Response res = UserEndPoints.deleteUser("");
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code! | Found: " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_US_DLU_04***");
	}

	@Test(priority = 5)
	public void Validation_SendRequestWithInexistedUsernameParam() {
		logger.info("***Starting TC_US_DLU_05**");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
				
		Response res = UserEndPoints.deleteUser("system-inexisted-username");
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code! | Found: " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_US_DLU_05***");
	}
	
	@Test(priority = 6)
	public void Validation_SendRequestWithInvalidUsernameParam() {
		logger.info("***Starting TC_US_DLU_06**");
		
		logger.info("***Log user in the system***");
		UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());
				
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("username", false)
			
			.when()
				.delete(Routes.delete_url);
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code! | Found: " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_US_DLU_06 ***");
	}
	
}
