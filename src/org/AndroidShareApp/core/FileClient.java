package org.AndroidShareApp.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileClient implements Runnable {

	private String mPath;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress; // TODO: Trocar por callback.

	public FileClient(String path, int size, Socket socket) {
		mPath = path;
		mSize = size;
		mSocket = socket;
		mCurrentProgress = 0.0;
	}

	@Override
	public void run() {
		try {

			int BLOCK_SIZE = NetworkProtocol.BLOCK_SIZE;

			File file = new File(mPath);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream out = new BufferedOutputStream(fos);

			InputStream in = mSocket.getInputStream();
			int nPackets = (int) Math.ceil(((double) mSize)
					/ ((double) BLOCK_SIZE));

			mCurrentProgress = 0.0;
			int bytesReceived;
			byte[] received = new byte[mSize];

			for (int i = 0; i < nPackets; i++) {
				bytesReceived = in.read(received, i * BLOCK_SIZE, BLOCK_SIZE);
				synchronized (mCurrentProgress) {
					mCurrentProgress += bytesReceived / mSize;
				}
			}

			out.write(received, 0, mSize);
			out.flush();

			out.close();
			in.close();
			mSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
