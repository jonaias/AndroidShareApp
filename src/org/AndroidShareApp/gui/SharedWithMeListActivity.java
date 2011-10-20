/**
 * 
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.Person;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
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
public class SharedWithMeListActivity extends ListActivity {

	Person person0 = new Person("Seu ze;");
	Person person1 = new Person("Mane");
	Person person2 = new Person("Andrew");
	EfficientAdapter adapter;

	static ArrayList<Person> list_of_persons = new ArrayList<Person>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared_with_me_list_activity);
		list_of_persons.add(person0);
		list_of_persons.add(person1);
		list_of_persons.add(person2);

		adapter = new EfficientAdapter(this);
		setListAdapter(adapter);

		Toast.makeText(getApplicationContext(), R.string.person_list_legend,
				Toast.LENGTH_LONG).show();   
	}
	
	public void startShareWithMeActivity(Person person){
		Intent tempIntent = new Intent(this.getApplicationContext(), SharedWithMeActivity.class);
		tempIntent.putExtra("Person", person.getDeviceID());
		startActivityForResult(tempIntent, 0);
	}

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

			// When convertView is not null, we can reuse it directly, there is
			// no need to reinflate it. We only inflate a new View when the
			// convertView
			// supplied by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.shared_with_me_list_item, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.textLine = (TextView) convertView
						.findViewById(R.id.personListItemTextView);

				convertView.setOnClickListener(new OnClickListener() {
					private int pos = position;

					@Override
					public void onClick(View v) {
						Intent tempIntent = new Intent(context, SharedWithMeActivity.class);
						tempIntent.putExtra("Person", list_of_persons.get(position).getDeviceID());
						context.startActivity(tempIntent);
					}
				});

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.textLine.setText(list_of_persons.get(position).getName());

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
		public int getCount() {
			return list_of_persons.size();
		}

		@Override
		public Object getItem(int position) {
			return list_of_persons.get(position);
		}

	}

}
