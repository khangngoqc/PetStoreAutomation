package api.endpoints;

import static io.restassured.RestAssured.given;

import api.payload.Order;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StoreEndpoints {
	
	public static Response returnPetInventories() {
		
		Response res = given()
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					
				.when()
					.get(Routes.get_inventoriesByStatus_url);
		
		return res;		
	}
	
	public static Response placeAnOrder(Order order) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(order)
			.when()
				.post(Routes.post_placeAnOrderForAPet_url);
		return res;
	}
	
	public static Response findOrderById(String id) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("orderId", id)
				.when()
					.get(Routes.get_findPurchaseOrderById_url);
		
		return res;
		
	}
	
	public static Response DeleteOrderById(String id) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("orderId", id)
				.when()
					.delete(Routes.delete_PurchaseOrderById_url);
		
		return res;
	}
	

}
