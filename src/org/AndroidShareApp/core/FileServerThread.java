package org.AndroidShareApp.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class FileServerThread extends Thread implements FileTransferrer {

	private String mPath;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress;
	private Person mPerson;

	public FileServerThread(String transfer, Socket socket) {
		mPath = transfer.substring(transfer.indexOf(' ') + 1);
		mPath = NetworkManager.getInstance().getSharedByMeItemFullPath(mPath);
		mSocket = socket;

		mPerson = NetworkManager.getInstance().getPersonByDeviceID(
				transfer.substring(0, transfer.indexOf(' ')));

		mCurrentProgress = 0.0;
		
		Log.i("FileServerThread", "Created transfer with path \"" + mPath
				+ "\" on socket \"" + mSocket + "\".");
	}

	@Override
	public void run() {
		NetworkManager.getInstance().addTransfer(this);
		try {
			int BLOCK_SIZE = NetworkProtocol.BLOCK_SIZE;

			/* First, we read the file. */
			File currentFile = new File(mPath);
			Log.i("FileServerThread",
					"Serving file  " + currentFile.getAbsolutePath() + ".");
			mSize = (int) currentFile.length();
			byte[] bytesToSend = new byte[mSize];

			FileInputStream fis = new FileInputStream(currentFile);
			BufferedInputStream in = new BufferedInputStream(fis);

			in.read(bytesToSend, 0, bytesToSend.length);
			in.close();

			/* Then, we send it, BLOCK_SIZE per BLOCK_SIZE. */
			OutputStream out = mSocket.getOutputStream();

			synchronized (mCurrentProgress) {
				mCurrentProgress = 0.0;
			}

			int nPackets = (int) Math.ceil(((double) mSize)
					/ ((double) BLOCK_SIZE));

			Log.i("FileServerThread", "Started transfer on socket \"" + mSocket
					+ "\".");

			for (int i = 0; i < nPackets; i++) {
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
			Log.i("FileServerThread", "Ended transfer on socket \"" + mSocket
					+ "\".");

		} catch (IOException e) {
			Log.i("FileServerThread", "Error on transfer on socket \""
					+ mSocket + "\".");
			e.printStackTrace();
			return;
		}
	}

	@Override
	public double getProgress() {
		return mCurrentProgress;
	}

	@Override
	public Person getPerson() {
		return mPerson;
	}

	@Override
	public String getPath() {
		return mPath;
	}
	
	public int getType() {
		return FileTransferrer.TYPE_UPLOAD;
	}
	
	@Override
	public void interrupt () {
		super.interrupt();
		NetworkManager.getInstance().deleteTransfer(this);
	}
}
