package com.example.Pathfinder;

import android.util.Log;
import android.content.Context;

/**
 * Encapsualtion for a point comprised of a latitude and a longitude.
 */
public class WayPoint {

    // the latitude and longitude of the coordinate
    private Double lat;
    private Double lon;

    /**
     * The constructor for the Waypoint
     *
     * @param latitude  the latitude of the coordinate
     * @param longitude the longitude of the coordinate
     */
    public WayPoint(double latitude, double longitude) {

        // sets the longitude and latitude
        lat = latitude;
        lon = longitude;
    }

    /**
     * Turns the Waypoint into string format
     *
     * @return  the lat and lon in String form
     */
    @Override
    public String toString() {
        return "" + lat + ", " + lon;
    }

    /**
     *
     * @return  just the latitude of the coordinate
     */
    public double getLat() {
        return lat;
    }

    /**
     *
     * @return  just the longitude of the coordinate
     */
    public double getLon() {
        return lon;
    }
}
