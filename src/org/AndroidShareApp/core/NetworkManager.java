package org.AndroidShareApp.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class NetworkManager {
	
	private static NetworkManager mSingleton=null;
	private ArrayList<Person> mPersonList;
	private ArrayList<FileTransferrer> mCurrentTransfers;
	private ArrayList<SharedByMeItem> mSharedByMeItems;
	private String mThisDeviceId;
	private String mThisDevideName;
	private NetworkSender mNetworkSender;
	private NetworkListener mNetworkListener;

	private NetworkManager() {
		mPersonList = new ArrayList<Person>();
		mCurrentTransfers = new ArrayList<FileTransferrer>();
		mSharedByMeItems = new ArrayList<SharedByMeItem>();
		
		SharedPerson everybody = new SharedPerson("Everybody",String.valueOf(new Random().nextInt()),false,false);
		mPersonList.add(everybody);
		
		mPersonList.add(0,new Person("teste", "123121"));
		mPersonList.add(0,new Person("teste1", "1231211"));
		mPersonList.add(0,new Person("teste2", "1231212"));
		
		
		mThisDeviceId = android.provider.Settings.Secure.ANDROID_ID;
		mThisDevideName = android.os.Build.USER.concat("-"+android.os.Build.MODEL);
		
		/* TODO: Criar NetworkSender. */
		mNetworkSender = new NetworkSender();
		mNetworkSender.start();
		mNetworkListener = new NetworkListener(9226);
		mNetworkListener.start();
		
		/* TODO: REMOVE!!!!!!!!!!!!! */
		
		SharedPerson paul = new SharedPerson("Paul", "PaulID", true, false);
		SharedPerson john = new SharedPerson("John", "JohnID", false, false);
		SharedByMeItem s1 = new SharedByMeItem("/path/to/nothing");
		s1.getSharedPersonList().add(paul);
		s1.getSharedPersonList().add(john);
		mSharedByMeItems.add(s1);
		
		/*###########################*/
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
	
	public synchronized void addPerson(Person person){
		/* If person exists, delete it */
		deletePerson(person);
		/* Add the new person */
		mPersonList.add(person);
	}
	
	/* If person device ID does not exists, does nothing */
	public synchronized void deletePerson(Person person){
		Iterator<Person> itr = mPersonList.iterator();
		/* Search for Person with same device ID */
	    while (itr.hasNext()) {
	      Person tempPerson = itr.next();
	      /* If has the same ID, delete it */
	      if (tempPerson.getDeviceID().compareTo(person.getDeviceID()) == 0){
	    	  mPersonList.remove(tempPerson);
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
	
	public String getThisDeviceId(){
		return mThisDeviceId;
	}
	
	public String getThisDeviceName(){
		return mThisDevideName;
	}

	public NetworkSender getNetworkSender() {
		return mNetworkSender;
	}

	public NetworkListener getNetworkListener() {
		return mNetworkListener;
	}
}
