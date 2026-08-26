package api.test.Store;

import static io.restassured.RestAssured.given;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.Routes;
import api.endpoints.StoreEndpoints;
import api.payload.Order;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class DeletePurchaseOrderByIdTests {

	Order orderPayload;
	Logger logger;
	Faker faker;
	String minimalSchema = "{\n" + "  \"type\": \"object\"\n" + "}";

	public String randomDate() {

		// Generate a random date (e.g., between now and 1 year from now)
		Date randomDate = faker.date().future(365, java.util.concurrent.TimeUnit.DAYS);
		// or faker.date().past(...) / faker.date().birthday() etc.

		// Format to ISO-8601 with milliseconds and Z suffix
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // ensures 'Z' is accurate (UTC)

		String formattedDate = sdf.format(randomDate);

		return formattedDate;
	}

	public void createTestOrder() {

		Response res = StoreEndpoints.placeAnOrder(orderPayload);

	}

	@BeforeMethod
	public void setup() {

		faker = new Faker();

		logger = LogManager.getLogger(this.getClass());

		String randomShipDate = randomDate();

		orderPayload = new Order(faker.number().randomDigitNotZero(), faker.number().randomDigit(),
				faker.random().nextInt(1, 5), randomShipDate, "placed", faker.random().nextBoolean());

		createTestOrder();
	}

	@Test
	public void MainFunctionality() {

		logger.info("***Starting TC_ST_DP_01***");
		
		String testOrderId = Integer.toString(this.orderPayload.getId());

		Response res = StoreEndpoints.DeleteOrderById(testOrderId);

		res.then().log().all();

		res.then().statusCode(200);
		Assert.assertNotNull(res.getBody().asString());

		// Validate with FindOrderByIdRequest
		Response res2 = StoreEndpoints.findOrderById(testOrderId);

		res.then().log().all();

		res.then().statusCode(400);
		Assert.assertNotNull(res.getBody().asString());

		logger.info("***Starting TC_ST_DP_01***");
	}

	@Test
	public void ResponseBody() {

		logger.info("***Starting TC_ST_DP_02***");
		
		String testOrderId = Integer.toString(this.orderPayload.getId());

		Response res = StoreEndpoints.DeleteOrderById(testOrderId);

		res.then().log().body();

		res.then().assertThat().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(minimalSchema));
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_DP_02***");


	}
	
	@DataProvider(name="invalidId")
	public Object[] invalidValue() {
		
		Object[] invalidValues = {-10, "@@@@@", 999};
		
		return invalidValues;
	}

	
	@Test(dataProvider="invalidId")
	public void Validation_InvalidIDRequest(Object id) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("orderId", id)
				.when()
					.delete(Routes.delete_PurchaseOrderById_url);
		
		res.then().log().body();
		
		
		Assert.assertTrue(res.getStatusCode() >= 400 , "Unexpected status code!");		
		Assert.assertNotNull(res.getBody().asString());

	}
	
	@DataProvider(name="InvalidParamDataType")
	public Object[] InvalidParamDataType() {
		
		Object[] invalidValues = {"one", true, " "};
		
		return invalidValues;
	}

	
	@Test(dataProvider="InvalidParamDataType")
	public void Validation_InvalidParamDataTypeRequest(Object id) {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("orderId", id)
				.when()
					.delete(Routes.delete_PurchaseOrderById_url);
		
		res.then().log().body();
		
		
		Assert.assertTrue(res.getStatusCode() >= 400 , "Unexpected status code!");		
		Assert.assertNotNull(res.getBody().asString());

	}
	
	
}
