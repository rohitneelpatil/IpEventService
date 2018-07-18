package com.lookout.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lookout.model.Application;
import com.lookout.util.ProcessEventUtil;

/**
 * @author rohit.patil
 *
 */

/*
 * Entry point for the service Handles requests for : 1.Posting Events
 * 2.Get/Fetch Events for an application (appId) 3. Delete event counters
 */

@RestController
@RequestMapping("/")
public class EventController {

	private final Map<String, Application> applicationMap = new HashMap<String, Application>();
	private final static String Success = "Success";

	/**
	 * Process incoming events. Events are bytes of proto buff encdoded stream
	 * containing: - appID (unique) - ip (IpAddress to which the application is
	 * connected )
	 * 
	 * @param eventRequest
	 * @return 200 OK for Successful EventsProcessing
	 *         unprocessableEntity(422)/badRequest(400) for Failures
	 */

	@PostMapping(path = "/events", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> processEvents(@RequestBody final byte[] eventRequest) {

		try {
			final IpEventProtos.IpEvent ipEventProtos = IpEventProtos.IpEvent
					.parseFrom(IOUtils.toByteArray(new ByteArrayInputStream(eventRequest)));

			String appId = ipEventProtos.getAppSha256();

			long ip = ipEventProtos.getIp();
			String ipString = ProcessEventUtil.longToIp(ip);
			String netblock = ProcessEventUtil.ipToNetblock28(ipString);

			Application app = null;
			if (applicationMap.containsKey(appId))
				app = applicationMap.get(appId);
			else
				app = new Application(appId, netblock, 1);

			app.getAllIps().add(ipString);

			if (app.getIpMap().containsKey(netblock))
				app.getIpMap().get(netblock).add(ipString);
			else
				app.getIpMap().put(netblock, new HashSet<String>(Arrays.asList(ipString)));

			if (app.getIpMap().get(netblock).size() > app.getMaxSubnetIpLength()) {
				app.setMaxSubnetIpLength(app.getMaxSubnetIpLength());
				app.setMaxSubnet(netblock);
			}
			applicationMap.put(appId, app);

		} catch (InvalidProtocolBufferException e) {
			System.out.println("Exception while parsing protocol buffer IpEvent Entity: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		} catch (IOException e) {
			System.out.println("Exception while reading the octet stream: " + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception while processing the incoming event " + e.getMessage());
			e.printStackTrace();
		}

		return ResponseEntity.ok().body(Success);
	}

	/**
	 * @param appId
	 * @return For an input appId provides a list of good ips,bad ips and total
	 *         events for the appId
	 */

	@GetMapping(path = "events/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getEventStatus(@PathVariable String appId) {

		Application app = null;
		Map<String, Object> responseMap = new HashMap<>();

		if (applicationMap.containsKey(appId)) {
			app = applicationMap.get(appId);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).body(responseMap);
		}

		// Get most dense netblock as good ips
		Set<String> goodips = new TreeSet<String>(app.getIpMap().get(app.getMaxSubnet()));

		// bad ips = all ips except good ips
		Set<String> badIps = new HashSet<String>(app.getAllIps());
		badIps.removeAll(goodips);

		responseMap.put("count", app.getAllIps().size());
		responseMap.put("good_ips", goodips);
		responseMap.put("bad_ips", new TreeSet<String>(badIps));
		return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(responseMap);
	}

	/**
	 * Clear counters and applicationMap for next test run
	 * 
	 * @return 200 OK for Successful Clearing of the Counters
	 */

	@DeleteMapping(path = "/events", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> postprocess() {

		applicationMap.clear();
		return ResponseEntity.ok().body(Success);
	}
}
