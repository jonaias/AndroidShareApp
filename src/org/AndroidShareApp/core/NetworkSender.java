package org.AndroidShareApp.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import org.AndroidShareApp.gui.SharedWithMeListActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkSender extends Thread {
	SharedWithMeListActivity mSharedWithMeListActivity=null;
	DatagramSocket mSocket = null;
	DatagramPacket mPacket = null;
	JSONObject mJsonObject=null;
	int mPort;
	
	public NetworkSender(int port){
		mPort = port;
	}
	
	
	public void registerCallBack(SharedWithMeListActivity sharedWithMeListActivity){
			mSharedWithMeListActivity = sharedWithMeListActivity;
	}
	
	@Override
	public void run() {
		
		/* Creates broadcast message */
		try {
			mSocket = new DatagramSocket();
			mSocket.setBroadcast(true);
			
			mJsonObject = new JSONObject();
			mJsonObject.put("messageType", NetworkProtocol.MESSAGE_LIVE_ANNOUNCEMENT);
			mJsonObject.put("name", NetworkManager.getInstance().getThisDeviceName());
			mJsonObject.put("deviceId", NetworkManager.getInstance().getThisDeviceId());
			mPacket = new DatagramPacket(mJsonObject.toString().getBytes(), mJsonObject.toString().getBytes().length ,
					InetAddress.getByName("10.0.2.255"), mPort);
			
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		
		/* Every 5s:
		 * -Broadcasts live announcement 
		 * -Refresh displayed activity 
		 * -Decreases every person a timeout counter
		 * */
		while(!isInterrupted()){
			
			
			/* Broadcasts live announcement */
			try {	
				mSocket.send(mPacket);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/* Wait 5s 
			 * TODO: improve broadcast interval*/
			try {
				sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			/* Refresh displayed activity */ 
			if(mSharedWithMeListActivity!=null){
				mSharedWithMeListActivity.refreshUi();
			}
			
			
			/* Decreases every person a timeout counter */
			ArrayList<Person> personList = NetworkManager.getInstance().getPersonList();
			synchronized (personList) {
				Iterator<Person> itr = personList.iterator();
			    while (itr.hasNext()) {
			    	  Person tempPerson = itr.next();
				      /* If this person is Everybody, skip it */			      
				      if(tempPerson.getName().compareTo("Everybody")!=0){
				    	  if (--tempPerson.mTimeoutLeft<=1){
				    		  /* TODO: REMOVE comment below */
				    		  //itr.remove();
				    	  }
				      }
			    }
			}
		}
		
	}

}
