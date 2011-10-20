package org.AndroidShareApp.core;

import java.util.ArrayList;

public class SharedByMeItem {

	private String mSharedPath;
	private ArrayList<SharedPerson> mPerson;

	public SharedByMeItem(String sharedPath, SharedPerson person) {
		mSharedPath = sharedPath;
		mPerson = new ArrayList<SharedPerson>();
		mPerson.add(person);
	}
	
	public SharedByMeItem(String sharedPath) {
		mSharedPath = sharedPath;
		mPerson = new ArrayList<SharedPerson>();
	}

	public String getSharedPath() {
		return mSharedPath;
	}

	public void setSharedPath(String SharedPath) {
		this.mSharedPath = SharedPath;
	}

	public ArrayList<SharedPerson> getSharedPersonList() {
		return mPerson;
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
