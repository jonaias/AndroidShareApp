package org.AndroidShareApp.core;

public class SharedPerson extends Person {

	private boolean mRead;
	private boolean mWrite;
	
	public SharedPerson (String name) {
		super(name);
	}
	
	public SharedPerson (String name, boolean read, boolean write) {
		super(name);
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
