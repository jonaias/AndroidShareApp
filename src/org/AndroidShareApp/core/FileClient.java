package org.AndroidShareApp.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileClient implements Runnable {

    private String mDeviceID;
	private String mPath;
    private String mDestination;
	private Socket mSocket;
	private int mSize;
	private Double mCurrentProgress; // TODO: Trocar por callback.

	public FileClient(String deviceID, String path, String destination, int size, Socket socket) {
        mDeviceID = deviceID;
		mPath = path;
		mSize = size;
		mSocket = socket;
		mCurrentProgress = 0.0;
        mDestination = destination;
	}

	@Override
	public void run() {
		try {

            OutputStream sout = mSocket.getOutputStream();
            String s = mDeviceID + " " + mPath + "\n";
            System.out.println("[FileClient] s = " + s);
            sout.write(s.getBytes(), 0, s.getBytes().length);

			int BLOCK_SIZE = NetworkProtocol.BLOCK_SIZE;

			File file = new File(mDestination);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream out = new BufferedOutputStream(fos);

			InputStream in = mSocket.getInputStream();
			int nPackets = (int) Math.ceil(((double) mSize)
					/ ((double) BLOCK_SIZE));
            
            System.out.println("[FileServerThread] nPackets = " + nPackets);

			mCurrentProgress = 0.0;
			int bytesReceived;
			byte[] received = new byte[mSize];

			for (int i = 0; i < nPackets; i++) {
                System.out.println("[Client] i=" + i);
                int sizeToReceive = (mSize - i * BLOCK_SIZE >= BLOCK_SIZE) ? BLOCK_SIZE
						: mSize - i * BLOCK_SIZE;

				bytesReceived = in.read(received, i * BLOCK_SIZE, sizeToReceive);
				synchronized (mCurrentProgress) {
					mCurrentProgress += ((double) bytesReceived) / ((double) mSize);
                    System.out.println("FileServerThread: Progress = " + mCurrentProgress);
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
	}
}
