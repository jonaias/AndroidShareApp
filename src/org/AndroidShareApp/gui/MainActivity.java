package org.AndroidShareApp.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.AndroidShareApp.R;
import org.AndroidShareApp.core.NetworkManager;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

//import android.content.res.Resources;

public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Add a new tab with SharedByMeActivity
		intent = new Intent().setClass(this, SharedByMeActivity.class);
		spec = tabHost.newTabSpec("sharedByMe")
				.setIndicator(getString(R.string.shared_by_me))
				.setContent(intent);
		tabHost.addTab(spec);

		// Add a new tab with SharedWithMeActivity
		intent = new Intent().setClass(this, SharedWithMeListActivity.class);
		spec = tabHost.newTabSpec("sharedWithMe")
				.setIndicator(getString(R.string.shared_with_me))
				.setContent(intent);
		tabHost.addTab(spec);

		// TODO: Remove. Just for debugging.
		intent = new Intent().setClass(this, TransferActivity.class);
		spec = tabHost.newTabSpec("transfer")
				.setIndicator(getString(R.string.active_transfers))
				.setContent(intent);
		tabHost.addTab(spec);

		for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;
		}

		tabHost.setCurrentTab(0);
		
		InetAddress bcast = getBroadcastAddress();
		InetAddress host = getHostAddress();
		
		NetworkManager.getInstance().setBroadcastAddress(bcast);
		NetworkManager.getInstance().setHostAddress(host);
		
		Log.i("MainActivity", "Broadcast address: " + bcast);
		Log.i("MainActivity", "Host address: " + host);
	}

	private InetAddress getBroadcastAddress() {
		WifiManager wifi = (WifiManager) getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		try {
			return InetAddress.getByAddress(quads);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private InetAddress getHostAddress() {
		WifiManager wifi = (WifiManager) getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = dhcp.ipAddress;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		try {
			return InetAddress.getByAddress(quads);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
}