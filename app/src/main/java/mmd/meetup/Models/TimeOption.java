package mmd.meetup.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class TimeOption implements Parcelable {

    //string stored in SimpleDateTime format
    public Date dataStart;
    public Date dataEnd;
    public String date;
    public String startTime;


    //endTime can be endTime or length of time in minutes as String
    public String endTime;

    public TimeOption() {

    }

    public TimeOption (String date, String startTime, String endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
