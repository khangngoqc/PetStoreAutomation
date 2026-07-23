package api.test.User;

import static io.restassured.RestAssured.given;

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

public class CreateUserTests {
	
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
	public void MainFunctionality() {
		logger.info("***Starting TC_US_CRU_01***");
		Response res = UserEndPoints.createUser(userPayload);
		res.then().log().body();
		
		Assert.assertEquals(res.getStatusCode(), 200);
		
		logger.info("***Finished TC_US_CRU_01***");
	}
	
	@Test(priority = 2)
	public void ResponseBody() {
		logger.info("******Starting TC_US_CRU_02******");
		Response res = UserEndPoints.createUser(userPayload);
		
		 String minimalSchema = "{\n" +
	                "  \"type\": \"object\"\n" +
	                "}";
		 
		res.then().log().body();
		
		res.then().assertThat()
        	.statusCode(200)
        	.body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		
		
		
		logger.info("******Finished TC_US_CRU_02******");
	}
	
	@Test(priority = 3)
	public void Validation_SendRequestWithoutBody() {
		logger.info("******Starting TC_US_CRU_03******");
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)	
		.when()
			.post(Routes.post_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_CRU_03******");
	}
	
	@Test(priority = 4)
	public void Validation_SendRequestWithInsufficientBody() {
		logger.info("******Starting TC_US_CRU_03******");
		
		User userPayload2 = new User();
		userPayload2.setId(faker.number().randomDigit());
		userPayload2.setUsername(faker.name().username());
		userPayload2.setFirstname(faker.name().firstName());
		
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(userPayload2)
		.when()
			.post(Routes.post_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_CRU_03******");
	}
	
	@Test(priority = 5)
	public void Validation_SendRequestWithInvalidDataType() {
		logger.info("******Starting TC_US_CRU_03******");
		
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
			.body(data)
			
		.when()
			.post(Routes.post_url);
				
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400);
		Assert.assertTrue(res.getBody() != null);
	
		logger.info("******Finished TC_US_CRU_03******");
	}
	

}
