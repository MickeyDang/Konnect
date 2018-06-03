package mmd.konnect.Models;


/**
 * Created by mickeydang on 2018-03-31.
 */

public class FinalizedMeeting extends Meeting{

    private TimeOption timeOption;
    private MeetingPlace meetingPlace;

    public FinalizedMeeting() {
        super();
    }

    public TimeOption getTimeOption() {
        return timeOption;
    }

    public void setTimeOption(TimeOption timeOption) {
        this.timeOption = timeOption;
    }

    public MeetingPlace getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(MeetingPlace meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public static FinalizedMeeting createFromPendingMeeting(PendingMeeting pm, TimeOption to, MeetingPlace mp) {

        FinalizedMeeting fm = new FinalizedMeeting();
        fm.timeOption = to;
        fm.meetingPlace = mp;
        fm.setInvitedUsers(pm.getInvitedUsers());
        fm.setDescription(pm.getDescription());

        fm.setId(pm.getId());
        fm.setInviteID(pm.getInviteID());
        fm.setOrganizerID(pm.getOrganizerID());
        fm.setTitle(pm.getTitle());

        return fm;
    }
}
