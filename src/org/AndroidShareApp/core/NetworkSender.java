package org.AndroidShareApp.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;

public class NetworkSender extends Thread {
	//Handler mHandler;
	
	public NetworkSender(){
		//mHandler = new Handler();
	}
	
	@Override
	public void run() {
		/* Executes it every 5 s */
		//mHandler.postDelayed(this, 5000);
		while(!isInterrupted()){
			DatagramSocket socket;
			try {
				byte[] message = "Oh Hai!".getBytes();
				socket = new DatagramSocket(9763, InetAddress.getByName("10.0.2.15"));
				//socket.setBroadcast(true);
				DatagramPacket packet = new DatagramPacket(message, message.length ,
						InetAddress.getByName("10.0.2.2"), 9764);
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

}
