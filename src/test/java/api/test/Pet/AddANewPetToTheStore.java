package api.test.Pet;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.PetEndPoints;
import api.endpoints.Routes;
import api.payload.Pet;
import api.utilities.PetTestDataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AddANewPetToTheStore {

	Pet myPet;
	Faker faker;
	String filePath = ".\\src\\test\\resources\\TestFile.txt";
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";
	
	Logger logger;
	
	@BeforeTest
	public void setup() {
		faker = new Faker();
		
		logger = LogManager.getLogger(this.getClass());
		
		myPet = PetTestDataFactory.randomPet();
		
	}
	
	@Test
	public void mainFunctionality() {
		
		logger.info("***Starting TC_ANP_001***");
		
		Response res = PetEndPoints.addNewPet(myPet);
		
		res.then().log().all();
		
		res.then().statusCode(200);
		
		logger.info("***Finished TC_ANP_001***");
	}
	
	@Test
	public void ResponseBody() {
		
		logger.info("***Starting TC_ANP_002***");
		
		Response res = PetEndPoints.addNewPet(myPet);
		
		Pet responsePet = res.as(Pet.class);
		
		res.then().log().body();
		
		res.then().statusCode(200)
		.body("id", equalTo(myPet.getId()))
		.body("category.id", equalTo(myPet.getCategory().getId()))
	    .body("category.name", equalTo(myPet.getCategory().getName()))
		.body("name", equalTo(myPet.getName()))
		.body("photoUrls", containsInAnyOrder(myPet.getPhotoUrls().toArray()))
		.body("status", equalTo(myPet.getStatus()));
		
		Assert.assertEquals(new HashSet<>(responsePet.getTags()), new HashSet<>(myPet.getTags()));
		
		logger.info("***Finished TC_ANP_002***");

	}
	
	
	@Test
	public void Validation_requestWithoutRequiredFields() {
		
		logger.info("***Starting TC_ANP_003***");
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
			.post(Routes.post_addNewPet_url);
		
		res.then().log().body();
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_ANP_003***");

		
	}
	
	@Test
	public void validation_fieldsSchema() {
		
		logger.info("***Starting TC_ANP_004***");
		
		Response res = PetEndPoints.addNewPet(myPet);
		
		Pet responsePet = res.as(Pet.class);
		
		res.then().log().body();
		
		res.then().statusCode(200).body(matchesJsonSchemaInClasspath("pet-schema.json"));
		
		logger.info("***Finished TC_ANP_004***");

		
	}
}
