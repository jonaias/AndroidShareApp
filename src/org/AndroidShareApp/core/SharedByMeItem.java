package org.AndroidShareApp.core;

public class SharedByMeItem {
	
	private String mSharedPath;
	private SharedPerson mPerson;
	
	public SharedByMeItem (String sharedPath, SharedPerson person) {
		mSharedPath = sharedPath;
		mPerson = person;
	}

	public String getSharedPath() {
		return mSharedPath;
	}

	public void setSharedPath(String SharedPath) {
		this.mSharedPath = SharedPath;
	}

	public Person getPerson() {
		return mPerson;
	}

	public void setPerson(SharedPerson person) {
		this.mPerson = person;
	}
	
}
