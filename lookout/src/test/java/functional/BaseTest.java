package functional;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.BeforeClass;

import com.jayway.restassured.RestAssured;

public class BaseTest {

	/***
	 * Define common constants to be used within all functional test_cases
	 * 
	 * 
	 */
	static final String appId = "test_app_id";
	static final String postContentType = "application/octet-stream";
	static final String getContentType = "application/json";
	static final String eventsEndPoint = "/events";
	static final String Success = "Success";

	static final Set<String> goodIps = new TreeSet<String>(
			Arrays.asList(new String[] { "11.134.137.0", "11.134.137.1", "11.134.137.2", "11.134.137.3", "11.134.137.4",
					"11.134.137.5", "11.134.137.6", "11.134.137.7", "11.134.137.8", "11.134.137.9", "11.134.137.10",
					"11.134.137.11", "11.134.137.12", "11.134.137.13", "11.134.137.14" }));
	static final Set<String> badIps = new TreeSet<String>(Arrays.asList(new String[] { "11.124.137.0", "165.134.137.1",
			"192.134.137.2", "143.134.137.3", "15.134.137.4", "17.134.137.5", "18.134.137.6", "181.134.137.7",
			"11.134.135.8", "11.135.137.9", "11.136.137.10", "11.132.137.11", "11.131.137.12", "14.134.137.14" }));

	@BeforeClass
	public static void setup() {
		String port = System.getProperty("server.port");
		if (port == null) {
			RestAssured.port = Integer.valueOf(8080);
		} else {
			RestAssured.port = Integer.valueOf(port);
		}

		String basePath = System.getProperty("server.base");
		if (basePath == null) {
			basePath = "/";
		}
		RestAssured.basePath = basePath;

		String baseHost = System.getProperty("server.host");
		if (baseHost == null) {
			baseHost = "http://localhost";

			// baseHost = "http://ec2-54-86-73-176.compute-1.amazonaws.com";
			// Connect to ec2 for host testing
		}
		RestAssured.baseURI = baseHost;

	}

}