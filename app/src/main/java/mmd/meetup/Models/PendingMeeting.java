package mmd.meetup.Models;

import java.util.List;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class PendingMeeting extends Meeting{

    private List<Long> startTimes;
    private List<Long> endTimes;
    private List<String> locationNames;
    private List<String> locationAddresses;

    public PendingMeeting() {
        super();
    }

    public List<Long> getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(List<Long> startTime) {
        this.startTimes = startTime;
    }

    public List<Long> getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(List<Long> endTime) {
        this.endTimes = endTime;
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
