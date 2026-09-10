package api.test.Pet;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.HashSet;

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
import api.payload.Tag;
import api.utilities.PetTestDataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UpdateAnExistingPetTests {

	Pet myPet, myPet2;
	Faker faker;
	String filePath = ".\\src\\test\\resources\\TestFile.txt";
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

	Logger logger;

	@BeforeTest
	public void setup() {
		faker = new Faker();

		logger = LogManager.getLogger(this.getClass());

		myPet = PetTestDataFactory.randomPet();

		myPet2 = PetTestDataFactory.randomPet();

		System.out.println("Pet 1: " + myPet.getId() + " | " + myPet.getName());

		System.out.println("Pet 2: " + myPet2.getId() + " | " + myPet2.getName());
		
		System.out.println("===================================================");
	}

	@Test
	public void mainFunctionality() {

		logger.info("***Starting TC_UEP_001***");

		myPet2.setId(myPet.getId());

		Response res = PetEndPoints.updatePet(myPet2);

		res.then().log().all();

		res.then().statusCode(200);

		logger.info("***Finished TC_UEP_001***");
	}

	@Test
	public void ResponseBody() {

		logger.info("***Starting TC_UEP_002***");

		myPet2.setId(myPet.getId());

		Response res = PetEndPoints.updatePet(myPet2);
		
		Pet responsePet = res.as(Pet.class);

		res.then().log().body();

		res.then().statusCode(200)
		.body("id", equalTo((int) myPet.getId()))
		.body("category.id", equalTo(myPet2.getCategory().getId()))
	    .body("category.name", equalTo(myPet2.getCategory().getName()))
		.body("name", equalTo(myPet2.getName()))
		.body("photoUrls", containsInAnyOrder(myPet2.getPhotoUrls().toArray()))
		.body("status", equalTo(myPet2.getStatus()));

		Assert.assertEquals(new HashSet<>(responsePet.getTags()), new HashSet<>(myPet2.getTags()));
		
		logger.info("***Finished TC_UEP_002***");
	}
	
	@DataProvider(name="InvalidIds")
	public Object[] invalidId() {
		Object[] invalidIds = {"test", -1, "@@@", "213131123123"};
		return invalidIds;
	}
	
	@Test(dataProvider="InvalidIds")
	public void Validation_updatePetWithInvalidIds(Object id) {
		
		logger.info("***Starting TC_UEP_003-004***");
		
		HashMap data = new HashMap();
		data.put("id", id);
		data.put("name", myPet2.getName());
	
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(data)
				.when()
				.put(Routes.put_updatePet_url);
		
		res.then().log().all();
		
		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);
		
		logger.info("***Finished TC_UEP_003***");
	}
	
	
	@Test()
	public void Validation_requiredFields() {
		
		logger.info("***Starting TC_UEP_005***");
		
		HashMap data = new HashMap();
		data.put("name", myPet.getName());
		data.put("photoUrls", myPet2.getPhotoUrls());
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(data)
				.when()
				.put(Routes.put_updatePet_url);
		
		res.then().log().all();
		
		Tag tags = new Tag();
		
		res.then().statusCode(200)
		.body("name", equalTo(myPet.getName()))
		.body("photoUrls", containsInAnyOrder(myPet2.getPhotoUrls().toArray()));
		
		Pet responsePet = res.as(Pet.class);
		Assert.assertTrue(responsePet.getTags() == null || responsePet.getTags().isEmpty(),
		        "Expected tags to be empty or null when omitted from request");
		
		logger.info("***Finished TC_UEP_005***");
	}
	
	@Test()
	public void Validation_withoutRequiredFields() {
		
		logger.info("***Starting TC_UEP_006***");
		
		HashMap data = new HashMap();
		
		data.put("id", faker.random().nextLong());
		data.put("category", myPet2.getCategory());
		data.put("tags",  myPet2.getTags());
		data.put("status", myPet2.getStatus());
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(data)
				.when()
				.put(Routes.put_updatePet_url);
		
		res.then().log().all();
		
		Assert.assertTrue(res.statusCode() == 405, "Unexpected status code");
		Assert.assertTrue(res.getBody() != null, "Response body is null!");
		
		logger.info("***Finished TC_UEP_006***");
	}
}
