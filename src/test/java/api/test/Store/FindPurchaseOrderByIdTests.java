package api.test.Store;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.Routes;
import api.endpoints.StoreEndpoints;
import api.payload.Order;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FindPurchaseOrderByIdTests {

	String purchaseId = "1";

	Order orderPayload;
	Logger logger;
	Faker faker;

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

	@DataProvider(name = "orderId")
	public String[] outOfBoundData() {

		String[] outOfBoundValues = { "-1", "0", "11" };

		return outOfBoundValues;

	}
	
	@DataProvider(name = "orderId_InvalidParamDataType")
	public Object[] InvalidDataType() {

		Object[] outOfBoundValues = { "one", true, " "};

		return outOfBoundValues;

	}

	@BeforeTest
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

		logger.info("***Starting TC_ST_FP_01***");

		String testOrderId = Integer.toString(this.orderPayload.getId());

		Response res = StoreEndpoints.findOrderById(testOrderId);

		res.then().log().all();

		res.then().statusCode(200);
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_FP_01***");

	}

	@Test
	public void ResponseBody() {

		logger.info("***Starting TC_ST_FP_02***");

		String testOrderId = Integer.toString(this.orderPayload.getId());

		Response res = StoreEndpoints.findOrderById(testOrderId);

		res.then().log().body();

		res.then().statusCode(200).body(matchesJsonSchemaInClasspath("order-response-schema.json"));
		;

		Order responseOrder = res.as(Order.class);

		Assert.assertEquals(responseOrder.getId(), this.orderPayload.getId());
		Assert.assertEquals(responseOrder.getPetId(), this.orderPayload.getPetId());
		Assert.assertEquals(responseOrder.getQuantity(), this.orderPayload.getQuantity());
		Assert.assertEquals(responseOrder.getStatus(), this.orderPayload.getStatus());
		Assert.assertEquals(responseOrder.isComplete(), this.orderPayload.isComplete());

		logger.info("***Starting TC_ST_FP_02***");

	}

	@Test(dataProvider = "orderId")
	public void Validation_OutOfBoundRequest(String id) {

		logger.info("***Starting TC_ST_FP_03***");

		// String testOrderId = Integer.toString(this.orderPayload.getId());

		Response res = StoreEndpoints.findOrderById(id);

		res.then().log().all();

		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_FP_03***");

	}

	@Test(dataProvider = "orderId_InvalidParamDataType")
	public void Validation_InvalidDataTypeParams(Object id) {

		logger.info("***Starting TC_ST_FP_04***");

		// String testOrderId = Integer.toString(this.orderPayload.getId());

		Response res = given()
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.pathParam("orderId", id)
				.when()
					.get(Routes.get_findPurchaseOrderById_url);

		res.then().log().all();

		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_FP_04***");

	}
	
}
