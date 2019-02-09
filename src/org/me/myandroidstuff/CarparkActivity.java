/**
 * Defines all the components and methods for the Carpark tab. Acts like a fragment on screen and
 * will update every 'x' minutes with fresh car park information.
 * 
 * @author Stuart Harrison - S0907581
 * @version 1.0
 * @since 03/03/2015
 */

package org.me.myandroidstuff;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class CarparkActivity extends Activity {
	
	//Define my graphical components from the XML layout
	public static LinearLayout buttonLayout;
	public static ShowDialogAsyncTask tasker;
	
	//Define some control variables
	public static boolean loadedData = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carparkactivity);
        //Setup my UI Objects
        buttonLayout = (LinearLayout)findViewById(R.id.scrollButtons);
        //Setup data from settings file
        
        //Begin handling of the AsyncTask to handle auto updating the car park data
        tasker = new ShowDialogAsyncTask();
    	tasker.execute();
    }
	
	@Override
	public void onResume() {
		super.onResume();
		while(!loadedData) { }
	}
	
	/**
	 * Method is called after a collection of car park objects has been successfully pulled from
	 * the stream. Displays each object as a button with it's own onClick() function which will
	 * display a dialog to the user displaying the information about that car park.
	 * @param data The arraylist of car park objects to be displayed on screen
	 */
	@SuppressWarnings("unchecked")
	public void CreateButtons(ArrayList<Carpark> data) {
		//Loop around for the size of the array. (this means car parks can be removed and added
		//and the application should no crash out)
    	for(int i = 0; i < data.size(); i++) {
    		//I want the percentage as an integer now so that my other method can easily calculate a colour.
    		int percent = Integer.parseInt(data.get(i).getOccupancyPercentage());
    		Button newButton = new Button(this); //Setup a new button.
    		newButton.setTextColor(getColourForButtons(percent)); //Get the text colour for this button.
    		newButton.setText(data.get(i).getName() + " | " + 
    							percent + "%"); //Give it this temporary name for now.
    		newButton.setTag(data.get(i));
    		//Setup the position of the button on screen and then set its onclicklistener.
    		newButton.setLayoutParams(new LayoutParams(
    				ViewGroup.LayoutParams.MATCH_PARENT,
    				ViewGroup.LayoutParams.WRAP_CONTENT));
    		newButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					DisplayDataDialog((Carpark)v.getTag());
				}
			});
    		//Append this new button to the button layout (LinearLayout) as a child view.
    		buttonLayout.addView(newButton); 
    	}
    }
	
	/**
	 * Method that is called as a buttons onclick(). Displays a Dialog to the application user
	 * with the relevant car park information based on which button the user clicked/tapped.
	 * @param theData The CarPark object that is attached to the button being clicked/tapped
	 */
    public void DisplayDataDialog(Carpark theData) {
    	//Setup the dialog
    	final Dialog dialog = new Dialog(this);
    	dialog.setContentView(R.layout.dialog_parkinfo);
    	dialog.setTitle(theData.getName() + " Information");
    	
    	//Setup the view components for the dialog
    	Button dialogButton = (Button)dialog.findViewById(R.id.idialogButtonOK);
    	TextView spaces = (TextView)dialog.findViewById(R.id.txtbiSpaces);
    	TextView percent = (TextView)dialog.findViewById(R.id.txtbiPercentage);
    	
    	//Set the text components with the data in the Carpark object and the onclicklistener for the button.
    	spaces.setText("Occupied Spaces: " + theData.getCurrentOccupancy() + "/" + theData.getTotalCapacity());
    	percent.setText("That's about " + theData.getOccupancyPercentage() + "% full!");
    	dialogButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss(); //Close the dialog
			}
		});
    	dialog.show(); //Show the dialog
    }
    
    /**
     * Call when an exception is thrown. It's purpose is to add a child textview
     * component to the button's linear layout to display a friendly message
     * to the application user.
     * @param errorString The error message from the thrown exception
     */
    public void DisplayErrorMessage(String errorString) {
    	//Setup a new TextView component
    	TextView txtbError = new TextView(this);
    	//Assign some data to the view
    	txtbError.setTextColor(Color.RED);
    	txtbError.setText(errorString);
    	txtbError.setLayoutParams(new LayoutParams(
    			ViewGroup.LayoutParams.MATCH_PARENT,
    			ViewGroup.LayoutParams.WRAP_CONTENT));
    	buttonLayout.addView(txtbError); //Add the view to the button layout
    }
    
    /**
     * Basic method that will return a colour depending on how full a particular car
     * park is.
     * @param percentage the percentage value of how full the car park is
     * @return returns a colour value as an integer so that the setTextColor function understands
     */
    public int getColourForButtons(int percentage) { //Change the name :(
    	if (percentage <= 25) { //Green
    		return Color.GREEN;
    	}
    	if (percentage > 25 && percentage < 75) { //amber
    		return Color.MAGENTA;
    	}
    	if (percentage >= 75) { //red
    		return Color.RED;
    	}
    	else { //Unknown number?
    		return Color.BLACK; 
    	}
    }
    
    private class ShowDialogAsyncTask extends AsyncTask<Void, ArrayList<Carpark>, Void> {

    	private ArrayList<Carpark> carParkData;
    	private Xml xmlDataParser;
    	
    	public boolean cancelOperation = false;
    	
    	@Override
        protected void onPreExecute() {
    		super.onPreExecute();
    		carParkData = new ArrayList<Carpark>();
    		xmlDataParser = new Xml();
        }
         
        @SuppressWarnings("unchecked")
		@Override
        protected Void doInBackground(Void... params) {
        	while(!cancelOperation) {
        		try {
        			carParkData = xmlDataParser.getData(); 
       		 	}
       		 	catch (Exception ex) {
       		 		carParkData = new ArrayList<Carpark>();
       		 	}
       		 	publishProgress(carParkData);
       		 	SystemClock.sleep(150000);
       	 	}
       	 	return null;
        }
     
        @Override
        protected void onProgressUpdate(ArrayList<Carpark>... values) {
        	if(buttonLayout.getChildCount() > 0)
  				 buttonLayout.removeAllViews();
        	if(!values[0].isEmpty())
        		CreateButtons(values[0]);
        	else {
        		DisplayErrorMessage("You have reached the rate limit. Please wait for the 2.5 minute auto-update.");
        	}
        	loadedData = true;
        }
      
        @Override
        protected void onPostExecute(Void result) {
        	super.onPostExecute(result);
        }
    
    }
}
