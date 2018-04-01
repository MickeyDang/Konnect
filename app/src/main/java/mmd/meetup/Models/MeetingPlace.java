package mmd.meetup.Models;

import java.io.Serializable;

/**
 * Created by mickeydang on 2018-04-01.
 */

public class MeetingPlace implements Serializable{

    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public MeetingPlace() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
