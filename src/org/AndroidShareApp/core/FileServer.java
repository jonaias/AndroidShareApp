package org.AndroidShareApp.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class FileServer extends Thread {

	private ArrayList<String> mPermissions;

	public FileServer() {
		mPermissions = new ArrayList<String>();
	}

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(9876);
			//TODO: Colocar as portas no NetworkProtocol.
			Socket socket;

			while (!isInterrupted()) {
				socket = serverSocket.accept();

				InputStream is = socket.getInputStream();

                byte[] buffer = new byte[1024];
                int i=0;
                do {
                    is.read(buffer, i, 1);
                    System.out.println((char) buffer[i]);
                    i++;
                } while (buffer[i-1] != '\n');

                String currentLine = new String(buffer).substring(0,i-1);
                System.out.println("[FileServer] currentLine = \"" + currentLine + "\"");

				synchronized (mPermissions) {
					if (mPermissions.contains(currentLine)) {
					
                        System.out.println("[FileServer] Tem permissão");    
						/*XXX: Será que ele pega a string pelo conteúdo
						 * ou pelo ponteiro para o objeto????? */
						
						Thread t = new Thread(new FileServerThread(currentLine,
								socket));
						mPermissions.remove(currentLine);
						t.start();
					}
				}
				is.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addPermission(String deviceID, String path) {
		synchronized (mPermissions) {
			mPermissions.add(deviceID + " " + path);
		}
        System.out.println("Added permission " + deviceID + " " + path);
	}
}
