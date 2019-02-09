/**
 * This is the main class of the application. It will setup all the other
 * tab activities and then assign them to the tab host.
 * 
 * @author Stuart Harrison - S0907581
 * @version 1.2
 * @since 01/03/2015
 */

package org.me.myandroidstuff;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.ViewSwitcher;

//TabActivity is deprecated however it serves as a good way to outline my design
//whilst I ensure the rest off my application works. Also the new way of fragments
//will not work with the minimum selected API.
@SuppressWarnings("deprecation")
public class CarParkListingTestActivity extends TabActivity
{
	private ViewSwitcher switcher;
	private TabHost tabHost;
    
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)  {
		//Setup components
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        switcher = (ViewSwitcher)findViewById(R.id.mainViewSwitch);
        tabHost = getTabHost();
        
        //Car Park Display Tab
        TabSpec carparkSpec = tabHost.newTabSpec("Car Parks");
        Intent carparksIntent = new Intent(this, CarparkActivity.class);
        carparkSpec.setIndicator("Car Parks", getResources().getDrawable(R.drawable.carpark_tab));
        carparkSpec.setContent(carparksIntent);
        
        //Map Display Tab
        TabSpec mapsSpec = tabHost.newTabSpec("Map");
        Intent mapsIntent = new Intent(this, MapsActivity.class);
        mapsSpec.setIndicator("Map", getResources().getDrawable(R.drawable.maps_tab));
        mapsSpec.setContent(mapsIntent);
        
        //Settings Display Tab
        TabSpec settingsSpec = tabHost.newTabSpec("Options");
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsSpec.setIndicator("Settings", getResources().getDrawable(R.drawable.settings_tab));
        settingsSpec.setContent(settingsIntent);
        
        //Add all my tabs to the TabHost
        tabHost.addTab(carparkSpec);
        tabHost.addTab(mapsSpec);
        tabHost.addTab(settingsSpec);
        
        switcher.showNext(); //Switch the view switcher away from the splash screen
    }
}