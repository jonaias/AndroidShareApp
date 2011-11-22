package org.AndroidShareApp.core;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import org.AndroidShareApp.gui.SharedWithMeListActivity;
import org.AndroidShareApp.gui.TransferActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NetworkSender extends Thread {
	private SharedWithMeListActivity mSharedWithMeListActivity = null;
	private DatagramSocket mSocket = null;
	private DatagramPacket mPacket = null;
	private JSONObject mJsonObject = null;
	private int mPort;
	private TransferActivity mTransferActivity = null;

	public NetworkSender(int port) {
		mPort = port;

		try {
			mSocket = new DatagramSocket();
			mSocket.setBroadcast(true);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setName("Network Sender");
	}

	public void registerCallBack(
			SharedWithMeListActivity sharedWithMeListActivity) {
		mSharedWithMeListActivity = sharedWithMeListActivity;
	}

	public void registerCallBack(TransferActivity transferActivity) {
		mTransferActivity = transferActivity;
	}

	public void requestDownload(Person person, ArrayList<String> itemsToDownload) {
		Log.i("NetworkSender", "requestDownload(" + person.getName() + ","
				+ itemsToDownload);
		DatagramPacket tempPacket = null;
		JSONObject tempJsonObject = null;

		Iterator<String> itr = itemsToDownload.iterator();
		while (itr.hasNext()) {
			String tempItem = itr.next();
			try {
				tempJsonObject = new JSONObject();
				tempJsonObject.put("messageType",
						NetworkProtocol.MESSAGE_DOWNLOAD_REQUEST);
				tempJsonObject.put("deviceID", NetworkManager.getInstance()
						.getThisDeviceId());
				tempJsonObject.put("path", tempItem);
				Log.i("NetworkSender",
						"Sending packet " + tempJsonObject.toString()
								+ " adress " + person.getIP().toString());
				tempPacket = new DatagramPacket(tempJsonObject.toString()
						.getBytes(),
						tempJsonObject.toString().getBytes().length,
						person.getIP(), mPort);

				sendDatagram(tempPacket);

			} catch (JSONException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		}
	}

	public void sendDatagram(DatagramPacket packet) {
		synchronized (mSocket) {
			try {
				mSocket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/* Sends share info for everybody which 
	 * this devices has shares 
	 * TODO: Send every shared once*/
	public void sendShareInfoForEverbody(){
		Iterator<SharedByMeItem> sharedByMeItemIterator = NetworkManager.getInstance().getSharedByMeItems().iterator();
		/* For each shared item */
		while (sharedByMeItemIterator.hasNext()) {
			SharedByMeItem sharedByMeItem = sharedByMeItemIterator.next();
			Iterator<SharedPerson> sharedPersonIterator = sharedByMeItem.getSharedPersonList().iterator();
			/* For each person which the item is shared */
			while (sharedPersonIterator.hasNext()){
				SharedPerson sharedPerson = sharedPersonIterator.next();
				/* If shared person has access to the path */
				if(sharedPerson.hasAnyAcess()){
					JSONObject tempJsonObject = new JSONObject();
					JSONArray sharedListJSON = new JSONArray();
					JSONObject sharedItemJSON = new JSONObject();
					/* Create and send shared item */
					try {
						tempJsonObject.put("messageType", NetworkProtocol.MESSAGE_SHARING_DETAILS_SEND);
						tempJsonObject.put("deviceID", NetworkManager.getInstance().getThisDeviceId());
						
						sharedItemJSON.put("path", sharedByMeItem.getSharedPath());
						
						sharedItemJSON.put("permissions", sharedPerson.getPermissionString());
						
						sharedItemJSON.put("size", sharedByMeItem.getFileSize());
						
						sharedListJSON.put(sharedItemJSON);
						
						tempJsonObject.accumulate("sharedList",sharedListJSON);
						
						DatagramPacket tempPacket = new DatagramPacket(tempJsonObject.toString().getBytes(), tempJsonObject.toString().getBytes().length ,
								sharedPerson.getIP(), mPort);
						sendDatagram(tempPacket);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
				
	}
	
	@Override
	public void run() {

		/* Creates broadcast message */
		try {
			mJsonObject = new JSONObject();
			mJsonObject.put("messageType",
					NetworkProtocol.MESSAGE_LIVE_ANNOUNCEMENT);
			mJsonObject.put("name", NetworkManager.getInstance()
					.getThisDeviceName());
			mJsonObject.put("deviceID", NetworkManager.getInstance()
					.getThisDeviceId());
			mPacket = new DatagramPacket(mJsonObject.toString().getBytes(),
					mJsonObject.toString().getBytes().length, NetworkManager
							.getInstance().getBroadcastAddress(), mPort);
			/* TODO:NetworkManager.getInstance().getBroadcastAddress() */

		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		/*
		 * Every 5s: -Broadcasts live announcement -Refresh displayed activity
		 * -Decreases every person a timeout counter
		 */
		while (!isInterrupted()) {

			/* Broadcasts live announcement */
			sendDatagram(mPacket);

			/*
			 * Wait 5s TODO: improve broadcast interval
			 */
			
			/* Broadcasts share info */
			sendShareInfoForEverbody();
			
			/* Wait 5s 
			 * TODO: improve broadcast interval*/
			try {
				sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/* Refresh displayed activity */
			if (mSharedWithMeListActivity != null) {
				mSharedWithMeListActivity.refreshUi();
			}

			if (mTransferActivity != null) {
				mTransferActivity.refreshUi();
			}

			/* Decreases every person a timeout counter */
			ArrayList<Person> personList = NetworkManager.getInstance()
					.getPersonList();
			synchronized (personList) {
				Iterator<Person> itr = personList.iterator();
				while (itr.hasNext()) {
					Person tempPerson = itr.next();
					/* If this person is Everybody, skip it */
					if (tempPerson.getName().compareTo("Everybody") != 0) {
						if (--tempPerson.mTimeoutLeft <= 1) {
							/* TODO: REMOVE comment below */
							// itr.remove();
						}
					}
				}
			}
		}

	}

}
