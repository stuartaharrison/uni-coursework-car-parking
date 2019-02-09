/**
 * Defines all the components and methods for the Maps tab. Acts like a fragment on screen and
 * then displays a map based on their location with all the car parks taken from the stream
 * pointed out on the map to allow the application user to see which car park is closest.
 * 
 * @author Stuart Harrison - S0907581
 * @version 1.0
 * @since 03/03/2015
 */

package org.me.myandroidstuff;

import android.app.Activity;
import android.os.Bundle;

public class MapsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapsactivity);
    }
}
