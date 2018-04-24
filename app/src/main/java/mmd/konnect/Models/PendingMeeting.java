package mmd.konnect.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mickeydang on 2018-03-31.
 */

public class PendingMeeting extends Meeting implements Serializable {

    private List<TimeOption> timeOptions;
    private List<MeetingPlace> meetingPlaces;

    public PendingMeeting() {
        super();
    }

    public List<TimeOption> getTimeOptions() {
        return timeOptions;
    }

    public void setTimeOptions(ArrayList<TimeOption> timeOptions) {
        this.timeOptions = timeOptions;
    }

    public List<MeetingPlace> getMeetingPlaces() {
        return meetingPlaces;
    }

    public void setMeetingPlaces(List<MeetingPlace> meetingPlaces) {
        this.meetingPlaces = meetingPlaces;
    }
}
