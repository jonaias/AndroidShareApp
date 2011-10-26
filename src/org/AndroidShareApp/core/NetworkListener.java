package org.AndroidShareApp.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkListener extends Thread {
	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(32767);

			Socket incoming = listener.accept();

			try {
				InputStream input = incoming.getInputStream();
				Scanner in = new Scanner(input);

				boolean done = false;
				String currJSON;
				while (!done && in.hasNextLine()) {
					currJSON = in.nextLine();
					parseJSON(currJSON);
				}

			} finally {
				incoming.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseJSON(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			int messageType = obj.getInt("messageType");

			switch (messageType) {
			case (NetworkProtocol.MESSAGE_LIVE_ANNOUNCEMENT):
				/* In this case, either a person has entered the network or
				 * a person who already is in the network is announcing
				 * that he/she is active. */
				
				break;
			case (NetworkProtocol.MESSAGE_LEAVING_ANNOUNCEMENT):
				break;
			case (NetworkProtocol.MESSAGE_SHARING_NOTIFICATION):
				break;
			case (NetworkProtocol.MESSAGE_SHARING_DETAILS_REQUEST):
				break;
			case (NetworkProtocol.MESSAGE_SHARING_DETAILS_SEND):
				break;
			case (NetworkProtocol.MESSAGE_DOWNLOAD_REQUEST):
				break;
			case (NetworkProtocol.MESSAGE_DOWNLOAD_ACCEPT):
				break;
			case (NetworkProtocol.MESSAGE_DOWNLOAD_DENY):
				break;
			case (NetworkProtocol.MESSAGE_UPLOAD_REQUEST):
				break;
			case (NetworkProtocol.MESSAGE_UPLOAD_ACCEPT):
				break;
			case (NetworkProtocol.MESSAGE_UPLOAD_DENY):
				break;
			}

		} catch (JSONException e) {
			System.err.println("Malformed JSON string: \"" + json + "\"\n");
			return;
		}
	}
}
