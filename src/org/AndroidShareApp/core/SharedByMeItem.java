package org.AndroidShareApp.core;

import java.io.File;
import java.util.ArrayList;

public class SharedByMeItem {

	private String mSharedPath;
	private String mFullPath;
	private ArrayList<SharedPerson> mPerson;
	private boolean mIsActive;
	private long mFileSize;

	public SharedByMeItem(String fullPath, SharedPerson person) {
		mFullPath = fullPath;
		mPerson = new ArrayList<SharedPerson>();
		mPerson.add(person);
		mSharedPath = getSharedPath(fullPath);
		mIsActive = true;
		mFileSize = new File(mFullPath).length();
	}

	public SharedByMeItem(String fullPath) {
		mFullPath = fullPath;
		mSharedPath = getSharedPath(fullPath);
		mPerson = new ArrayList<SharedPerson>();
		mFileSize = new File(mFullPath).length();
	}

	private static String getSharedPath(String fullPath) {
		File f = new File(fullPath);
		if (!f.isDirectory()) {
			return fullPath.substring(fullPath.lastIndexOf('/'));
		} else {
			return fullPath.substring(
					fullPath.substring(0, 
							fullPath.length() - 1).lastIndexOf('/'));
		}
	}

	public SharedByMeItem() {
		mPerson = new ArrayList<SharedPerson>();
	}

	public String getSharedPath() {
		return mSharedPath;
	}

	public String getFullPath() {
		return mFullPath;
	}

	public void setFullPath(String fullPath) {
		mFullPath = fullPath;
		mSharedPath = getSharedPath(fullPath);
		mFileSize = new File(mFullPath).length();
	}

	public ArrayList<SharedPerson> getSharedPersonList() {
		return mPerson;
	}

	public boolean isActive() {
		return mIsActive;
	}

	public void setActive(boolean isActive) {
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
	
	public long getFileSize(){
		return mFileSize;
	}
}
