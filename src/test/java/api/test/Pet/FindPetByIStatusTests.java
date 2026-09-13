package api.test.Pet;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.PetEndPoints;
import api.endpoints.Routes;
import api.payload.Pet;
import api.utilities.PetTestDataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;	

public class FindPetByIStatusTests {

	Pet myPet;
	Faker faker;
	String filePath = ".\\src\\test\\resources\\TestFile.txt";
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

	Logger logger;
	
	@BeforeTest
	public void setup() {

		logger = LogManager.getLogger(this.getClass());

		myPet = PetTestDataFactory.randomPet();

		PetEndPoints.addNewPet(myPet);

	}
	
	@DataProvider(name="PetStatus")
	private String[] status() {
	
		String[] statuses = {"pending", "sold", "available"};
		
		return statuses;
		
	}
	
	
	@Test(dataProvider="PetStatus")
	public void MainFunctionality(String status) {
		
		Response res = PetEndPoints.findPetByStatus(status);
		
		//res.then().log().all();
		
		res.then().statusCode(200)
		.body("status", everyItem(equalTo(status)));
		
	}
	
	@DataProvider(name="InvalidStatus")
	private String[] invalid_Status() {
	
		String[] statuses = {"123", " ", "lost"};
		
		return statuses;
		
	}
	
	@Test(dataProvider = "InvalidStatus")
	public void validation_invalidStatus(String status) {
		
		Response res = PetEndPoints.findPetByStatus(status);
		
		res.then().log().all();
		
		res.then().statusCode(400);
		Assert.assertTrue(res.getBody() != null);
		
	}
	
	@Test
	public void validation_everyStatusQuery() {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("status", "sold")
				.queryParam("status", "pending")
				.queryParam("status", "available")
				
				.when()
				.get(Routes.get_findPetByStatus_url);
		
		res.then().log().body().body("status", everyItem(anyOf(equalTo("available"), equalTo("pending"), equalTo("sold"))));
		
		res.then().statusCode(200);
	}
	
	@Test
	public void validation_mixingStatusQuery() {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("status", "sold")
				.queryParam("status", "lost")
				.queryParam("status", "available")
				
				.when()
				.get(Routes.get_findPetByStatus_url);
		
		//res.then().log().body().body("status", everyItem(anyOf(equalTo("available"), equalTo("pending"), equalTo("sold"))));
		res.then().log().body();
		
		res.then().statusCode(400);
	}
	
	
	

}
