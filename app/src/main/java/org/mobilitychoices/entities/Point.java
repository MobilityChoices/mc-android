package org.mobilitychoices.entities;

public class Point {
    private String address;
    private double latitude;
    private double longitude;

    public Point(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
