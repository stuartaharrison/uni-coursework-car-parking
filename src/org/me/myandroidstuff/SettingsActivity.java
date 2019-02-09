/**
 * Defines all the components and methods for the Settings tab. Acts like a fragment on screen and
 * then displays all the settings for the application. These settings are like turning the auto-update
 * function off.
 * 
 * @author Stuart Harrison - S0907581
 * @version 1.0
 * @since 03/03/2015
 */

package org.me.myandroidstuff;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;

public class SettingsActivity extends Activity {
	
	private CheckBox autoUpdateCheck;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);
        autoUpdateCheck = (CheckBox)findViewById(R.id.chkbAutoUpdate);
    }
}
