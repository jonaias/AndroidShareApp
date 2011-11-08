package org.AndroidShareApp.core;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileTransferrer extends Thread {
	private int mType;
	private String mTransferPath;
	private Double mCurrentProgress;
	private String mIP;
	private int mSize;

	public FileTransferrer(int type, String IP, String transferPath) {
		mType = type;
		mTransferPath = transferPath;
		mIP = IP;
		mCurrentProgress = 0.0;
	}

	public FileTransferrer(int type, String IP, String transferPath, int size) {
		mType = type;
		mTransferPath = transferPath;
		mIP = IP;
		mCurrentProgress = 0.0;
		mSize = size;
	}

	public int getType() {
		return mType;
	}

	public String getTransferPath() {
		return mTransferPath;
	}

	public double getCurrentProgress() {
		double ret;
		synchronized (mCurrentProgress) {
			ret = mCurrentProgress;
		}
		return ret;
	}

	@Override
	public void run() {

		if (mType == TYPE_UPLOAD) {
			/* We are the server. */
			try {
				ServerSocket serverSocket = new ServerSocket(13562);
				Socket socket = serverSocket.accept();

				/* First, we read the file. */
				File currentFile = new File(mTransferPath);
				mSize = (int) currentFile.length();
				byte[] bytesToSend = new byte[mSize];

				FileInputStream fis = new FileInputStream(currentFile);
				BufferedInputStream in = new BufferedInputStream(fis);

				in.read(bytesToSend, 0, bytesToSend.length);
				in.close();

				/* Then, we send it, BLOCK_SIZE per BLOCK_SIZE. */
				OutputStream out = socket.getOutputStream();
				mCurrentProgress = 0.0;
				for (int i = 0; i < mSize; i += BLOCK_SIZE) {
					int sizeToSend = (mSize - i * BLOCK_SIZE >= BLOCK_SIZE) ? BLOCK_SIZE
							: mSize - i * BLOCK_SIZE;

					out.write(bytesToSend, i*BLOCK_SIZE, sizeToSend);
					out.flush();
					synchronized (mCurrentProgress) {
						mCurrentProgress += ((double) BLOCK_SIZE)
								/ ((double) mSize);
					}
				}

				out.close();
				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			/* We are downloading something. We are the client. */
			try {
				Socket socket = new Socket(mIP, 13562);

				InputStream in = socket.getInputStream();
				FileOutputStream fos = new FileOutputStream(mTransferPath);
				BufferedOutputStream out = new BufferedOutputStream(fos);

				int nPackets = (int) Math.ceil(((double) mSize)
						/ ((double) BLOCK_SIZE));

				mCurrentProgress = 0.0;
				int bytesReceived;
				byte[] received = new byte[mSize];

				for (int i = 0; i < nPackets; i++) {
					bytesReceived = in.read(received, i * BLOCK_SIZE,
							BLOCK_SIZE);
					synchronized (mCurrentProgress) {
						mCurrentProgress += bytesReceived / mSize;
					}
				}

				out.write(received, 0, mSize);
				out.flush();

				out.close();
				in.close();
				socket.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static final int TYPE_DOWNLOAD = 0;
	public static final int TYPE_UPLOAD = 1;
	private static final int BLOCK_SIZE = 1024;

}
