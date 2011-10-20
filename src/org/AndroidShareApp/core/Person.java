package org.AndroidShareApp.core;

public class Person {

	private String mName;
	private String mDeviceId;

	public Person(String name) {
		this.mName = name;
		this.mDeviceId = "0"; //TODO: Find the device ID.
	}

	public String getName() {
		return mName;
	}
	
	@Override
	public String toString() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getDeviceID() {
		return mDeviceId;
	}

}
