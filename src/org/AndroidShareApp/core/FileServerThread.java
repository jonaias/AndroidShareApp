package org.AndroidShareApp.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class FileServerThread implements Runnable {

	private String mPath;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress; /*TODO: Trocar por um callback!!!*/

	public FileServerThread(String transfer, Socket socket) {
		mPath = transfer.substring(transfer.indexOf(' ') + 1);
		mSocket = socket;
		Log.i("FileServerThread", "Added transfer with path \"" + mPath
				+ "\" on socket " + mSocket);
		mCurrentProgress = 0.0;
	}

	@Override
	public void run() {
		Log.i("FileServerThread", "Started transfer on socket" + mSocket);

		try {
			int BLOCK_SIZE = NetworkProtocol.BLOCK_SIZE;
			
			/* First, we read the file. */
			File currentFile = new File(mPath);
			mSize = (int) currentFile.length();
			byte[] bytesToSend = new byte[mSize];
			
			FileInputStream fis = new FileInputStream(currentFile);
			BufferedInputStream in = new BufferedInputStream(fis);

			in.read(bytesToSend, 0, bytesToSend.length);
			in.close();

			/* Then, we send it, BLOCK_SIZE per BLOCK_SIZE. */
			OutputStream out = mSocket.getOutputStream();
			mCurrentProgress = 0.0;

			for (int i = 0; i < mSize; i += BLOCK_SIZE) {
				int sizeToSend = (mSize - i * BLOCK_SIZE >= BLOCK_SIZE) ? BLOCK_SIZE
						: mSize - i * BLOCK_SIZE;

				out.write(bytesToSend, i * BLOCK_SIZE, sizeToSend);
				out.flush();
				synchronized (mCurrentProgress) {
					mCurrentProgress += ((double) BLOCK_SIZE)
							/ ((double) mSize);
				}
			}

			out.close();
			mSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Log.i("FileServerThread", "Finished transfer on socket" + mSocket);
	}

	public double getCurrentProgress() {
		double ret;
		synchronized (mCurrentProgress) {
			ret = mCurrentProgress;
		}
		return ret;
	}
}
