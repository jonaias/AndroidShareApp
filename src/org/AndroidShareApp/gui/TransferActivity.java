/**
 * 
 */
package org.AndroidShareApp.gui;

import java.util.ArrayList;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.FileTransferrer;
import org.AndroidShareApp.core.NetworkManager;
import org.AndroidShareApp.core.RefreshInterface;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author jonaias
 * 
 */
public class TransferActivity extends ListActivity implements RefreshInterface {

	private EfficientAdapter mAdapter;
	private ArrayList<FileTransferrer> mCurrentTransfers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_activity);

		mCurrentTransfers = NetworkManager.getInstance().getTransfers();

		mAdapter = new EfficientAdapter(this);

		setListAdapter(mAdapter);
	}

	/*-------------- Callback functions start -----------*/
	public void refreshUi() {
		this.runOnUiThread(new Runnable() {
			public void run() {
				mAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		NetworkManager.getInstance().getNetworkSender().registerCallBack(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		/* TODO: Verificar se pode dar problema!!! */
		NetworkManager.getInstance().getNetworkSender().registerCallBack(null);
	}

	/*-------------- Callback functions stop -----------*/

	public class EfficientAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater mInflater;
		private ArrayList<FileTransferrer> mCurrentTransfers;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			mCurrentTransfers = TransferActivity.this.mCurrentTransfers;
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
			// unnecessary calls to findViewById() on each row.
			ViewHolder holder;

			convertView = mInflater.inflate(R.layout.transfer_item, null);

			// Creates a ViewHolder and store references to the two children
			// views we want to bind data to.
			holder = new ViewHolder();
			holder.transferInfoText = (TextView) convertView
					.findViewById(R.id.transferInfoText);
			holder.transferPersonText = (TextView) convertView
					.findViewById(R.id.transferPersonText);
			holder.transferCancelButton = (ImageButton) convertView
					.findViewById(R.id.transferCancelButton);
			holder.transferProgress = (ProgressBar) convertView
					.findViewById(R.id.transferProgress);

			holder.transferCancelButton
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							TransferActivity.this.mCurrentTransfers.get(
									position).interrupt();
						}
					});

			convertView.setTag(holder);

			synchronized (mCurrentTransfers) {
				holder.transferInfoText.setText(mCurrentTransfers.get(position)
						.getPath());
				// TODO: Colocar o nome da pessoa na classe FileTransfer e
				// colocá-lo
				// no holder.transferPersonText
				holder.transferProgress.setProgress((int) Math
						.round(mCurrentTransfers.get(position).getProgress()));
				if (holder.transferProgress.getProgress() == 100)
					holder.transferInfoText.setText(holder.transferInfoText
							.getText() + " (Concluded)");
				if (mCurrentTransfers.get(position).getType() == FileTransferrer.TYPE_DOWNLOAD)
					holder.transferPersonText.setText("from " + mCurrentTransfers
							.get(position).getPerson().getName());
				else
					holder.transferPersonText.setText("to " + mCurrentTransfers
							.get(position).getPerson().getName());
			}

			return convertView;
		}

		class ViewHolder {
			TextView transferInfoText;
			TextView transferPersonText;
			ImageButton transferCancelButton;
			ProgressBar transferProgress;
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
			int ret;
			synchronized (mCurrentTransfers) {
				ret = mCurrentTransfers.size();
			}
			return ret;
		}

		@Override
		public Object getItem(int position) {
			Object ret;
			synchronized (mCurrentTransfers) {
				ret = mCurrentTransfers.get(position);
			}
			return ret;
		}

	}
}
