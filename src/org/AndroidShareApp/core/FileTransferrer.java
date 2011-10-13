package org.AndroidShareApp.core;

import java.util.ArrayList;

public class FileTransferrer {
	private int mType;
	private ArrayList<String> mTransferPaths;
	
	public FileTransferrer (ArrayList<String> transferPaths, int type) {
		mType = type;
		mTransferPaths = new ArrayList<String>(transferPaths);
	}
	
	public FileTransferrer (String transferPath, int type) {
		mType = type;
		mTransferPaths = new ArrayList<String>();
		mTransferPaths.add(transferPath);
	}
	
	public int getType() {
		return mType;
	}
	
	public ArrayList<String> getTransferPaths() {
		return mTransferPaths;
	}

	public static final int TYPE_DOWNLOAD = 0;
	public static final int TYPE_UPLOAD = 1;
	
}
