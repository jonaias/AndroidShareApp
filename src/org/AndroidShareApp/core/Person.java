package org.AndroidShareApp.core;

import java.util.ArrayList;

public class Person implements Comparable<Person> {

	private String mName;
	private String mDeviceId;
	private ArrayList<SharedWithMeItem> mSharedItems;
	private boolean mSharesWithMe;
	public int mTimeoutLeft;
	private static final int mMaxTimeoutLeft = 6;
	private String mIP;

	public Person(String name, String deviceId, String IP) {
		mName = name;
		mDeviceId = deviceId;
		/* At creation, person doesnt have any shares */
		setSharesWithMe(false);
		
		mSharedItems = new ArrayList<SharedWithMeItem>();
		resetTimeoutLeft();
		
		setIP(IP);
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

	public void setSharesWithMe(boolean sharesWithMe) {
		mSharesWithMe = sharesWithMe;
	}

	public boolean sharesWithMe() {
		return mSharesWithMe;
	}

	public synchronized void resetTimeoutLeft(){
		mTimeoutLeft = mMaxTimeoutLeft;
	}

	@Override
	public int compareTo(Person other) {
		return this.mName.compareTo(other.mName);
	}

	public String getIP() {
		return mIP;
	}

	public void setIP(String IP) {
		mIP = IP;
	}
}
