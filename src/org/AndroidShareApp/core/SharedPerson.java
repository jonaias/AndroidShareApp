package org.AndroidShareApp.core;

public class SharedPerson extends Person {

	private boolean mRead;
	private boolean mWrite;
	

	public SharedPerson (String name, String deviceId, boolean read, boolean write) {
		super(name,deviceId,false);
		setPermissions(read, write);
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
