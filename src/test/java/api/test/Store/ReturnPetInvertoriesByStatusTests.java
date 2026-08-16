package api.test.Store;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.StoreEndpoints;
import io.restassured.response.Response;

public class ReturnPetInvertoriesByStatusTests {
	
	public static Logger logger;
	
	@BeforeClass
	public void setup() {
		logger = LogManager.getLogger(this.getClass());
	}
	
	@Test()
	public void MainFunctionaility() {
		
		logger.info("***Starting TC_ST_RP_01***");
		
		Response res = StoreEndpoints.returnPetInventories();
		
		res.then().log().all();
		
		res.then()
		.statusCode(200)
        .body("additionalProp1", equalTo(0))
        .body("additionalProp2", equalTo(0))
        .body("additionalProp3", equalTo(0));
		
		logger.info("***Starting TC_ST_RP_01***");
		
	}
	
	@Test()
	public void ResponseBody() {
		
		logger.info("***Starting TC_ST_RP_02***");
		
		Response res = StoreEndpoints.returnPetInventories();
		
		res.then().log().body();
		
		res.then()
		.statusCode(200)
		.body(matchesJsonSchemaInClasspath("Inventory-schema-JSON.json"));
		
		logger.info("***Starting TC_ST_RP_02***");
		
	}

}
