package org.AndroidShareApp.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class FileServer extends Thread {

	private ArrayList<String> mPermissions;

	public FileServer() {
		mPermissions = new ArrayList<String>();
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(9876);
			Socket socket;

			while (!isInterrupted()) {
				socket = serverSocket.accept();

				InputStream is = socket.getInputStream();
				Scanner in = new Scanner(is);

				String currentLine = in.nextLine();
				synchronized (mPermissions) {
					if (mPermissions.contains(currentLine)) {
						
						/*XXX: Será que ele pega a string pelo conteúdo
						 * ou pelo ponteiro para o objeto????? */
						
						Thread t = new Thread(new FileServerThread(currentLine,
								socket));
						mPermissions.remove(currentLine);
						t.start();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPermission(String deviceID, String path) {
		synchronized (mPermissions) {
			mPermissions.add(deviceID + " " + path);
		}
	}
}
