/**
 *
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.NetworkManager;
import org.AndroidShareApp.core.Person;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jonaias
 * 
 */
public class SharedWithMeListActivity extends ListActivity implements RefreshInterface {

	private EfficientAdapter adapter;
	private static ArrayList<Person> personList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_with_me_list_activity);

		personList = NetworkManager.getInstance().getPersonList();

		adapter = new EfficientAdapter(this);
		setListAdapter(adapter);
		

		//Toast.makeText(getApplicationContext(), R.string.person_list_legend,
		//		Toast.LENGTH_LONG).show();
	
	}
	
	
	
	/*-------------- Callback functions start -----------*/
	public void refreshUi(){
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
		NetworkManager.getInstance().getNetworkSender().registerCallBack(this);
	}
	@Override
	protected void onPause(){
		super.onPause();
		/*TODO: Verificar se pode dar problema!!!*/
		NetworkManager.getInstance().getNetworkSender().registerCallBack(null);
	}
	/*-------------- Callback functions stop -----------*/

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;
		private Context context;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.context = context;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			
			// A ViewHolder keeps references to children views to avoid
			// unnecessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			convertView = mInflater.inflate(R.layout.shared_with_me_list_item, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.textLine = (TextView) convertView
					.findViewById(R.id.personListItemTextView);

			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent tempIntent = new Intent(context,
							SharedWithMeActivity.class);
					synchronized (personList) { 
						tempIntent.putExtra("deviceID", personList.get(position)
								.getDeviceID());
					}
					context.startActivity(tempIntent);
				}
			});

			convertView.setTag(holder);
		

			// Bind the data efficiently with the holder.
			synchronized (personList) {
				holder.textLine.setText(personList.get(position).getName());
			}

			return convertView;
		}

		static class ViewHolder {
			TextView textLine;
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
		/* Dont count Everybody */
		public int getCount() {
			int ret;
			synchronized (personList) {
				ret = personList.size()-1;
			}
			return ret;
		}

		@Override
		public Object getItem(int position) {
			Object ret;
			synchronized (personList) {
				ret = personList.get(position+1);
			}
			return ret;
		}

	}

}
