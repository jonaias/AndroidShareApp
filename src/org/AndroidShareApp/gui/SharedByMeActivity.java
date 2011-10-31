/**
 * 
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.NetworkManager;
import org.AndroidShareApp.core.SharedByMeItem;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Copyright (C) 2009 codemobiles.com.
 * http://www.codemobiles.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Chaiyasit Tayabovorn 
 * email: chaiyasit.t@gmail.com
 * 
 * Modified in October 2011 for this project by:
 * author: Andre Luiz Verucci da Cunha
 * e-mail: andrecunha.usp@gmail.com
 * 
 */

public class SharedByMeActivity extends ListActivity implements
		OnClickListener, OnItemClickListener {

	private EfficientAdapter adap;
	private static ArrayList<SharedByMeItem> mSharedByMeItems;
	//private volatile int mCurrentSelectedItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shared_by_me_activity);

		mSharedByMeItems = NetworkManager.getInstance().getSharedByMeItems();

		adap = new EfficientAdapter(this, this);
		setListAdapter(adap);

		Button createShareButton = (Button) findViewById(R.id.createShareButton);
		createShareButton.setOnClickListener(this);
		
		getListView().setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume () {
		super.onResume();
		getListView().invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		v.setSelected(true);
		//mCurrentSelectedItem = position;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.createShareButton) {
			Intent intent = new Intent(this, SharedByMeConfigActivity.class);
			startActivity(intent);
		}
	}
	
	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;
		private Bitmap mIcon1;
		private Context mContext;
		//private SharedByMeActivity mListActivity;

		public EfficientAdapter(Context context,
				SharedByMeActivity listActivity) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			mContext = context;
			//mListActivity = listActivity;
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
						R.layout.shared_by_me_list_item, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.textLine = (TextView) convertView
						.findViewById(R.id.textLine);
				holder.iconLine = (ImageView) convertView
						.findViewById(R.id.iconLine);

				//convertView.setOnClickListener(mListActivity);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, SharedByMeConfigActivity.class);
						intent.putExtra("position", position);
						mContext.startActivity(intent);
					}
				});

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			int id = mContext.getResources().getIdentifier("flag_0",
					"drawable", mContext.getString(R.string.package_str));

			if (id != 0x0) {
				mIcon1 = BitmapFactory.decodeResource(mContext.getResources(),
						id);
			}
			holder.iconLine.setImageBitmap(mIcon1);
			holder.textLine.setText(mSharedByMeItems.get(position)
					.getSharedPath());

			return convertView;
		}

		static class ViewHolder {
			TextView textLine;
			ImageView iconLine;
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
			return mSharedByMeItems.size();
		}

		@Override
		public Object getItem(int position) {
			// return data[position];
			return mSharedByMeItems.get(position).getSharedPath();
		}

	}
}
