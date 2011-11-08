package org.AndroidShareApp.core;

import java.net.InetAddress;

public class SharedPerson extends Person {

	private boolean mRead;
	private boolean mWrite;
	

	public SharedPerson (String name, String deviceId, InetAddress IP, boolean read, boolean write) {
		super(name, deviceId, IP);
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
	
	public void setRead (boolean canRead) {
		mRead = canRead;
	}
	
	public void setWrite (boolean canWrite) {
		mWrite = canWrite;
	}
}
