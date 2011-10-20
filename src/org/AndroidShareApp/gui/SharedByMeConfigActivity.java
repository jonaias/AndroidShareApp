/**
 * 
 */
package org.AndroidShareApp.gui;

import org.AndroidShareApp.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jonaias
 * 
 */
public class SharedByMeConfigActivity extends ListActivity {

	private EfficientAdapter adap;
	private static String[] data = new String[] { "Fulano de Tal",
			"Beltrano de Tal", "Cicrano de Tal" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shared_by_me_config_activity);
		adap = new EfficientAdapter(this);
		setListAdapter(adap);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "Got click on share" + String.valueOf(position),
				Toast.LENGTH_SHORT).show();
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
				convertView = mInflater.inflate(
						R.layout.shared_by_me_config_item, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.peerName = (TextView) convertView
						.findViewById(R.id.peerNameTextBox);
				holder.canReadCheckBox = (CheckBox) convertView
						.findViewById(R.id.canReadCheckBox);
				holder.canWriteCheckBox = (CheckBox) convertView
						.findViewById(R.id.canWriteCheckBox);

				convertView.setOnClickListener(new OnClickListener() {
					private int pos = position;

					@Override
					public void onClick(View v) {
						Toast.makeText(context,
								"Got click on share " + String.valueOf(pos),
								Toast.LENGTH_SHORT).show();
					}
				});

				holder.peerName.setText(data[position]);
				holder.canReadCheckBox.setChecked(true);
				holder.canWriteCheckBox.setChecked(false);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			return convertView;
		}

		static class ViewHolder {
			TextView peerName;
			CheckBox canReadCheckBox;
			CheckBox canWriteCheckBox;
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
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			return data[position];
		}
	}
}
