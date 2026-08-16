package api.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class StoreEndpoints {
	
	public static Response returnPetInventories() {
		
		Response res = given()
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					
				.when()
					.get(Routes.get_inventoriesByStatus_url);
		
		return res;		
	}

}
