package unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.lookout.util.ProcessEventUtil;

public class ProcessEventUtilTest {

	static final String ipString = "11.134.137.0";
	static final long ipLong = 193366272L;

	@Test
	public void ipStringToLongTest() {
		assertEquals(ipLong, ProcessEventUtil.ipToLong(ipString));
	}

	@Test
	public void ipLongToStringTest() {
		assertEquals(ipString, ProcessEventUtil.longToIp(ipLong));
	}

	@Test
	public void ipToNetBlockTest() {
		assertEquals(ipString.substring(0, ipString.lastIndexOf(".")), ProcessEventUtil.ipToNetblock28(ipString));
	}

}
