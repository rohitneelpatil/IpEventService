package functional;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Before;
import org.junit.Test;

import com.lookout.controller.IpEventProtos;

public class EventControllerStatusTest extends BaseTest {

	@Before
	public void delete() {
		given().when().delete(eventsEndPoint).then().body(containsString(Success)).statusCode(200);
	}

	@Test
	public void deleteTest() {
		given().when().delete(eventsEndPoint).then().statusCode(200);
	}

	@Test
	public void postTest() {
		final IpEventProtos.IpEvent ipEvent = IpEventProtos.IpEvent.newBuilder().setAppSha256(appId).setIp(100000)
				.build();

		given().contentType(postContentType).body(ipEvent.toByteArray()).when().post(eventsEndPoint).then()
				.statusCode(200).body(containsString(Success));
	}

	@Test
	public void postInvalidProtoBuf() {
		String ipEvent = "test_ipEventProto";
		given().contentType(postContentType).body(ipEvent.getBytes()).when().post(eventsEndPoint).then()
				.statusCode(422);
	}

	@Test
	public void getTest() {

		final IpEventProtos.IpEvent ipEvent = IpEventProtos.IpEvent.newBuilder().setAppSha256(appId).setIp(100000)
				.build();

		given().contentType(postContentType).body(ipEvent.toByteArray()).when().post(eventsEndPoint).then()
				.statusCode(200).body(containsString(Success));

		given().accept(getContentType).get(eventsEndPoint + "/" + appId).then().statusCode(200);

	}

	@Test
	public void getTestUnknownApp() {
		given().accept(getContentType).get(eventsEndPoint + "/" + appId).then().statusCode(404);

	}

}