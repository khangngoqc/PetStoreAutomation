package api.test.Pet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import api.endpoints.PetEndPoints;
import api.endpoints.Routes;
import api.payload.Pet;
import api.utilities.PetTestDataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdatePetWithFormDataTests {
	
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
		
		PetEndPoints.addNewPet(myPet);
	}
	
	@Test
	public void MainFunctionality() {
		Response res = PetEndPoints.updatePetForm(myPet.getId(), "test name", PetTestDataFactory.randomStatus());
		
		res.then().log().all();
		
		res.then().statusCode(405);
		Assert.assertTrue(res.getBody() != null);
		
	}
	
	
	@Test
	public void Validation_requiredFieldOnly() {
		Response res = given()
				.contentType("application/x-www-form-urlencoded")
				.pathParam("petId", myPet.getId())
				.formParam("name", "")
				.formParam("status", "")
				.when()
				.post(Routes.post_updatePetForm_url);
		
		res.then().log().all();
		
		res.then().statusCode(405);
		Assert.assertTrue(res.getBody() != null);
		
	}

}
