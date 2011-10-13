package org.droidShare.core;

public class SharedWithMeItem {
	
	private String mSharedPath;
	private boolean mRead;
	private boolean mWrite;
	
	public SharedWithMeItem (String sharedPath, boolean read, boolean write) {
		mSharedPath = sharedPath;
		mRead = read;
		mWrite = write;
	}
	
	public String getSharedPath () {
		return mSharedPath;
	}
	
	public boolean canRead () {
		return mRead;
	}
	
	public boolean canWrite () {
		return mWrite;
	}

}
