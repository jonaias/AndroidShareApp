package org.AndroidShareApp.gui;

import org.AndroidShareApp.R;

import android.app.TabActivity;
import android.content.Intent;
//import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        //Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		
        // Add a new tab with SharedByMeActivity
		intent = new Intent().setClass(this, SharedByMeActivity.class);
        spec = tabHost.newTabSpec("sharedByMe").setIndicator(getString(R.string.shared_by_me))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Add a new tab with SharedWithMeActivity
		intent = new Intent().setClass(this, SharedWithMeListActivity.class);
        spec = tabHost.newTabSpec("sharedWithMe").setIndicator(getString(R.string.shared_with_me))
                      .setContent(intent);
        tabHost.addTab(spec);

        //TODO: Remove. Just for debugging.
		intent = new Intent().setClass(this, TransferActivity.class);
        spec = tabHost.newTabSpec("transfer").setIndicator(getString(R.string.active_transfers))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;
        }

        tabHost.setCurrentTab(0);
    }
}