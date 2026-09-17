package api.test.Pet;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

import java.util.HashMap;

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

public class FindPetsByIdTest {
	
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
	
	@Test()
	public void MainFunctionality() {
		
		logger.info("***Starting TC_FPI_01***");
		
		Response res = PetEndPoints.findPetById(myPet.getId());
		
		res.then().log().all();
		
		res.then().statusCode(200)
		.body("id", equalTo((int) myPet.getId()))
		.body("category.id", equalTo(myPet.getCategory().getId()))
	    .body("category.name", equalTo(myPet.getCategory().getName()))
		.body("name", equalTo(myPet.getName()))
		.body("photoUrls", containsInAnyOrder(myPet.getPhotoUrls().toArray()))
		.body("status", equalTo(myPet.getStatus()));
		
		logger.info("***Finished TC_FPI_001***");
		
	}
	
	@Test()
	public void Validation_RequestWithInexistingPetId() {
		
		logger.info("***Starting TC_FPI_02***");
		
		Response res = PetEndPoints.findPetById(1254125125);
		
		res.then().log().body();
		
		res.then().statusCode(404)
		.header("Content-Type", equalTo("application/json"))
		.header("access-control-allow-origin", equalTo("*"))
		.header("access-control-allow-methods", equalTo("GET, POST, DELETE, PUT"))
		.header("access-control-allow-headers", equalTo("Content-Type, api_key, Authorization"))
		.header("Server", equalTo("Jetty(9.2.9.v20150224)"));
		
		logger.info("***Finished TC_FPI_002***");

	}
	
	@DataProvider(name="InvalidIds")
	public Object[] invalidId() {
		Object[] invalidIds = {"test", -1, "@@@"};
		return invalidIds;
	}
	
	@Test(dataProvider="InvalidIds")
	public void Validation_FindPetWithInvalidIds(Object id) {
		
		logger.info("***Starting TC_FPI_003***");
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("petId", id)
				.when()
				.get(Routes.get_findPetById_url);
		
		res.then().log().all();
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_FPI_003***");
	}

}
