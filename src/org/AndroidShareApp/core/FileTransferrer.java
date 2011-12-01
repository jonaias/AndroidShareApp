package org.AndroidShareApp.core;

public interface FileTransferrer {
	
	public double getProgress ();
	
	public void interrupt();

	public boolean isAlive();
	
	public Person getPerson();
	
	public String getPath();
	
	public int getType ();
	
	public static final int TYPE_DOWNLOAD = 0;
	public static final int TYPE_UPLOAD = 1;
}
