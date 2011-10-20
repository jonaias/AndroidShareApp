package org.AndroidShareApp.core;

import java.util.ArrayList;

public class NetworkManager {

	private static NetworkManager mSingleton;
	private ArrayList<Person> mPersonList;
	private ArrayList<FileTransferrer> mCurrentTransfers;
	private ArrayList<SharedByMeItem> mSharedByMeItems;

	private NetworkManager() {
		mSingleton = null;
		mPersonList = new ArrayList<Person>();
		mCurrentTransfers = new ArrayList<FileTransferrer>();
		mSharedByMeItems = new ArrayList<SharedByMeItem>();
		
		SharedPerson everybody = new SharedPerson("Everybody");
		mPersonList.add(everybody);
		
		/*############ TODO: REMOVE! JUST FOR DEBUGGING. ############## */
		SharedPerson james = new SharedPerson("James Stewart", true, false);
		SharedPerson john = new SharedPerson("John Noble", false, true);
		SharedPerson paul = new SharedPerson("Paul Tomas", true, true);
		
		SharedByMeItem item1 = new SharedByMeItem("Music/");
		item1.managePerson(james);
		item1.managePerson(john);
		item1.managePerson(paul);
		
		/*------------------------------------------------------------ */
		
		SharedPerson james2 = new SharedPerson("James Stewart", false, true);
		SharedPerson john2 = new SharedPerson("John Noble", true, true);
		SharedPerson paul2 = new SharedPerson("Paul Tomas", true, false);
		
		SharedByMeItem item2 = new SharedByMeItem("file.pdf");
		item2.managePerson(james2);
		item2.managePerson(john2);
		item2.managePerson(paul2);
		
		mSharedByMeItems.add(item1);
		mSharedByMeItems.add(item2);
		
		/* ########################################################### */
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

	public void addNewTransfer(FileTransferrer newTransfer) {
		mCurrentTransfers.add(newTransfer);
	}

	public void deleteTransfer(FileTransferrer item) {
		mCurrentTransfers.remove(item);
	}

	public ArrayList<FileTransferrer> getTransfers() {
		return mCurrentTransfers;
	}
}
