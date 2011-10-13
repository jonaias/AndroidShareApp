package org.droidShare.core;

public class SharedPerson extends Person {

	private boolean mRead;
	private boolean mWrite;
	
	public SharedPerson (String name) {
		super(name);
	}
	
	public void setPermissions (boolean read, boolean write) {
		mRead = read;
		mWrite = write;
	}
	
	public boolean canRead () {
		return mRead;
	}
	
	public boolean canWrite () {
		return mWrite;
	}
}
