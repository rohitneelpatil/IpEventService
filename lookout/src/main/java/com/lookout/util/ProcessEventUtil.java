package com.lookout.util;
/**
 * 
 * @author rohit.patil
 *
 */

/*
 *  Helper Functions for Processing Requests
 * 
 */
public class ProcessEventUtil {

	/**
	 * @param long value of ip
	 * @return ipAddress String
	 */
	public static String longToIp(long ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}

	/**
	 * @param ipAddress  String
	 * @return long value of ipAddress
	 */
	public static long ipToLong(String ipAddress) {

		String[] ipAddressInArray = ipAddress.split("\\.");

		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {
			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);

		}
		return result;
	}

	/**
	 * @param ipAddress String
	 * @return ipAddress/28 cidr netblock String
	 */
	public static String ipToNetblock28(String ipAddress) {
		return ipAddress.substring(0, ipAddress.lastIndexOf("."));
	}

}