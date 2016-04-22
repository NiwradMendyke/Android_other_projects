package com.example.Pathfinder;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class represents the route object.
 * A route is essentially an arrayList of GPS coordinates (called Waypoints).
 * Also includes the associated information, such as:
 * various objects needed for the GPS
 * the time
 * the distance traveled
 * and the name of the route
 */
public class Route implements LocationListener {

    private Context context; // collected from the Main class, needed for the GPS and toast statements

    // holder variables for the current current latitude and longitude so that they may be added to the arrayList of Waypoints
    private double currentLat;
    private double currentLon;

    private Long startTime; // used to calculate the total time of the route
    //private Long timeUsed;

    private String name; // name of the route

    private double traveledDistance = 0; // the total distance the route requires

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 30; // 1 minute

    private ArrayList<WayPoint> points = new ArrayList<WayPoint>(); // the list of coordinates that comprise the route

    private ArrayList<Double> times = new ArrayList<Double>(); // the list of times required to travel the route

    /**
     * The constructor for the route class.
     * Creates and initializes many of the variables needed for the GPS.
     * Records the first GPS point, then starts recording.
     *
     * @param theContext  passed from the main class
     */
    public Route(Context theContext) {

        Log.d("Route", "A new route has been created");

        context = theContext;

        // needed for the GPS to function
        Criteria criteria = new Criteria();
        String provider = Main.manager.getBestProvider(criteria, false);

        Main.manager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        Location location = Main.manager.getLastKnownLocation(provider); // gets the initial location

        // uses the current location to determine current coordinates
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();

        points.add(new WayPoint(currentLat, currentLon)); // creates a new Waypoint with the coordinates and adds the point to the list

        startTime = System.currentTimeMillis(); // sets the starting time for the recording of the route
    }

    /**
     * A second constructor for the route class.
     * Used for when reading a route from text files and creating a temporary route either for screen output or comparison.
     *
     * @param theContext    passed from the main class
     * @param pointsInRoute the list of coordinates in the route
     * @param routeName     the name of the route
     * @param distance      distance the route covers
     * @param time          list of times required to traverse the route
     */
    public Route(Context theContext, ArrayList<WayPoint> pointsInRoute, String routeName, double distance, ArrayList<Double> time) {
        // sets all the passed parameters to the essential variables of the class
        context = theContext;
        points = pointsInRoute;
        name = routeName;
        times = time;
        traveledDistance = distance;
    }

    /**
     * Define the callback method that receives location updates.
     * Records and updates the coordiantes in the route.
     *
     * @param location  the current location
     */
    @Override
    public void onLocationChanged(Location location) {

        if (Main.isRecording) {

            // determines the current latitude and longitude
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();

            // determines the distance since since last coordinate recording, and adds distance to the total traveled distance
            double distanceFromLastPoint = calcDistance(points.get(points.size()-1), 'K');
            traveledDistance += distanceFromLastPoint;

            points.add(new WayPoint(currentLat, currentLon)); // uses lat and lon to create new Waypoint in the list
        }
    }

    /**
     * Required method of the LocationListener interface, but not in use by program.
     *
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    /**
     * Required method of the LocationListener interface, but not in use by program.
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    /**
     * Required method of the LocationListener interface, but not in use by program.
     *
     * @param provider
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    /**
     * Used to determine the distance between this route and another passed route.
     *
     * @param point   the route to be used for comparison
     * @param unit    char indicating whether to use miles or kilometers
     * @return        the distance value in the form of a double
     */
    public double calcDistance(WayPoint point, char unit) {
        double lon1 = point.getLon();
        double lat1 = point.getLat();
        double lon2 = currentLon;
        double lat2 = currentLat;
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1609.344;         //changes unit of distance K = kilometers, N = nautical miles
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return dist;
    }

    /**
     * Used to determine the distance between two routes.
     *
     * @param point   one of the routes to check with
     * @param point2  the second of the routes to check with
     * @return        the distance value in the form of a double
     */
    public double calcDistance(WayPoint point, WayPoint point2) {
        double lon1 = point.getLon();
        double lat1 = point.getLat();
        double lon2 = point2.getLon();
        double lat2 = point2.getLat();
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        /*if (unit == 'K') {
            dist = dist * 1.609344;         //changes unit of distance K = kilometers, N = nautical miles
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }*/
        return dist;
    }

    // returns degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // returns radians to degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /**
     * Used to output the information regarding the route in an easily-readable format on the screen.
     *
     * @return  The route information in String format
     */
    public String toString() {
        String routeString = "";

        if (name == null) {
            if (points.size() > 1)
                routeString += "Route " + ":\n" + points.get(0).toString() + "\n" + points.get(points.size() - 1).toString() + "\n" + getDistanceUsed() + "\n" + getTimeUsed();
            else
                routeString += "Route " + ":\n" + points.get(0).toString() + "\n" + getDistanceUsed() + "\n" + getTimeUsed();
        }
        else {
            if (points.size() > 1)
                routeString += "Route " + name + ":\n" + points.get(0).toString() + "\n" + points.get(points.size() - 1).toString() + "\n" + getDistanceUsed() + "\n" + getTimeUsed();
            else
                routeString += "Route " + name + ":\n" + points.get(0).toString() + "\n" + getDistanceUsed() + "\n" + getTimeUsed();
        }

        return routeString;
    }

    /**
     * Determines the average time required to traverse the route.
     *
     * @return  the average time in hours, minutes, and seconds
     */
    public String getTimeUsed() {
        double seconds = getAvgTime() / 1000;
        int minutes;

        if ((seconds / 60) >= 1) { // determines number of hours and minutes
            minutes = (int)(seconds / 60);
            seconds %= 60;

            return "Average of " + minutes + " minutes and " + (int)seconds + " seconds";
        }

        return "Average of " + (int)seconds + " seconds";
    }

    /**
     *
     * @return    the distance traveled in string form
     */
    public String getDistanceUsed() {
        String distance = "" + traveledDistance;

        if (distance.length() > 4)
            return "You traveled " + distance.substring(0, 5) + " meters";
        else
            return "You traveled " + distance + " meters";
    }

    /**
     *
     * @return    the distance traveled in double form
     */
    public double getDistance() {
        return traveledDistance;
    }

    /**
     * Calculates the average time for the route.
     *
     * @return    the average time
     */
    public double getAvgTime() {
        double sum = 0;

        for (double d : times)
            sum += d;

        sum /= times.size();
        Log.d("Route", "AvgTime is " + sum);

        return sum;
    }

    /**
     * Ends the time count for the route and sets the total time required.
     */
    public void setEndTime() {
        Long timeUsed = System.currentTimeMillis() - startTime;
        Log.d("Route", "setting the endTime " + timeUsed);
        times.add((double)timeUsed);
    }

    /**
     *
     * @return    the list of coordinates in the route
     */
    public ArrayList<WayPoint> getPoints() {
        return points;
    }

    /**
     *
     * @return    the list of times for the route
     */
    public ArrayList<Double> getTimes() {
        Log.d("Route", "GetTimes was called");
        return times;
    }

    /**
     * Sets the name for a temporary route.
     *
     * @param givenName   The new name for the route
     */
    public void setName(String givenName) {
        name = givenName;
    }
}
