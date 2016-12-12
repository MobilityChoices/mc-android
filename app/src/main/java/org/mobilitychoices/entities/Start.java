package org.mobilitychoices.entities;

public class Start {
    private String address;
    private double latitude;
    private double longitude;

    public Start(String a, double lat, double lng) {
        address = a;
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
