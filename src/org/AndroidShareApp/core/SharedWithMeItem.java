package org.AndroidShareApp.core;

public class SharedWithMeItem implements Comparable<SharedWithMeItem> {
	
	private String mSharedPath;
	private boolean mRead;
	private boolean mWrite;
	private long mSize;
	
	public SharedWithMeItem (String sharedPath, boolean read, boolean write, long size) {
		mSharedPath = sharedPath;
		mRead = read;
		mWrite = write;
		mSize = size;
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
	
	public boolean isPath(){
		return mSharedPath.matches("\\p{ASCII}*/");
	}
	
	public String typeString(){
		if (isPath()){
			return "folder";
		}
		else{
			return "file";
		}
	}
	
	public long getFileSize(){
		return mSize;
	}
	
	@Override
	public int compareTo(SharedWithMeItem other) {
		return this.mSharedPath.compareTo(other.mSharedPath);
	}

}
