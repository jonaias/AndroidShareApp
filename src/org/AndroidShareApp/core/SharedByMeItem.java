package org.AndroidShareApp.core;

import java.util.ArrayList;

public class SharedByMeItem {

	private String mSharedPath;
	private String mFullPath;
	private ArrayList<SharedPerson> mPerson;
	private boolean mIsActive;

	public SharedByMeItem(String fullPath, SharedPerson person) {
		mFullPath = fullPath;
		mPerson = new ArrayList<SharedPerson>();
		mPerson.add(person);
		/* TODO: Pegar o sharedPath a partir do fullPath. */
		mIsActive = false;
	}
	
	public SharedByMeItem(String sharedPath) {
		mSharedPath = sharedPath;
		mPerson = new ArrayList<SharedPerson>();
	}
	
	public SharedByMeItem () {
		mPerson = new ArrayList<SharedPerson>();
	}

	public String getSharedPath() {
		return mSharedPath;
	}

	public void setSharedPath(String SharedPath) {
		this.mSharedPath = SharedPath;
	}

	public String getFullPath() {
		return mFullPath;
	}

	public void setFullPath(String fullPath) {
		this.mFullPath = fullPath;
	}
	
	public ArrayList<SharedPerson> getSharedPersonList() {
		return mPerson;
	}
	
	public boolean isActive () {
		return mIsActive;
	}
	
	public void setActive (boolean isActive) {
		mIsActive = isActive;
	}

	public void managePerson(SharedPerson person) {
		if (mPerson.contains(person)) {
			if ((!person.canRead()) && (!person.canWrite()))
				mPerson.remove(person);
			else {
				mPerson.get(mPerson.indexOf(person)).setPermissions(
						person.canRead(), person.canWrite());
			}
		} else {
			mPerson.add(person);
		}
	}
}
