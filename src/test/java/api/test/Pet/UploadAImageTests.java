package api.test.Pet;

import static io.restassured.RestAssured.given;

import java.io.File;

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
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class UploadAImageTests {
	
	Pet myPet;
	Faker faker;
	String filePath = ".\\src\\test\\resources\\TestFile.txt";
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";
	
	Logger logger;
	
	@BeforeTest
	public void setup() {
		
		logger = LogManager.getLogger(this.getClass());
		
		myPet = PetTestDataFactory.randomPet();
	}
	
	@Test
	public void MainFunctionality() {
		
		logger.info("***Starting TC_UAI_001***");
		
		Response res = PetEndPoints.uploadAnImage(myPet.getId(), "my test data", filePath);
		
		res.then().log().all();
		
		res.then().assertThat().statusCode(200);
		
		logger.info("***Finished TC_UAI_001***");
	}
	
	@Test
	public void RepsonseBody() {
		
		logger.info("***Starting TC_UAI_002***");
		
		Response res = PetEndPoints.uploadAnImage(myPet.getId(), "my test data", filePath);
		
		res.then().log().body();
		
		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_UAI_002***");
	}
	
	@Test
	public void Validation_requestWithoutFormData() {
		
		logger.info("***Starting TC_UAI_003***");
		
		Response res = given()
				.pathParam("petId", myPet.getId())
			.when()
				.post(Routes.post_uploadImage_url);
		
		res.then().log().body();
		
		Assert.assertTrue(res.statusCode() >= 400, "Unexpected status code");
		Assert.assertTrue(res.getBody() != null, "Response body is null!");
		
		logger.info("***Finshed TC_UAI_003***");

		
	}
	
	@DataProvider(name="InvalidIds")
	public Object[] invalidId() {
		
		Object[] invalidIds = {9999, "test", -1, "@@@@@" };
		
		return invalidIds;
	}
 	
	@Test(dataProvider="InvalidIds")
	public void Validation_requestInvalidIds(Object id) {
		
		logger.info("***Starting TC_UAI_004***");
		
		Response res = given()
				.pathParam("petId", id)
				.multiPart("additionalMetadata", "my test data")
				.multiPart("file", new File(filePath))
			.when()
				.post(Routes.post_uploadImage_url);
		
		res.then().log().body();
		
		Assert.assertTrue(res.statusCode() >= 400, "Unexpected status code");
		Assert.assertTrue(res.getBody() != null, "Response body is null!");
		
		logger.info("***Finished TC_UAI_004***");
		
	}
	
	
	

}
