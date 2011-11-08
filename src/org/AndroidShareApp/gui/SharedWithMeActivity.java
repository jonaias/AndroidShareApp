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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

	private EfficientAdapter adap;
	private static String[] data = new String[] { "0", "1", "2", "3", "4" };
	private String personId;
	private static ArrayList<SharedWithMeItem> itemList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_with_me_activity);
		
		Bundle bundle = this.getIntent().getExtras();
		personId = bundle.getString("person");
		
		ArrayList<Person> personList = NetworkManager.getInstance().getPersonList();
		Iterator<Person> itr = personList.iterator();
	    while (itr.hasNext()) {
	    	  Person tempPerson = itr.next();
		      /* If this person is Everybody, skip it */			      
		      if(tempPerson.getName().compareTo(personId)==0){
		    	  itemList = tempPerson.getSharedWithMeItems();
		    	  break;
		      }
	    }
		
	    adap = new EfficientAdapter(this);
		setListAdapter(adap);
		
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		EditText editText = (EditText) findViewById(R.id.shareNameText);
		editText.setText(personId);
		
		
	}
	
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
		private Bitmap mIcon1;
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
			
			SharedWithMeItem sharedWithMeItem = itemList.get(position);
			// A ViewHolder keeps references to children views to avoid
			// unnecessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need to reinflate it. We only inflate a new View when the
			// convertView
			// supplied by ListView is null.
			if (convertView == null) {
				//convertView = mInflater.inflate(R.layout.adaptor_content, null);
				convertView = mInflater.inflate(R.layout.shared_with_me_item, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.textLine = (TextView) convertView
						.findViewById(R.id.textLine);
				holder.iconLine = (ImageView) convertView
						.findViewById(R.id.iconLine);
				holder.downPathIconLine = (ImageView) convertView
						.findViewById(R.id.downPathIcon);
				
				
				convertView.setOnClickListener(new OnClickListener() {
					private int pos = position;

					@Override
					public void onClick(View v) {
						Toast.makeText(context,
								"Got click on share " + String.valueOf(pos),
								Toast.LENGTH_SHORT).show();
					}
				});

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
				
			
			// Get flag name and id
			int id = context.getResources().getIdentifier(sharedWithMeItem.typeString(), "drawable",
					context.getString(R.string.package_str));

			// Icons bound to the rows.
			if (id != 0x0) {
				mIcon1 = BitmapFactory.decodeResource(context.getResources(),
						id);
			}

			// Bind the data efficiently with the holder.
			holder.iconLine.setImageBitmap(mIcon1);
			holder.textLine.setText(sharedWithMeItem.getSharedPath());
			
			if(sharedWithMeItem.isPath()){
				holder.downPathIconLine.setVisibility(View.VISIBLE);
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