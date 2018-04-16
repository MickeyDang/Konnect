package mmd.meetup.Models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class TimeOption implements Parcelable, Serializable {

    private String date;
    private String startTime;
    public String endTime;
    private long startTimeMillis;

    @PropertyName("vote_count")
    private int voteCount;

    public TimeOption() {

    }

    //read in same order as writing data
    private TimeOption(Parcel parcel) {

        date = parcel.readString();
        startTime = parcel.readString();
        endTime = parcel.readString();
        startTimeMillis = parcel.readLong();
        voteCount = parcel.readInt();
    }

    public TimeOption (String date, String startTime, String endTime, long startTimeMillis) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startTimeMillis = startTimeMillis;
        voteCount = 0;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long l) {
        startTimeMillis = l;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static final Parcelable.Creator<TimeOption> CREATOR
            = new Parcelable.Creator<TimeOption>() {
        @Override
        public TimeOption createFromParcel(Parcel parcel) {
            return new TimeOption(parcel);
        }

        @Override
        public TimeOption[] newArray(int i) {
            return new TimeOption[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //write in same order as reading data
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(date);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeLong(startTimeMillis);
        parcel.writeInt(voteCount);
    }

}
