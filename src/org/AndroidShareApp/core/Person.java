package org.AndroidShareApp.core;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class Person implements Comparable<Person> {

	private String mName;
	private String mDeviceId;
	private ArrayList<SharedWithMeItem> mSharedItems;
	private boolean mSharesWithMe;
	public int mTimeoutLeft;
	private static final int mMaxTimeoutLeft = 6;
	private InetAddress mIP;
	private String mCurrentPath;

	public Person(String name, String deviceId, InetAddress IP) {
		Log.i("Person", this.mName + ":constructor(" + name + "," + deviceId
				+ "," + IP + ")");
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

	public void addSharedWithMeItem(SharedWithMeItem item) {
		Log.i("Person",
				this.mName + ":addSharedWithMeItem(" + item.getSharedPath()
						+ "," + item.canRead() + "," + item.canWrite() + ")");
		/* If item exists, delete it */
		deleteSharedWithMeItem(item);
		mSharedItems.add(item);
	}

	/* If person device ID does not exists, does nothing */
	public void deleteSharedWithMeItem(SharedWithMeItem item) {
		Log.i("Person",
				this.mName + ":deleteSharedWithMeItem(" + item.getSharedPath()
						+ ")");
		Iterator<SharedWithMeItem> itr = mSharedItems.iterator();
		while (itr.hasNext()) {
			SharedWithMeItem tempItem = itr.next();
			if (tempItem.compareTo(item) == 0) {
				itr.remove();
			}
		}
	}

	public ArrayList<SharedWithMeItem> getSharedWithMeItems() {
		Log.i("Person", this.mName + ":getSharedWithMeItems ()");
		ArrayList<SharedWithMeItem> tempSharedWithMeItems = new ArrayList<SharedWithMeItem>();
		Iterator<SharedWithMeItem> itr = mSharedItems.iterator();
		while (itr.hasNext()) {
			SharedWithMeItem tempItem = itr.next();
			if (tempItem.getSharedPath().startsWith(mCurrentPath)) {
				String buff;
				buff = tempItem.getSharedPath();
				buff = buff.substring(mCurrentPath.length() - 1);
				if (buff.matches("[/]?[^/]*[/]?")) {
					SharedWithMeItem itemToAdd = new SharedWithMeItem(buff,
							tempItem.canRead(), tempItem.canWrite());
					tempSharedWithMeItems.add(itemToAdd);
				}
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

	public synchronized void resetTimeoutLeft() {
		mTimeoutLeft = mMaxTimeoutLeft;
	}

	@Override
	public int compareTo(Person other) {
		return this.mDeviceId.compareTo(other.mDeviceId);
	}

	public InetAddress getIP() {
		return mIP;
	}

	public void setIP(InetAddress IP) {
		mIP = IP;
	}

	public String getCurrentPath() {
		return mCurrentPath;
	}

	public void setCurrentPath(String currentPath) {
		mCurrentPath = currentPath;
		if (mCurrentPath.compareTo("/") != 0) {
			mSharedItems.add(new SharedWithMeItem(mCurrentPath, true, true));
		}
	}
}
