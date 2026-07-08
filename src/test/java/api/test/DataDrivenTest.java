package api.test;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTest {

	
	@Test(priority =1 ,dataProvider = "Data", dataProviderClass = DataProviders.class)
	public void testPostUser(String userID,String userName, String fname, String lname, String userEmail, String password, String phoneNumber ) {
		
		User userPayload = new User();
		userPayload.setId(Integer.parseInt(userID));
		userPayload.setUsername(userName);
		userPayload.setFirstname(fname);
		userPayload.setLastname(lname);
		userPayload.setEmail(userEmail);
		userPayload.setPassword(password);
		userPayload.setPhone(phoneNumber);
		
		Response res = UserEndPoints.createUser(userPayload);
		
		Assert.assertEquals(res.getStatusCode(), 200);
	}
	
	@Test(priority = 2, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
	public void testDeleteUserByName(String userName) {
		
		Response res = UserEndPoints.deleteUser(userName);
		assertEquals(res.getStatusCode(), 200);
		
	}
	
	
	
}
