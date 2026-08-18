package api.test.Store;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.Routes;
import api.endpoints.StoreEndpoints;
import api.payload.Order;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PlaceAnOrderForAPetTests {

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
	
	@BeforeTest
	public void setup() {

		faker = new Faker();

		logger = LogManager.getLogger(this.getClass());

		String randomShipDate = randomDate();

		orderPayload = new Order(faker.number().randomDigit(), faker.number().randomDigit(),
				faker.random().nextInt(1, 5), randomShipDate, "placed", faker.random().nextBoolean());

	}

	

	@Test
	public void MainFunctionality() {

		logger.info("***Starting TC_ST_PL_01***");

		Response res = StoreEndpoints.placeAnOrder(orderPayload);
		res.then().log().all();

		res.then().statusCode(200);

		Assert.assertTrue(res.getBody() != null, "Response body is null!");

		logger.info("***Starting TC_ST_PL_01***");
	}

	@Test
	public void ResponseBody() {

		logger.info("***Starting TC_ST_PL_02***");

		Response res = StoreEndpoints.placeAnOrder(orderPayload);
		res.then().log().body();

		/*
		 * InputStream schemaStream = getClass().getClassLoader()
		 * .getResourceAsStream("order-response-schema.json");
		 * 
		 * if (schemaStream == null) { throw new
		 * IllegalStateException("Schema file not found on classpath!"); }
		 */

		res.then().statusCode(200).body(matchesJsonSchemaInClasspath("order-response-schema.json"));
		;

		Order responseOrder = res.as(Order.class);

		Assert.assertEquals(responseOrder.getId(), this.orderPayload.getId());
		Assert.assertEquals(responseOrder.getPetId(), this.orderPayload.getPetId());
		Assert.assertEquals(responseOrder.getQuantity(), this.orderPayload.getQuantity());
		Assert.assertEquals(responseOrder.getStatus(), this.orderPayload.getStatus());
		Assert.assertEquals(responseOrder.isComplete(), this.orderPayload.isComplete());

		logger.info("***Starting TC_ST_PL_02***");

	}

	@Test
	public void Validation_SendRquestWithoutBody() {

		logger.info("***Starting TC_ST_PL_03***");

		Order emptyPayload = new Order();

		Response res = StoreEndpoints.placeAnOrder(emptyPayload);
		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_PL_03***");

	}

	@Test
	public void Validation_SendRquestWithInvalidBody() {

		logger.info("***Starting TC_ST_PL_04***");

		HashMap invalidDataPayload = new HashMap();
		invalidDataPayload.put("id", true);
		invalidDataPayload.put("petId", null);
		invalidDataPayload.put("quantity", true);
		invalidDataPayload.put("shipDate", 2);
		invalidDataPayload.put("status", null);
		invalidDataPayload.put("complete", "string");

		Response res = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(invalidDataPayload).when()
				.post(Routes.post_placeAnOrderForAPet_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_PL_04***");

	}

	@Test
	public void Validation_SendRquestInsufficientBody() {

		logger.info("***Starting TC_ST_PL_05***");

		String randomShipDate = randomDate();
		
		HashMap insufficientDataPayload = new HashMap();
		insufficientDataPayload.put("id", faker.number().randomDigit());
		//insufficientDataPayload.put("petId", faker.number().randomDigit());
		insufficientDataPayload.put("quantity", faker.random().nextInt(1, 5));
		//insufficientDataPayload.put("shipDate", randomShipDate);
		//insufficientDataPayload.put("status", "placed");
		//insufficientDataPayload.put("complete", faker.random().nextBoolean());

		Response res = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(insufficientDataPayload)
				.when().post(Routes.post_placeAnOrderForAPet_url);

		res.then().log().body();

		Assert.assertTrue(res.getStatusCode() >= 400, "Unexpected status code! | " + res.getStatusCode());
		Assert.assertTrue(res.getBody() != null);

		logger.info("***Starting TC_ST_PL_05***");

	}

}
