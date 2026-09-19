package api.endpoints;

import static io.restassured.RestAssured.given;

import java.io.File;

import api.payload.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class PetEndPoints {
	
	public static Response uploadAnImage(long id, String data, String filePath) {
		
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
	
	public static Response updatePet(Pet petPayload) {
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(petPayload)
				.when()
				.put(Routes.put_updatePet_url);
		
		return res; 
				
	}
	
	public static Response findPetByStatus(String status) {
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.queryParam("status", status)
				.when()
				.get(Routes.get_findPetByStatus_url);
		
		return res;
	}
	
	public static Response findPetById(long id) {
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("petId", id)
				.when()
				.get(Routes.get_findPetById_url);
		
		return res;
	}
	
	public static Response updatePetForm(long id, String name , String status) {
		Response res = given()
				.contentType("application/x-www-form-urlencoded")
				.pathParam("petId", id)
				.formParam("name", name)
				.formParam("status", status)
				.when()
				.post(Routes.post_updatePetForm_url);
		
		return res; 
				
	}
	
	

}
