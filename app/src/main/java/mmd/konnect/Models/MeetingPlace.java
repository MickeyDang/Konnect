package mmd.konnect.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

/**
 * Created by mickeydang on 2018-04-01.
 */


public class MeetingPlace implements Parcelable, Serializable{

    private String name;
    private String address;
    private double latitude;
    private double longitude;

    @PropertyName("vote_count")
    private int voteCount;

    public MeetingPlace() {

    }

    //read in same order as write
    private MeetingPlace(Parcel parcel) {
        name = parcel.readString();
        address = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
        voteCount = parcel.readInt();
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

    static final Parcelable.Creator<MeetingPlace> CREATOR =  new Parcelable.Creator<MeetingPlace>() {
        @Override
        public MeetingPlace createFromParcel(Parcel parcel) {
            return new MeetingPlace(parcel);
        }

        @Override
        public MeetingPlace[] newArray(int i) {
            return new MeetingPlace[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //write in same order as read
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(voteCount);

    }
}
