package org.AndroidShareApp.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;

public class NetworkSender extends Thread {
	Handler mHandler;
	
	public NetworkSender(){
		mHandler = new Handler();
	}
	
	@Override
	public void run() {
		/* Executes it every 5 s */
		mHandler.postDelayed(this, 5000);
		
		DatagramSocket socket;
		try {
			byte[] message = new byte[100];
			message = "Oh Hai!".getBytes();
			
			socket = new DatagramSocket();
			socket.setBroadcast(true);
			DatagramPacket packet = new DatagramPacket(message,100 ,
					InetAddress.getByName("127.0.0.1"), 9764);
			socket.send(packet);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
