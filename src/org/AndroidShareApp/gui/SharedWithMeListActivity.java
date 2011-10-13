/**
 * 
 */
package org.AndroidShareApp.gui;

import org.AndroidShareApp.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author jonaias
 *
 */
public class SharedWithMeListActivity extends Activity {

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);

	      setContentView(R.layout.shared_with_me_list_activity);

	      ListView lv = (ListView) findViewById(R.id.personList);
	      lv.setAdapter(new ArrayAdapter<String>(this, R.layout.shared_with_me_list_item, WRITE_LIST));
	      lv.setTextFilterEnabled(true);

	      Toast.makeText(getApplicationContext(), R.string.person_list_legend,
	              Toast.LENGTH_LONG).show();
	      
	      lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	          // When clicked, show a toast with the TextView text
	          Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	              Toast.LENGTH_SHORT).show();
	        }
	      });
	      
	      
	    }
	    
	    
	    static final String[] WRITE_LIST = new String[] {
	        "Andrew", "Joseph", "Oguration", "Réééé"
	      };
}
