package org.AndroidShareApp.core;

public abstract class NetworkProtocol {
	public static final int MESSAGE_LIVE_ANNOUNCEMENT 		= 1;
	public static final int MESSAGE_LEAVING_ANNOUNCEMENT 	= 2;
	public static final int MESSAGE_SHARING_NOTIFICATION 	= 3;
	public static final int MESSAGE_SHARING_DETAILS_REQUEST = 4;
	public static final int MESSAGE_SHARING_DETAILS_ANSWER 	= 5;
	public static final int MESSAGE_SHARING_DETAILS_DENIED 	= 6;
	public static final int MESSAGE_DOWNLOAD_REQUEST 		= 7;
	public static final int MESSAGE_DOWNLOAD_ACCEPT 		= 8;
	public static final int MESSAGE_DOWNLOAD_DENY 			= 9;
	public static final int MESSAGE_UPLOAD_REQUEST 			= 10;
	public static final int MESSAGE_UPLOAD_ACCEPT 			= 11;
	public static final int MESSAGE_UPLOAD_DENY 			= 12;
	
	public static final int BUFFER_SIZE						= 128 * 1024;
}
