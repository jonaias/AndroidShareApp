package org.AndroidShareApp.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkSender extends Thread {
	
	public NetworkSender(){
	}
	
	@Override
	public void run() {
		DatagramSocket mSocket = null;
		DatagramPacket mPacket = null;
		
		JSONObject jsonObjetct = new JSONObject();
		try {
			jsonObjetct.put("messageType", NetworkProtocol.MESSAGE_LIVE_ANNOUNCEMENT);
			jsonObjetct.put("nome", NetworkManager.getInstance().getThisDeviceName());
			jsonObjetct.put("deviceId", NetworkManager.getInstance().getThisDeviceId());
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		byte [] message = jsonObjetct.toString().getBytes();
		
		try {
			
			mSocket = new DatagramSocket(9763, InetAddress.getByName("10.0.2.15"));
			mSocket.setBroadcast(true);
			mPacket = new DatagramPacket(message, message.length ,
					InetAddress.getByName("10.0.2.2"), 9764);
			
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		/* Executes it every 5 s */
		while(!isInterrupted()){
			try {
				sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				
				mSocket.send(mPacket);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
