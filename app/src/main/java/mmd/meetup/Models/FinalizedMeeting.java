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

    public static FinalizedMeeting makeFinalizedMeeting(PendingMeeting pm, TimeOption to, MeetingPlace mp) {

        FinalizedMeeting fm = new FinalizedMeeting();
        fm.timeOption = to;
        fm.locationName = mp.getName();
        fm.locationAddress = mp.getAddress();
        fm.setInvitedUsers(pm.getInvitedUsers());
        fm.setDescription(pm.getDescription());
        fm.setId(pm.getId());
        fm.setInviteID(pm.getInviteID());
        fm.setOrganizerID(pm.getOrganizerID());
        fm.setTitle(pm.getTitle());

        return fm;
    }
}
