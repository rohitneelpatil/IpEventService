package functional;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lookout.controller.IpEventProtos;
import com.lookout.util.ProcessEventUtil;

public class EventControllerFeatureTest extends BaseTest {

	@Before
	public void delete() {
		given().when().delete(eventsEndPoint).then().body(containsString(Success)).statusCode(200);
	}

	@Test
	public void deleteEventTest() {
		given().when().delete(eventsEndPoint).then().statusCode(200);
	}

	@Test
	public void postAndGetSingleEventTest() {

		String ipAddress = goodIps.iterator().next();
		List goodip = Arrays.asList(new String[] { ipAddress });
		List badip = new ArrayList<String>();

		long ip = ProcessEventUtil.ipToLong(ipAddress);
		final IpEventProtos.IpEvent ipEvent = IpEventProtos.IpEvent.newBuilder().setAppSha256(appId).setIp(ip).build();
		given().contentType(postContentType).body(ipEvent.toByteArray()).when().post(eventsEndPoint).then()
				.statusCode(200).body(containsString(Success));

		given().accept(getContentType).get(eventsEndPoint + "/" + appId).then().body("count", equalTo(1))
				.body("good_ips", equalTo(goodip)).body("bad_ips", equalTo(badip)).statusCode(200);

	}

	@Test
	public void postAndGetMultipleEventsTest() {

		List<String> postListIps = new ArrayList<String>(goodIps);
		postListIps.addAll(badIps);

		for (String postIp : postListIps) {
			long ip = ProcessEventUtil.ipToLong(postIp);
			final IpEventProtos.IpEvent ipEvent = IpEventProtos.IpEvent.newBuilder().setAppSha256(appId).setIp(ip)
					.build();
			given().contentType(postContentType).body(ipEvent.toByteArray()).when().post(eventsEndPoint).then()
					.statusCode(200).body(containsString(Success));
		}

		given().accept(getContentType).get(eventsEndPoint + "/" + appId).then()
				.body("count", equalTo(postListIps.size())).body("good_ips", equalTo(new ArrayList<>(goodIps)))
				.body("bad_ips", equalTo(new ArrayList<>(badIps))).statusCode(200);

	}

}