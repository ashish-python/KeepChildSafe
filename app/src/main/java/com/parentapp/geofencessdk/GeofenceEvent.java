package com.parentapp.geofencessdk;

public class GeofenceEvent {
    private String geofenceId;
    private String geofenceName;
    private double latitude;
    private double longitude;
    private double accuracy;
    private float speed;
    private double altitude;
    private float bearing;
    private String timestamp;

    public GeofenceEvent(String geofenceId, String geofenceName, double latitude, double longitude, double accuracy, float speed, double altitude, float bearing, String timestamp) {
        this.geofenceId = geofenceId;
        this.geofenceName = geofenceName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.speed = speed;
        this.altitude = altitude;
        this.bearing = bearing;
        this.timestamp = timestamp;
    }

    public String getGeofenceId() {
        return geofenceId;
    }

    public String getGeofenceName() {
        return geofenceName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getBearing() {
        return bearing;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
