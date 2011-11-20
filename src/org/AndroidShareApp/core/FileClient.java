package org.AndroidShareApp.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class FileClient extends Thread {

	private String mDeviceID;
	private String mPath;
	private String mDestination;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress;

	public FileClient(String deviceID, String path, String destination,
			int size, Socket socket) {
		mDeviceID = deviceID;
		mPath = path;
		mSize = size;
		mSocket = socket;
		mDestination = destination;

		Log.i("FileClient", "Created transfer with path \"" + mPath
				+ "\" on socket \"" + mSocket + "\" to destination \""
				+ mDestination + "\".");
	}

	@Override
	public void run() {
		mCurrentProgress = 0.0;
		try {

			OutputStream sout = mSocket.getOutputStream();
			String s = mDeviceID + " " + mPath + "\n";
			sout.write(s.getBytes(), 0, s.getBytes().length);

			int BLOCK_SIZE = NetworkProtocol.BLOCK_SIZE;

			File file = new File(mDestination);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream out = new BufferedOutputStream(fos);

			InputStream in = mSocket.getInputStream();
			int nPackets = (int) Math.ceil(((double) mSize)
					/ ((double) BLOCK_SIZE));

			int bytesReceived;
			byte[] received = new byte[mSize];

			Log.i("FileClient", "Started transfer on socket \"" + mSocket
					+ "\".");

			for (int i = 0; i < nPackets; i++) {
				int sizeToReceive = (mSize - i * BLOCK_SIZE >= BLOCK_SIZE) ? BLOCK_SIZE
						: mSize - i * BLOCK_SIZE;

				bytesReceived = in
						.read(received, i * BLOCK_SIZE, sizeToReceive);
				
				synchronized (mCurrentProgress) {
					mCurrentProgress += ((double) bytesReceived) / ((double) mSize);
				}
			}

			out.write(received, 0, mSize);
			out.flush();

			sout.close();
			out.close();
			in.close();
			mSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.i("FileClient", "Ended transfer on socket \"" + mSocket + "\".");
	}
	
	public String getPath () {
		return mPath;
	}

	public double getProgress() {
		double ret;
		synchronized (mCurrentProgress) {
			ret = mCurrentProgress;
		}
		return ret;
	}
}
