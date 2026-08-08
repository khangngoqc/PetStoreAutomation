package api.test.User;

import static io.restassured.RestAssured.given;

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

public class LogUserIntoTheSystemTests {

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

	@Test
	public void MainFunctionality() {
		logger.info("***Starting TC_US_LGI_01***");

		Response res = UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());

		res.then().log().all();

		Assert.assertEquals(res.getStatusCode(), 200, "Incorrect status code!");
		Assert.assertTrue(res.getBody() != null, "Incorrect status code!");

		logger.info("***Finished TC_US_LGI_01***");

	}

	@Test
	public void ResponseBody() {
		logger.info("***Starting TC_US_LGI_02***");

		Response res = UserEndPoints.loginUser(this.userPayload.getUsername(), this.userPayload.getPassword());

		res.then().log().body();

		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));

		logger.info("***Finished TC_US_LGI_02***");

	}

	@Test
	public void Validation_sendRequestWithoutParams() {
		logger.info("***Starting TC_US_LGI_03***");

		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
		
			.when()
				.get(Routes.login_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code!");
		Assert.assertTrue(res.getBody() != null, "Incorrect status code!");

		logger.info("***Finished TC_US_LGI_03***");

	}
	
	@Test(priority= 1)
	public void Validation_sendRequestWithoutUsernameParams() {
		logger.info("***Starting TC_US_LGI_04_A***");

		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("password", this.userPayload.getPassword())
						
			.when()
				.get(Routes.login_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code!");
		Assert.assertTrue(res.getBody() != null, "Incorrect status code!");

		logger.info("***Finished TC_US_LGI_04_A***");

	}

	@Test(priority= 2)
	public void Validation_sendRequestWithoutPasswordParams() {
		logger.info("***Starting TC_US_LGI_04_B***");

		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("username", this.userPayload.getUsername())
						
			.when()
				.get(Routes.login_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code!");
		Assert.assertTrue(res.getBody() != null, "Incorrect status code!");

		logger.info("***Finished TC_US_LGI_04_B***");

	}
	
	@Test(priority= 3)
	public void Validation_sendRequestWithInavalidUsernameParams() {
		logger.info("***Starting TC_US_LGI_05***");

		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("username", 99999999)
				.queryParam("password", this.userPayload.getPassword())
						
			.when()
				.get(Routes.login_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code!");
		Assert.assertTrue(res.getBody() != null, "Incorrect status code!");

		logger.info("***Finished TC_US_LGI_05***");

	}
	
	
	@Test(priority= 4)
	public void Validation_sendRequestWithInavalidPasswordParams() {
		logger.info("***Starting TC_US_LGI_06***");

		Response res = given()
				.header(null)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("username", this.userPayload.getUsername())
				.queryParam("password", 99999999)
						
			.when()
				.get(Routes.login_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Incorrect status code!");
		Assert.assertTrue(res.getBody() != null, "Incorrect status code!");

		logger.info("***Finished TC_US_LGI_06***");

	}
	
	
	
}
