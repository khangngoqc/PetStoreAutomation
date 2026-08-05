package api.test.User;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

public class CreatesListOfUserWithGivenInputArrayTests {

	Faker faker;

	List<User> users;
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

	public Logger logger;

	@BeforeClass
	public void setupData() {

		faker = new Faker();
		users = Arrays.asList(
				new User(faker.number().randomDigit(), faker.name().username(), faker.name().firstName(),
						faker.name().lastName(), faker.internet().emailAddress(), faker.internet().password(),
						faker.phoneNumber().cellPhone(), faker.random().nextInt(0, 2)),
				new User(faker.number().randomDigit(), faker.name().username(), faker.name().firstName(),
						faker.name().lastName(), faker.internet().emailAddress(), faker.internet().password(),
						faker.phoneNumber().cellPhone(), faker.random().nextInt(0, 2)),
				new User(faker.number().randomDigit(), faker.name().username(), faker.name().firstName(),
						faker.name().lastName(), faker.internet().emailAddress(), faker.internet().password(),
						faker.phoneNumber().cellPhone(), faker.random().nextInt(0, 2)));

		// logs
		logger = LogManager.getLogger(this.getClass());

	}

	@Test
	public void MainFunctionality() {
		logger.info("***Starting TC_US_CLU_001***");
		Response res = UserEndPoints.createListofUser(this.users);
		res.then().log().body();

		Assert.assertEquals(res.getStatusCode(), 200);
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Finished TC_US_CLU_001***");
	}

	@Test(priority = 2)
	public void ResponseBody() {
		logger.info("***Starting TC_US_CLU_002**");

		Response res = UserEndPoints.createListofUser(this.users);
		res.then().log().body();

		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Finished TC_US_CLU_002***");
	}

	@Test(priority = 3)
	public void Validation_SendRequestWithoutBody() {
		logger.info("***Starting TC_US_CLU_003***");
		Response res = given().contentType(ContentType.JSON).accept(ContentType.JSON).when()
				.post(Routes.post_createWithListArray_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Finished TC_US_CLU_003***");
	}

	
	@Test(priority = 4)
	public void Validation_SendRequestWithInsufficientBody() {

		logger.info("***Intialize test data***");

		List<User> users_2 = Arrays.asList(
				new User(faker.number().randomDigit(), faker.name().username(), faker.name().firstName(),
						faker.name().lastName(), faker.internet().emailAddress(), faker.internet().password(),
						faker.phoneNumber().cellPhone(), faker.random().nextInt(0, 2)),
				new User(faker.number().randomDigit(), faker.name().username(), faker.name().firstName()));

		logger.info("***Starting TC_US_GUN_004***");

		Response res = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(users_2).when()
				.post(Routes.post_createWithListArray_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Finished TC_US_GUN_004***");
	}

	
	@Test(priority = 5)
	public void Validation_SendRequestWithInvalidDataType() {
		logger.info("******Starting TC_US_CLU_005******");

		HashMap data = new HashMap();
		data.put("id", true);
		data.put("username", null);
		data.put("firstName", "");
		data.put("lastName", 3);
		data.put("password", true);
		data.put("email", 2);
		data.put("phone", true);
		data.put("userStatus", "string");
		
		List<HashMap> users_2 = Arrays.asList(data);

		Response res = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(users_2).when()
				.post(Routes.post_createWithListArray_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);

		logger.info("******Finished TC_US_CLU_005******");
	}

}