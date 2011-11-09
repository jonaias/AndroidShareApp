/**
 * 
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.NetworkManager;
import org.AndroidShareApp.core.Person;
import org.AndroidShareApp.core.SharedByMeItem;
import org.AndroidShareApp.core.SharedPerson;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author jonaias
 * 
 */
public class SharedByMeConfigActivity extends ListActivity implements
		OnClickListener {

	private EfficientAdapter mAdapter;
	private static ArrayList<SharedByMeItem> mSharedByMeItems;
	private static int mClickPosition;
	private static SharedByMeItem mNewItem;
	private static boolean mIsCreating;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shared_by_me_config_activity);

		mSharedByMeItems = NetworkManager.getInstance().getSharedByMeItems();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mIsCreating = false;
			mClickPosition = extras.getInt("position");
		} else {
			mIsCreating = true;
			mNewItem = new SharedByMeItem();
			mSharedByMeItems.add(mNewItem);
			mClickPosition = mSharedByMeItems.indexOf(mNewItem);
		}

		mAdapter = new EfficientAdapter(this, this);
		setListAdapter(mAdapter);

		Button selectSharePathButton = (Button) findViewById(R.id.selectSharePathButton);
		selectSharePathButton.setOnClickListener(this);

		ImageButton buttonDelete = (ImageButton) findViewById(R.id.buttonDelete);
		buttonDelete.setEnabled(false);
		buttonDelete.setVisibility(Button.INVISIBLE);

		ToggleButton activateToggleButton = (ToggleButton) findViewById(R.id.activateToggleButton);
		if (mClickPosition != -1)
			activateToggleButton.setChecked(mSharedByMeItems
					.get(mClickPosition).isActive());
		activateToggleButton.setOnClickListener(this);

		Button addPersonToShareButton = (Button) findViewById(R.id.addPersonToShareButton);
		addPersonToShareButton.setOnClickListener(this);

		Button sharedByMeBackButton = (Button) findViewById(R.id.sharedByMeBackButton);
		sharedByMeBackButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.selectSharePathButton) {
			((EditText) findViewById(R.id.sharedPathEditText))
					.setText("User selected something.");
		} else if (v.getId() == R.id.activateToggleButton) {
			ToggleButton button = (ToggleButton) v;
			mSharedByMeItems.get(mClickPosition).setActive(button.isChecked());
		} else if (v.getId() == R.id.addPersonToShareButton) {
			Intent intent = new Intent(this, PersonListActivity.class);
			startActivityForResult(intent, 1);
		} else if (v.getId() == R.id.sharedByMeBackButton)
			finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle returnedData = data.getExtras();
		String deviceID = returnedData.getString("selectedPerson");
		Person selectedPerson = NetworkManager.getInstance()
				.getPersonByDeviceID(deviceID);
		SharedPerson selectedSharedPerson = new SharedPerson(
				selectedPerson.getName(), selectedPerson.getDeviceID(),
				selectedPerson.getIP(), true, false);

		synchronized (mSharedByMeItems) {
			mSharedByMeItems.get(mClickPosition).managePerson(
					selectedSharedPerson);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshUi();
	}

	public void refreshUi() {
		this.runOnUiThread(new Runnable() {
			public void run() {
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;
		// private Context mContext;
		private SharedByMeConfigActivity mActivity;

		public EfficientAdapter(Context context,
				SharedByMeConfigActivity activity) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			// mContext = context;
			mActivity = activity;
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
			convertView = mInflater.inflate(R.layout.shared_by_me_config_item,
					null);

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

			holder.canReadCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					ArrayList<SharedByMeItem> shares = NetworkManager
							.getInstance().getSharedByMeItems();
					shares.get(mClickPosition).getSharedPersonList()
							.get(position).setRead(cb.isChecked());
				}
			});

			holder.canWriteCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					ArrayList<SharedByMeItem> shares = NetworkManager
							.getInstance().getSharedByMeItems();
					shares.get(mClickPosition).getSharedPersonList()
							.get(position).setWrite(cb.isChecked());
				}
			});

			if (!mIsCreating) {
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
