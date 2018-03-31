package mmd.meetup.Models;

import java.util.Date;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class TimeOption {

    //string stored in SimpleDateTime format
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

}
