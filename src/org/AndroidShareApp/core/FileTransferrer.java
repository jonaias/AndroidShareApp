package org.AndroidShareApp.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class FileTransferrer extends Thread {
	private int mType;
	private ArrayList<String> mTransferPaths;
	private double mCurrentProgress;

	public FileTransferrer(ArrayList<String> transferPaths, int type) {
		mType = type;
		mTransferPaths = new ArrayList<String>(transferPaths);
		mCurrentProgress = 0;
	}

	public FileTransferrer(String transferPath, int type) {
		mType = type;
		mTransferPaths = new ArrayList<String>();
		mTransferPaths.add(transferPath);
		mCurrentProgress = 0;
	}

	public int getType() {
		return mType;
	}

	public ArrayList<String> getTransferPaths() {
		return mTransferPaths;
	}

	public double getCurrentProgress() {
		return mCurrentProgress;
	}

	@Override
	public void run() {
		if (mType == TYPE_UPLOAD) {
			/* We are the server. */
			try {
				ServerSocket serverSocket = new ServerSocket(13562);

				Iterator<String> itr = mTransferPaths.iterator();
				while (itr.hasNext()) {
					Socket socket = serverSocket.accept();
					String currentPath = itr.next();

					/* First, we read the file. */
					File currentFile = new File(currentPath);
					byte[] byteArray = new byte[(int) currentFile.length()];

					FileInputStream fis = new FileInputStream(currentFile);
					BufferedInputStream bis = new BufferedInputStream(fis);

					bis.read(byteArray, 0, byteArray.length);
					bis.close();

					/* Then, we send it, BLOCK_SIZE per BLOCK_SIZE. */
					OutputStream os = socket.getOutputStream();
					mCurrentProgress = 0;
					byte[] aux = new byte[BLOCK_SIZE];
					for (int i = 0; i < (int) currentFile.length(); i += BLOCK_SIZE) {
						for (int j = 0; j < BLOCK_SIZE; j++)
							aux[j] = byteArray[i + j];

						os.write(aux, 0, BLOCK_SIZE);
						os.flush();
						mCurrentProgress += ((double) BLOCK_SIZE)
								/ (currentFile.length());
					}

					os.close();
					socket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	public static final int TYPE_DOWNLOAD = 0;
	public static final int TYPE_UPLOAD = 1;
	private static final int BLOCK_SIZE = 1024;

}
