package org.AndroidShareApp.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class FileServerThread implements Runnable {

	private String mPath;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress; /*TODO: Trocar por um callback!!!*/

	public FileServerThread(String transfer, Socket socket) {
		mPath = transfer.substring(transfer.indexOf(' ') + 1);
        System.out.println("[FileServerThread] mPath = " + mPath);
		mSocket = socket;
		mCurrentProgress = 0.0;
	}

	@Override
	public void run() {
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

			int nPackets = (int) Math.ceil(((double) mSize)
					/ ((double) BLOCK_SIZE));

            System.out.println("[FileServerThread] nPackets = " + nPackets);

			for (int i = 0; i<nPackets; i++) {
                System.out.println("[Server] i=" + i);
				int sizeToSend = (mSize - i * BLOCK_SIZE >= BLOCK_SIZE) ? BLOCK_SIZE
						: mSize - i * BLOCK_SIZE;

				out.write(bytesToSend, i * BLOCK_SIZE, sizeToSend);
				out.flush();
				synchronized (mCurrentProgress) {
					mCurrentProgress += ((double) BLOCK_SIZE)
							/ ((double) mSize);
                    System.out.println("FileServerThread: Progress = " + mCurrentProgress);
				}
			}

			out.close();
			mSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
	}

	public double getCurrentProgress() {
		double ret;
		synchronized (mCurrentProgress) {
			ret = mCurrentProgress;
		}
		return ret;
	}
}
