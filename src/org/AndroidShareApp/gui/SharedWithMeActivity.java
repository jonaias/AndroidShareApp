/**
 *
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;
import java.util.Iterator;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.NetworkManager;
import org.AndroidShareApp.core.Person;
import org.AndroidShareApp.core.SharedWithMeItem;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jonaias
 * 
 */
public class SharedWithMeActivity extends ListActivity implements OnClickListener {

	private static EfficientAdapter adapter;
	private static Person mPerson;
	private static ArrayList<SharedWithMeItem> itemList;
	private static ArrayList<String> mItemsToDownload;
	private static Button mDownloadButton;
	private static TextView mInfoText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_with_me_activity);
		
		Bundle bundle = this.getIntent().getExtras();
		
		mPerson = NetworkManager.getInstance().getPersonByDeviceID(bundle.getString("deviceID"));
		
		itemList = mPerson.getSharedWithMeItems();
		
	    adapter = new EfficientAdapter(this);
		setListAdapter(adapter);
		
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		TextView textView = (TextView) findViewById(R.id.shareNameText);
		textView.setText(mPerson.getName());
		
		mDownloadButton = (Button) findViewById(R.id.downloadButton);
		mDownloadButton.setOnClickListener(this);
		
		mInfoText = (TextView) findViewById(R.id.infoText);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		getListView().setItemsCanFocus(false);
		
	}
	
	/*-------------- Callback functions start -----------*/
	
	public static void refreshItemList(){
		itemList = mPerson.getSharedWithMeItems();
		adapter.notifyDataSetChanged();
	}
	
	public void refreshUi(){
		refreshItemList();
		this.runOnUiThread(
                new Runnable(){
                    public void run(){
                    	adapter.notifyDataSetChanged();
                    }
                });
	}
	@Override
	protected void onResume(){
		super.onResume();
		//NetworkManager.getInstance().getNetworkSender().registerCallBack(this);
	}
	@Override
	protected void onPause(){
		super.onPause();
		//NetworkManager.getInstance().getNetworkSender().registerCallBack(null);
	}
	/*-------------- Callback functions stop -----------*/
	
	
	
	/*------ Path management ------*/
	private static void addItemToDownload(String path){
		mItemsToDownload.add(path);
		if (mItemsToDownload.size()>0){
			mDownloadButton.setVisibility(View.VISIBLE);
			mInfoText.setText(mItemsToDownload.size()+" file(s) and/or folder(s) selected."+
					"\nUse right arrow to download");
		}
	}
	
	private static void removeItemToDownload(String path){
		Iterator<String> itr = mItemsToDownload.iterator();
		while (itr.hasNext()) {
			String tempItem = itr.next();
			if (tempItem.compareTo(path) == 0) {
				itr.remove();
			}
		}
		if (mItemsToDownload.size()==0){
			mDownloadButton.setVisibility(View.INVISIBLE);
			mInfoText.setText(R.string.select_a_file_tip);
		}
		else{
			mInfoText.setText(mItemsToDownload.size()+" file(s) and/or folder(s) selected." +
					"\nUse right arrow to download");
		}
	}
	
	private static boolean existsItemToDownload(String path){
		Iterator<String> itr = mItemsToDownload.iterator();
		while (itr.hasNext()) {
			String tempItem = itr.next();
			if (tempItem.compareTo(path) == 0) {
				return true;
			}
		}
		return false;
	}
	/*----Path management stop----*/
	
	/* Concatenate Path with file */
	private static String fullPath(String path, String file){
		String buffer = path;
		buffer = buffer.concat(file.substring(1));
		return buffer;
	}
	
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} 
		else if ( v.getId() == R.id.downloadButton){
			NetworkManager.getInstance().getNetworkSender().requestDownload(mPerson, mItemsToDownload);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//super.onListItemClick(l, v, position, id);
		String fullItemPath = mPerson.getCurrentPath();
		fullItemPath = fullItemPath.concat(itemList.get(position).getSharedPath().substring(1));
		
		if (existsItemToDownload(fullItemPath)){
			v.setBackgroundColor(Color.TRANSPARENT);
			v.setSelected(false);
			removeItemToDownload(fullItemPath);
			Log.i("SharedWithMeActivity", "Removed from mFilesToDownload "+ fullItemPath);
		}
		else{
			v.setBackgroundColor(R.drawable.list_background);
			v.setSelected(true);
			addItemToDownload(fullItemPath);
			Log.i("SharedWithMeActivity", "Added to mFilesToDownload "+ fullItemPath);	
		}
	}

	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			mItemsToDownload = new ArrayList<String>();
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			
			final SharedWithMeItem sharedWithMeItem = itemList.get(position);
			// A ViewHolder keeps references to children views to avoid
			// unnecessary calls
			// to findViewById() on each row.
			final ViewHolder holder;

			//convertView = mInflater.inflate(R.layout.adaptor_content, null);
			convertView = mInflater.inflate(R.layout.shared_with_me_item, null);

			// Creates a ViewHolder and store references to objects
			holder = new ViewHolder();
			holder.textLine = (TextView) convertView
					.findViewById(R.id.textLine);
			holder.iconLine = (ImageView) convertView
					.findViewById(R.id.iconLine);
			holder.downPathIconLine = (ImageView) convertView
					.findViewById(R.id.downPathIcon);

			convertView.setTag(holder);
				

			// Bind the data efficiently with the holder.
			holder.textLine.setText(sharedWithMeItem.getSharedPath());
			
			/* Parse item and add needed icons and arrows*/
			if(sharedWithMeItem.isPath()){
				holder.iconLine.setImageResource(R.drawable.folder) ;
				holder.downPathIconLine.setVisibility(View.VISIBLE);
				if(sharedWithMeItem.getSharedPath().compareTo("/")==0){
					/* A directory "/" will be considered folder up level */  
					holder.downPathIconLine.setImageResource(R.drawable.left_arrow);
					holder.downPathIconLine.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							/* buffer = ../ */
							String buffer = mPerson.getCurrentPath();
							buffer = buffer.substring(0, buffer.length()-2);
							buffer = buffer.substring(0,buffer.lastIndexOf('/')+1);
							/* Change person current Path */
							mPerson.setCurrentPath(buffer);
							/* Refresh List */
							refreshItemList();
		
						}
					});
				}
				else{
					holder.downPathIconLine.setImageResource(R.drawable.right_arrow);
					holder.downPathIconLine.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							/* Change person current Path */
							mPerson.setCurrentPath(fullPath(mPerson.getCurrentPath(), sharedWithMeItem.getSharedPath()));
							/* Refresh list */
							refreshItemList();

						}
					});
					
				}
			}
			else{
				holder.iconLine.setImageResource(R.drawable.file);
				holder.downPathIconLine.setVisibility(View.INVISIBLE);
			}
			
			if( existsItemToDownload(fullPath(mPerson.getCurrentPath(), sharedWithMeItem.getSharedPath()) )){
				convertView.setBackgroundColor(R.drawable.list_background);
				convertView.setSelected(true);
			}
			
			return convertView;
		}

		static class ViewHolder {
			TextView textLine;
			ImageView iconLine;
			ImageView downPathIconLine;
		}

		@Override
		public Filter getFilter() {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

	}
}