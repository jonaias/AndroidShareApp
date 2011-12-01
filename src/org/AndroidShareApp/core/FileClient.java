package org.AndroidShareApp.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Environment;
import android.util.Log;

public class FileClient extends Thread implements FileTransferrer {

	private String mDeviceID;
	private String mPath;
	private String mDestinationFolder;
	private String mDestination;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress;

	private Person mPerson;

	public FileClient(Person person, SharedWithMeItem sharedWithMeItem) {
		mSocket = null;
		mPerson = person;

		mPath = sharedWithMeItem.getSharedPath();
		mDestinationFolder = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		mDestination = mDestinationFolder + mPath;

		mSize = (int) sharedWithMeItem.getFileSize();
		mDeviceID = person.getDeviceID();

		try {
			mSocket = new Socket(person.getIP(), NetworkProtocol.FILE_PORT);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Log.i("FileClient", "Created transfer with path \"" + mPath
				+ "\" on socket \"" + mSocket + "\" to destination \""
				+ mDestinationFolder + "\".");
	}

	@Override
	public void run() {
		NetworkManager.getInstance().addTransfer(this);
		mCurrentProgress = 0.0;
		if (mSocket != null) {
			try {

				OutputStream sout = mSocket.getOutputStream();
				String s = mDeviceID + " " + mPath + "\n";
				sout.write(s.getBytes(), 0, s.getBytes().length);

				int BLOCK_SIZE = NetworkProtocol.BLOCK_SIZE;

				File file = new File(mDestination);
				int i = 0;
				while (file.exists()) {
					file = new File(mDestination + "(" + (++i) + ")");
				}

				Log.i("FileClient",
						"Created destination file " + file.getAbsolutePath());
				// Toast.makeText(, text, duration)
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream out = new BufferedOutputStream(fos);

				InputStream in = mSocket.getInputStream();
				int nPackets = (int) Math.ceil(((double) mSize)
						/ ((double) BLOCK_SIZE));

				int bytesReceived;
				byte[] received = new byte[mSize];

				Log.i("FileClient", "Started transfer on socket \"" + mSocket
						+ "\".");

				for (i = 0; i < nPackets; i++) {
					int sizeToReceive = (mSize - i * BLOCK_SIZE >= BLOCK_SIZE) ? BLOCK_SIZE
							: mSize - i * BLOCK_SIZE;

					bytesReceived = in.read(received, i * BLOCK_SIZE,
							sizeToReceive);

					synchronized (mCurrentProgress) {
						mCurrentProgress += ((double) bytesReceived)
								/ ((double) mSize);
					}
				}

				out.write(received, 0, mSize);
				out.flush();

				sout.close();
				out.close();
				in.close();
				mSocket.close();

				Log.i("FileClient", "Ended transfer on socket \"" + mSocket
						+ "\".");

			} catch (IOException e) {
				Log.i("FileClient", "IO exception on socket \"" + mSocket
						+ "\".");
				e.printStackTrace();
			}
		} else {
			Log.i("FileClient", "Cannot connect to \"" + mSocket + "\".");
		}
	}

	public String getPath() {
		return mPath;
	}

	public double getProgress() {
		double ret;
		synchronized (mCurrentProgress) {
			ret = mCurrentProgress;
		}
		return ret;
	}

	public Person getPerson() {
		return mPerson;
	}
	
	public int getType() {
		return FileTransferrer.TYPE_DOWNLOAD;
	}
	
	@Override
	public void interrupt () {
		super.interrupt();
		NetworkManager.getInstance().deleteTransfer(this);
	}
}
