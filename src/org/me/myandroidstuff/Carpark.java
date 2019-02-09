/**
 * The Carpark class is a class that stores information about a specific car park.
 * 
 * @author Stuart Harrison - S0907581
 * @version 1.1
 * @since 01/03/2015
 */

package org.me.myandroidstuff;

import java.text.DecimalFormat;

import android.location.Location;

public class Carpark {
	
	private String id;
	private String name;
	private double latitude;
	private double longitude;
	private int capacity;
	private int occupancy;
	private float percentage;
	
	//Todo: Add a return of type Location so that the map api can use it quickly.
	public String getID() { return id; }
	public String getName() { return name; }
	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }
	public int getTotalCapacity() { return capacity; }
	public int getCurrentOccupancy() { return occupancy; }
	public String getOccupancyPercentage() { 
		DecimalFormat df = new DecimalFormat("###"); //I don't want to see a decimal point in my percentages
		return df.format(percentage);
	}
	
	/**
	 * 
	 * Default constructor for the class.
	 * 
	 * @param id = the unique identifier for the car park
	 * @param name = the name of the car park
	 * @param latitude = the latitude coordinate of the car park
	 * @param longitude = the longitude coordinate of the car park
	 * @param capacity = the total number of vehicles the car park can take
	 * @param occupancy = the current number of vehicles using the car park
	 */
	public Carpark(String id, String name, String latitude, String longitude, 
					String occupancy, String capacity) {
		this.id = id;
		this.name = name;
		this.latitude = Double.parseDouble(latitude);
		this.longitude = Double.parseDouble(longitude);
		this.capacity = Integer.parseInt(capacity);
		this.occupancy = Integer.parseInt(occupancy);
		this.percentage = (float)(this.occupancy * 100) / this.capacity;
	}
}
