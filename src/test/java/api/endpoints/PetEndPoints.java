package api.endpoints;

import static io.restassured.RestAssured.given;

import java.io.File;

import api.payload.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class PetEndPoints {
	
	public static Response uploadAnImage(int id, String data, String filePath) {
		
		Response res = given()
				.pathParam("petId", id)
				.multiPart("additionalMetadata", data)
				.multiPart("file", new File(filePath))
			.when()
				.post(Routes.post_uploadImage_url);
		
		return res;
	}
	
	public static Response addNewPet(Pet petPayload) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.body(petPayload)
				.when()
			.post(Routes.post_addNewPet_url);
			
		return res;
	}
	
	

}
