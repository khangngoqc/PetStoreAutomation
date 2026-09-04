package api.test.Pet;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.PetEndPoints;
import api.payload.Pet;
import api.utilities.PetTestDataFactory;
import io.restassured.response.Response;

public class UploadAImageTests {
	
	Pet myPet;
	Faker faker;
	String filePath = ".\\src\\test\\resources\\TestFile.txt";
	
	@BeforeTest
	public void setup() {
		myPet = PetTestDataFactory.randomPet();
	}
	
	@Test
	public void MainFunctionality() {
		Response res = PetEndPoints.uploadAnImage(myPet.getId(), "my test data", filePath);
		
		res.then().log().all();
		
		res.then().assertThat().statusCode(200);
	}

}
