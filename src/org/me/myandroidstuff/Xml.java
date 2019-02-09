/**
 * The class handles all the XML live stream 'getting' and parsing for the application. This
 * class also contains a few debug methods for testing purposes.
 * 
 * @author Stuart Harrison - S0907581
 * @version 1.2
 * @since 06/03/2015
 */

package org.me.myandroidstuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class Xml {
	
	//Use the debug mode to turn off pulling fresh data from the internet
	private boolean debugMode = false;
	private String xmlSourceString;
	private static final String urlSourceString = "http://open.glasgow.gov.uk/api/live/parking.php?type=xml";
	
	public Xml() { //Constructor 
	}
	
	/**
	 * The main called method for the class. Collects XML as a single string
	 * from the source website and then parses through the information to create
	 * a collection of Carpark objects.
	 * @return returns the parsed data as a collection of Carpark objects
	 */
	public ArrayList<Carpark> getData() throws IOException {
		if (debugMode) 
			return getDataDebug();
		try {
			//I first want to make an attempt to get the data from the data source.
			String xmlSource = GetSourceString();
			//Check it to see if it contains the error tag. Likely to get that tag when a rate limit has been hit
			if (isRateLimit(xmlSource) || xmlSource.isEmpty())
			{
				if (!xmlSourceString.isEmpty()) //contains a parsable value
				{
					return ParseSourceString(xmlSourceString);
				}
				else	
					throw new IOException("No data was gotten");
			}
			else
			{
				xmlSourceString = xmlSource;
				return ParseSourceString(xmlSourceString);
			}
		}
		catch (IOException ex) {
			throw new IOException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private boolean isRateLimit(String source) {
		if(source.contains("<error>")) //Hit rate limit
			return true;
		else
			return false;
	}
	
	/**
	 * Debug method that returns a collection of data for testing the rest of the application without having
	 * to constantly fetch fresh data from the live stream. (No access limit)
	 * @return Returns a collection of Carpark data in an ArrayList. This data is test data and not 'live' data
	 */
 	private ArrayList<Carpark> getDataDebug() {
		ArrayList<Carpark> myData = new ArrayList<Carpark>();
		myData.add(new Carpark("GPG25C", "SECC", "55.85988984843241", "-4.282341367108816", "494", "1600"));
		myData.add(new Carpark("CPG24C", "Duke Street", "55.85966175538049", "-4.236528758151018", "58", "1170"));
		myData.add(new Carpark("CPG21C", "Dundasvale 2", "55.869167760473324", "-4.258813645522479", "62", "85"));
		myData.add(new Carpark("CPG13C", "High Street", "55.85977254007017", "-4.239331652385187", "0", "400")); 
		myData.add(new Carpark("CPG07C", "Charing Cross", "55.864730862024224", "-4.268912534797298", "85", "433")); 
		myData.add(new Carpark("CPG06C", "Cadogan Square", "55.86007562778954", "-4.266947136483616", "2", "325")); 
		myData.add(new Carpark("CPG05C", "Shields Road", "55.850235839806565", "-4.273195914676682", "0", "810")); 
		myData.add(new Carpark("CPG04C", "Buchanan Galleries", "55.86379621893744", "-4.24982359238236", "290", "2000")); 
		myData.add(new Carpark("CPG03C", "Cambridge Street", "55.86639252958139", "-4.258220948854107", "135", "812")); 
		myData.add(new Carpark("CPG02C", "Concert Square", "55.86577732200993", "-4.252559328401379", "355", "698")); 
		myData.add(new Carpark("CPG01C", "Dundasvale 1", "55.867862665358366", "-4.25793867759705", "74", "375")); 
		return myData;
	}
	
	/**
	 * Method taken from the skeleton code for this application. Goes to the URL defined from
	 * the constructor and downloads an XML file as a single string.
	 * @return the xml feed gotten from this method as a string
	 * @throws IOException thrown when a connection cannot be made to the assigned URL
	 */
	private String GetSourceString() throws IOException {
		//Code taken from the Assignment 'skeleton' code application
		String result = "";
		InputStream anInStream = null;
		int response = -1;
		URL url = new URL(urlSourceString);
		URLConnection conn = url.openConnection();	
		// Check that the connection can be opened
		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");
		try {
			// Open connection
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			// Check that connection is Ok
			if (response == HttpURLConnection.HTTP_OK) {
				// Connection is Ok so open a reader 
				anInStream = httpConn.getInputStream();
				InputStreamReader in= new InputStreamReader(anInStream);
				BufferedReader bin= new BufferedReader(in);
				    		
				// Read in the data from the XML stream
				bin.readLine(); // <-- Extra code added from updated version to throw away the header
				String line = new String();
				while (( (line = bin.readLine())) != null) {
					result = result + "\n" + line;
				}
			}
			Log.i("XML", "Got String: " + result);
			return result;
		}
		catch (Exception ex) {
			throw new IOException("Error connecting");
		}
	}
	
	/**
	 * Method for parsing the XML data to create the necessary Carpark objects.
	 * @param source the XML data as a string
	 * @return returns an ArrayList collection of Carpark objects
	 * @throws IOException thrown when the pull parser ecounters an error
	 */
	private ArrayList<Carpark> ParseSourceString(String source) throws IOException {
		ArrayList<Carpark> carParkData = new ArrayList<Carpark>();
		try {
			//Setup the strings I need to store the data from the XML temporarily before building the Carpark Object.
    		String id = "", name = "", latitude = "", longitude = "", occupied = "", capacity = "";
    		//Setup the Pull Parser
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		factory.setNamespaceAware(true);
    		XmlPullParser xpp = factory.newPullParser();
    		xpp.setInput(new StringReader(source));
    		int eventType = xpp.getEventType();
    		//Loop through the XML
    		while (eventType != XmlPullParser.END_DOCUMENT) {
    			//I want to check for an End tag first, I am looking for the 'situation'
    			//this is because each car park in the stream has a start and end tag of 'situation'.
    			if (eventType == XmlPullParser.END_TAG) {
    				if (xpp.getName().equalsIgnoreCase("situation")) {
    					//Build the object and add it to the ArrayList
    					carParkData.add(new Carpark(id, name, latitude, longitude, occupied, capacity));
    					//Since I have already built the object there is no need to continue through this
    					//while loop. I can set the eventType to the next tag and then go back to the top
    					//of the while.
    					eventType = xpp.next();
    					continue;
    				}
    			}
    			if (eventType == XmlPullParser.START_TAG) {
    				if (xpp.getName().equalsIgnoreCase("error")) {
    					//The error tag appears when the Rate Limit has been reached.
    					Log.e("XML", "Rate Limit was reached!");
    					throw new IOException("Rate Limit reached");
    				}
    				else {
    					if (xpp.getName().equalsIgnoreCase("latitude")) {
    						latitude = xpp.nextText();
    						Log.i("XML", "Found the latitude: " + latitude);
    					}
    					else {
    						if (xpp.getName().equalsIgnoreCase("longitude")) {
    							longitude = xpp.nextText();
    							Log.i("XML", "Found the longitude: " + longitude);
    						}
    						else {
    							if (xpp.getName().equalsIgnoreCase("carParkIdentity")) {
    								//I know carParkIdentity tag contains both the ID and Name
    								//of the Car park. So I can split the string and store the
    								//data appropriately. This saves me from checking for another tag.
    								String[] identityStringSplit = xpp.nextText().split(":");
    								name = identityStringSplit[0];
    								id = identityStringSplit[1];
    								Log.i("XML", "Found the carParkIdentity: " + name + " : " + id );
    							}
    							else {
    								if (xpp.getName().equalsIgnoreCase("occupiedSpaces")) {
    									occupied = xpp.nextText();
    									Log.i("XML", "Found the occupiedSpaces: " + occupied);
    								}
    								else {
    									if (xpp.getName().equalsIgnoreCase("totalCapacity")) {
    										capacity = xpp.nextText();
    										Log.i("XML", "Found the totalCapacity: " + capacity);
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    			eventType = xpp.next(); //Important! It moves to the next tag in the XML.
    		}
    		return carParkData;
    	}
    	catch (Exception ex) {
    		throw new IOException("Error parsing");
    	}
	}

}
