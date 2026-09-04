package api.endpoints;

import static io.restassured.RestAssured.given;

import java.io.File;

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

}
