package org.AndroidShareApp.core;

import java.util.ArrayList;
import java.util.Iterator;

public class Person implements Comparable<Person> {

	private String mName;
	private String mDeviceId;
	private ArrayList<SharedWithMeItem> mSharedItems;
	private boolean mSharesWithMe;
	public int mTimeoutLeft;
	private static final int mMaxTimeoutLeft = 6;
	private String mIP;
	private String mCurrentPath;

	public Person(String name, String deviceId, String IP) {
		mName = name;
		mDeviceId = deviceId;
		/* At creation, person doesnt have any shares */
		setSharesWithMe(false);
		
		mSharedItems = new ArrayList<SharedWithMeItem>();
		resetTimeoutLeft();
		setCurrentPath("/");
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
		ArrayList<SharedWithMeItem> tempSharedWithMeItems = new ArrayList<SharedWithMeItem>();
		
		Iterator<SharedWithMeItem> itr = mSharedItems.iterator();
	    while (itr.hasNext()) {
	    	  SharedWithMeItem tempItem = itr.next();
		      /* If this person is Everybody, skip it */			      
		      if(tempItem.getSharedPath().startsWith(mCurrentPath)){
		    	 tempSharedWithMeItems.add(tempItem);
		      }
	    }
		
		return tempSharedWithMeItems;
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

	public String getCurrentPath() {
		return mCurrentPath;
	}

	public void setCurrentPath(String currentPath) {
		mCurrentPath = currentPath;
	}
}
