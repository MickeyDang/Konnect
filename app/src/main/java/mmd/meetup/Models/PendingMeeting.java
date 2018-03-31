package mmd.meetup.Models;

import java.util.List;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class PendingMeeting extends Meeting{

    private List<TimeOption> timeOptions;
    private List<String> locationNames;
    private List<String> locationAddresses;

    public PendingMeeting() {
        super();
    }

    public List<TimeOption> getTimeOptions() {
        return timeOptions;
    }

    public void setTimeOptions(List<TimeOption> timeOptions) {
        this.timeOptions = timeOptions;
    }

    public List<String> getLocationNames() {
        return locationNames;
    }

    public void setLocationNames(List<String> locationNames) {
        this.locationNames = locationNames;
    }

    public List<String> getLocationAddresses() {
        return locationAddresses;
    }

    public void setLocationAddresses(List<String> locationAddresses) {
        this.locationAddresses = locationAddresses;
    }
}