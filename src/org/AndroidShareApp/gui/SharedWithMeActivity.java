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
	private static ArrayList<String> mFilesToDownload;

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
		
		EditText editText = (EditText) findViewById(R.id.shareNameText);
		editText.setText(mPerson.getName());
		
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
		mFilesToDownload.add(path);
	}
	
	private static void removeItemToDownload(String path){
		Iterator<String> itr = mFilesToDownload.iterator();
		while (itr.hasNext()) {
			String tempItem = itr.next();
			if (tempItem.compareTo(path) == 0) {
				itr.remove();
			}
		}
	}
	
	private static boolean existsItemToDownload(String path){
		Iterator<String> itr = mFilesToDownload.iterator();
		while (itr.hasNext()) {
			String tempItem = itr.next();
			if (tempItem.compareTo(path) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/*-----------------------------*/
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "Got click on share" + String.valueOf(position),
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backButton) {
			finish();
		} 
	}

	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;
		private Context context;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.context = context;
			mFilesToDownload = new ArrayList<String>();
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
							/* buffer = ./ + sharedWithMeItem.getSharedPath() */
							String buffer = mPerson.getCurrentPath();
							buffer = buffer.concat(sharedWithMeItem.getSharedPath().substring(1));
							/* Change person current Path */
							mPerson.setCurrentPath(buffer);
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
			
			
			holder.iconLine.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context,
							"Got click on icon " + String.valueOf(position),
							Toast.LENGTH_SHORT).show();
				}
			});
			
			holder.textLine.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (holder.textLine.isSelected()){
						holder.textLine.setBackgroundColor(Color.TRANSPARENT);
						holder.textLine.setSelected(false);
						String buffer = mPerson.getCurrentPath();
						buffer = buffer.concat(sharedWithMeItem.getSharedPath().substring(1));
						removeItemToDownload(buffer);
						Log.i("SharedWithMeActivity", "Removed from mFilesToDownload "+ buffer);
					}
					else{
						holder.textLine.setBackgroundColor(R.drawable.list_background);
						holder.textLine.setSelected(true);
						String buffer = mPerson.getCurrentPath();
						buffer = buffer.concat(sharedWithMeItem.getSharedPath().substring(1));
						addItemToDownload(buffer);
						Log.i("SharedWithMeActivity", "Added to mFilesToDownload "+ buffer);	
					}
					Toast.makeText(context,
							"Got click on text " + String.valueOf(position),
							Toast.LENGTH_SHORT).show();
				}
			});		
			
			
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