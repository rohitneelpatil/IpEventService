package com.lookout.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author rohit.patil
 *
 */

/*
 * Model Class to map applications from incoming post events
 * 
 */
public class Application {

	String appId;
	private final HashMap<String, Set<String>> ipMap = new HashMap<String, Set<String>>();
	private final List<String> allIps = new ArrayList<String>();
	int maxSubnetIpLength;
	String maxSubnet;

	public Application(String appId, String maxSubnet, int maxSubnetIpLength) {
		this.appId = appId;
		this.maxSubnetIpLength = maxSubnetIpLength;
		this.maxSubnet = maxSubnet;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public HashMap<String, Set<String>> getIpMap() {
		return ipMap;
	}

	public List<String> getAllIps() {
		return allIps;
	}

	public int getMaxSubnetIpLength() {
		return maxSubnetIpLength;
	}

	public void setMaxSubnetIpLength(int maxSubnetIpLength) {
		this.maxSubnetIpLength = maxSubnetIpLength;
	}

	public String getMaxSubnet() {
		return maxSubnet;
	}

	public void setMaxSubnet(String maxSubnet) {
		this.maxSubnet = maxSubnet;
	}

	public String toString() {
		return "Application [appId=" + appId + ", ipMap=" + ipMap + ", allIps=" + allIps + ", maxSubnetIpLength="
				+ maxSubnetIpLength + ", maxSubnet=" + maxSubnet + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allIps == null) ? 0 : allIps.hashCode());
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result + ((ipMap == null) ? 0 : ipMap.hashCode());
		result = prime * result + ((maxSubnet == null) ? 0 : maxSubnet.hashCode());
		result = prime * result + maxSubnetIpLength;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Application other = (Application) obj;
		if (allIps == null) {
			if (other.allIps != null)
				return false;
		} else if (!allIps.equals(other.allIps))
			return false;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		if (ipMap == null) {
			if (other.ipMap != null)
				return false;
		} else if (!ipMap.equals(other.ipMap))
			return false;
		if (maxSubnet == null) {
			if (other.maxSubnet != null)
				return false;
		} else if (!maxSubnet.equals(other.maxSubnet))
			return false;
		if (maxSubnetIpLength != other.maxSubnetIpLength)
			return false;
		return true;
	}

}
