/**
 * 
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.NetworkManager;
import org.AndroidShareApp.core.SharedByMeItem;
import org.AndroidShareApp.core.SharedPerson;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * @author jonaias
 * 
 */
public class SharedByMeConfigActivity extends ListActivity implements
		OnClickListener {

	private EfficientAdapter adap;
	private static ArrayList<SharedByMeItem> mSharedByMeItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shared_by_me_config_activity);

		mSharedByMeItems = NetworkManager.getInstance().getSharedByMeItems();

		adap = new EfficientAdapter(this, this);
		setListAdapter(adap);

		Button selectSharePathButton = (Button) findViewById(R.id.selectSharePathButton);
		selectSharePathButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.selectSharePathButton) {
			((EditText) findViewById(R.id.sharedPathEditText))
					.setText("User selected something.");
		}
	}

	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;
		// private Context mContext;
		private SharedByMeConfigActivity mActivity;
		private int mClickPosition;

		public EfficientAdapter(Context context,
				SharedByMeConfigActivity activity) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			// mContext = context;
			mActivity = activity;

			Bundle extras = mActivity.getIntent().getExtras();
			if (extras != null)
				mClickPosition = extras.getInt("position");
			else
				mClickPosition = -1;
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

				holder.canReadCheckBox
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								CheckBox cb = (CheckBox) v;
								ArrayList<SharedByMeItem> shares = NetworkManager
										.getInstance().getSharedByMeItems();
								shares.get(mClickPosition)
										.getSharedPersonList().get(position)
										.setRead(cb.isChecked());
							}
						});

				holder.canWriteCheckBox
						.setOnClickListener(new OnClickListener() {
		
							@Override
							public void onClick(View v) {
								CheckBox cb = (CheckBox) v;
								ArrayList<SharedByMeItem> shares = NetworkManager
										.getInstance().getSharedByMeItems();
								shares.get(mClickPosition)
										.getSharedPersonList().get(position)
										.setWrite(cb.isChecked());
							}
						});
				
				if (mClickPosition != -1) {
					ArrayList<SharedPerson> person = mSharedByMeItems.get(
							mClickPosition).getSharedPersonList();

					holder.peerName.setText(person.get(position).getName());
					holder.canReadCheckBox.setChecked(person.get(position)
							.canRead());
					holder.canWriteCheckBox.setChecked(person.get(position)
							.canWrite());

					((EditText) mActivity.findViewById(R.id.sharedPathEditText))
							.setText(mSharedByMeItems.get(mClickPosition)
									.getSharedPath());
				} else {
					((EditText) mActivity.findViewById(R.id.sharedPathEditText))
							.setText("Press 'Select' to set path");
				}
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
			if (mClickPosition == -1)
				return 0;
			return mSharedByMeItems.get(mClickPosition).getSharedPersonList()
					.size();
		}

		@Override
		public Object getItem(int position) {
			if (mClickPosition == -1)
				return null;
			return mSharedByMeItems.get(mClickPosition).getSharedPath();
		}
	}
}
