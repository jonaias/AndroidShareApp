package org.AndroidShareApp.core;

import java.util.ArrayList;
import java.util.Random;

public class Person {

	private String mName;
	private String mDeviceId;
	private ArrayList<SharedWithMeItem> mSharedItems;
	private boolean mSharesWithMe;
	private int mTimeoutLeft;
	private static final int mMaxTimeoutLeft = 6;

	public Person(String name,String deviceId, boolean sharesWithme) {
		mName = name;
		mDeviceId = deviceId;
		setSharesWithMe(sharesWithme);
		
		mSharedItems = new ArrayList<SharedWithMeItem>();
		resetTimeoutLeft();
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
	
	public void decTimeoutLeft(){
		if((--mTimeoutLeft)==0){
			NetworkManager.getInstance().deletePerson(this);
		}
	}

	public void resetTimeoutLeft(){
		mTimeoutLeft = mMaxTimeoutLeft;
	}
}
