package mmd.meetup.Models;


/**
 * Created by mickeydang on 2018-03-31.
 */

public class FinalizedMeeting extends Meeting{

    private long startTime;
    private long endTime;
    private String locationName;
    private String locationAddress;

    public FinalizedMeeting() {
        super();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
}
