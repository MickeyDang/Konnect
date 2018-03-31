package mmd.meetup.Models;


/**
 * Created by mickeydang on 2018-03-31.
 */

public class FinalizedMeeting extends Meeting{

    private TimeOption timeOption;
    private String locationName;
    private String locationAddress;

    public FinalizedMeeting() {
        super();
    }

    public TimeOption getTimeOption() {
        return timeOption;
    }

    public void setTimeOption(TimeOption timeOption) {
        this.timeOption = timeOption;
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
