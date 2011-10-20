package org.AndroidShareApp.core;

import java.util.ArrayList;

public class Person {

	private String mName;
	private String mDeviceId;
	private ArrayList<SharedWithMeItem> mSharedItems;

	public Person(String name) {
		mName = name;
		mDeviceId = "0"; //TODO: Find the device ID.
		mSharedItems = new ArrayList<SharedWithMeItem>();
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
	
	public void addSharedwithMeItem(SharedWithMeItem item) {
		mSharedItems.add(item);
	}
	
	public ArrayList<SharedWithMeItem> getSharedWithMeItems () {
		return mSharedItems;
	}

}
