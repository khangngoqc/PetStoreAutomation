package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	
	@BeforeClass
	public void setupData() {
		
		faker = new Faker();
		userPayload = new User();
		userPayload.setId(faker.number().randomDigit());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstname(faker.name().firstName());
		userPayload.setLastname(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().phoneNumber());
		
	}
	
	@Test
	public void testPostUser() {
		
		Response res = UserEndPoints.createUser(userPayload);
		res.then().log().all();
		
		Assert.assertEquals(res.getStatusCode(), 200);
	}
	

}

