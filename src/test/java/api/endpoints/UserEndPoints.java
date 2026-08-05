package api.endpoints;

import static io.restassured.RestAssured.given;

import java.util.List;
import java.util.Map;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;	


//UserEndPoints.java
//Created for perform Create, Read, Update, Delete requests the user API.

public class UserEndPoints {
	
	public static Response createUser(User payload)
	{
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(payload)
		
		.when()
			.post(Routes.post_url);
		
		return res;
	}
	
	public static Response readUser(String userName)
	{
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("username", userName)
		
		.when()
			.get(Routes.get_url);
		
		return res;
	}
	
	public static Response updateUser(String userName, User payload)
	{
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("username", userName)
			.body(payload)
		
		.when()
			.put(Routes.update_url);
		
		return res;
	}
	
	public static Response deleteUser(String userName)
	{
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("username", userName)
		
		.when()
			.delete(Routes.delete_url);
		
		return res;
	}
	
	public static Response loginUser(String userName, String password)
	{
		Response res = given()
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.queryParam("username", userName)
			.queryParam("password", userName)
	
		.when()
			.get(Routes.login_url);
		
		return res;
	}
	
	public static Response createListofUser(List<User> users) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(users)
			.when()
				.post(Routes.post_createWithListArray_url);
			
			return res;
		
	}
	
	
	
	
	
	

}
