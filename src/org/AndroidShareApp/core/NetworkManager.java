package org.AndroidShareApp.core;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class NetworkManager {

	private static NetworkManager mSingleton = null;
	private ArrayList<Person> mPersonList;
	private ArrayList<FileTransferrer> mCurrentTransfers;
	private ArrayList<SharedByMeItem> mSharedByMeItems;
	private String mThisDeviceId;
	private String mThisDevideName;
	private InetAddress mHostAddress;
	private InetAddress mBroadcastAddress;
	private NetworkSender mNetworkSender;
	private NetworkListener mNetworkListener;
	private FileServer mFileServer;

	private NetworkManager() {
		mPersonList = new ArrayList<Person>();
		mCurrentTransfers = new ArrayList<FileTransferrer>();
		mSharedByMeItems = new ArrayList<SharedByMeItem>();

		SharedPerson everybody = new SharedPerson("Everybody",
				String.valueOf(new Random().nextInt()), null, false, false);
		addPerson(everybody);

		/* TODO: REMOVE!!!!!!!!!!!!! */
		addPerson(new Person("teste", "123121", null));
		addPerson(new Person("teste1", "1231211", null));
		Person teste2 = new Person("teste2", "1231212", null);
		SharedWithMeItem sharedWithMeItem1 = new SharedWithMeItem(
				"/Documents/", true, false);
		SharedWithMeItem sharedWithMeItem2 = new SharedWithMeItem(
				"/cool music.mp3", true, true);
		SharedWithMeItem sharedWithMeItem3 = new SharedWithMeItem(
				"/picture.png", true, true);
		SharedWithMeItem sharedWithMeItem4 = new SharedWithMeItem("/Movies/",
				true, true);
		teste2.addSharedwithMeItem(sharedWithMeItem1);
		teste2.addSharedwithMeItem(sharedWithMeItem2);
		teste2.addSharedwithMeItem(sharedWithMeItem3);
		teste2.addSharedwithMeItem(sharedWithMeItem4);
		addPerson(teste2);
		/* ########################### */

		mThisDeviceId = android.provider.Settings.Secure.ANDROID_ID;
		mThisDevideName = android.os.Build.USER.concat("-"
				+ android.os.Build.MODEL);

		mNetworkSender = new NetworkSender(
				NetworkProtocol.BROADCAST_SEND_PORT);
		mNetworkSender.start();
		mNetworkListener = new NetworkListener(
				NetworkProtocol.BROADCAST_RECEIVE_PORT);
		mNetworkListener.start();
		mFileServer = new FileServer(NetworkProtocol.FILE_PORT);
		mFileServer.start();

		/* TODO: REMOVE!!!!!!!!!!!!! */
		addPerson(new Person("teste", "123121", null));
		addPerson(new Person("teste1", "1231211", null));
		addPerson(new Person("teste2", "1231212", null));

		SharedPerson paul = new SharedPerson("Paul", "PaulID", null, true,
				false);
		SharedPerson john = new SharedPerson("John", "JohnID", null, false,
				false);
		SharedByMeItem s1 = new SharedByMeItem("/path/to/nothing");
		s1.getSharedPersonList().add(paul);
		s1.getSharedPersonList().add(john);
		mSharedByMeItems.add(s1);
		/* ########################### */
	}

	public static synchronized NetworkManager getInstance() {
		if (mSingleton == null)
			mSingleton = new NetworkManager();

		return mSingleton;
	}

	public void addNewSharedByMe(SharedByMeItem newItem) {
		mSharedByMeItems.add(newItem);
	}

	public ArrayList<SharedByMeItem> getSharedByMeItems() {
		return mSharedByMeItems;
	}

	public ArrayList<Person> getPersonList() {
		return mPersonList;
	}

	public void addPerson(Person person) {
		synchronized (mPersonList) {
			/* If person exists, delete it */
			deletePerson(person);
			/*
			 * Add the new person, the last person(Everybody) will never be
			 * displayed
			 */
			mPersonList.add(0, person);
		}
	}

	/* If person device ID does not exists, does nothing */
	public void deletePerson(Person person) {
		synchronized (mPersonList) {
			Iterator<Person> itr = mPersonList.iterator();
			while (itr.hasNext()) {
				Person tempPerson = itr.next();
				if (tempPerson.getDeviceID().compareTo(person.getDeviceID()) == 0) {
					itr.remove();
				}
			}
		}
	}

	public void addNewTransfer(FileTransferrer newTransfer) {
		mCurrentTransfers.add(newTransfer);
	}

	public void deleteTransfer(FileTransferrer item) {
		mCurrentTransfers.remove(item);
	}

	public ArrayList<FileTransferrer> getTransfers() {
		return mCurrentTransfers;
	}

	public String getThisDeviceId() {
		return mThisDeviceId;
	}

	public String getThisDeviceName() {
		return mThisDevideName;
	}

	public NetworkSender getNetworkSender() {
		return mNetworkSender;
	}

	public NetworkListener getNetworkListener() {
		return mNetworkListener;
	}

	public FileServer getFileServer() {
		return mFileServer;
	}

	public InetAddress getHostAddress() {
		return mHostAddress;
	}

	public void setHostAddress(InetAddress hostAddress) {
		mHostAddress = hostAddress;
	}

	public InetAddress getBroadcastAddress() {
		return mBroadcastAddress;
	}

	public void setBroadcastAddress(InetAddress broadcastAddress) {
		mBroadcastAddress = broadcastAddress;
	}
}
