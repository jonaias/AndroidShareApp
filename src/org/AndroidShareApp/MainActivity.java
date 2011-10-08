package org.AndroidShareApp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_activity);
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        Intent intent;  // Reusable Intent for each tab

        // Add a new tab with SharedByMeActivity
        intent = new Intent(this,SharedByMeActivity.class);
        tabHost.addTab(tabHost.newTabSpec("sharedByMe").setIndicator("Shared By Me")
                      .setContent(intent));

        //  Add a new tab with SharedWithMeActivity
        intent = new Intent(this,SharedWithMeListActivity.class);
        tabHost.addTab(tabHost.newTabSpec("sharedWithMe").setIndicator("Shared With Me")
        						  .setContent(intent));
        
        //for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
         //   tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 23;
        //}

        tabHost.setCurrentTab(1);
    }

    
}