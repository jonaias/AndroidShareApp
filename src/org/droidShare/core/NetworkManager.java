package org.droidShare.core;

import java.util.ArrayList;

public class NetworkManager {

	private static NetworkManager singleton;
	private ArrayList<Person> mPersonList;
	private ArrayList<FileTransferrer> mCurrentTransfers;
	private ArrayList<SharedByMeItem> mSharedByMeItems;

	private NetworkManager() {
		mPersonList = new ArrayList<Person>();
		mCurrentTransfers = new ArrayList<FileTransferrer>();
		mSharedByMeItems = new ArrayList<SharedByMeItem>();
		
		Person everybody = new Person("Everybody");
		mPersonList.add(everybody);
	}

	public static synchronized NetworkManager getInstance() {
		if (singleton == null)
			singleton = new NetworkManager();

		return singleton;
	}

	public void addNewSharedByMe(SharedByMeItem newItem) {
		mSharedByMeItems.add(newItem);
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
