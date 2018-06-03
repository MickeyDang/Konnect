package mmd.konnect.Models;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmd.konnect.Constants;
import mmd.konnect.Firebase.FirebaseClient;

public class MeetingBuilder {

    private PendingMeeting meeting;
    private HashMap<String, String> invites = new HashMap<>();

    public void addInvites(List<String> list) {
        invites.put(FirebaseClient.getInstance().getUserID(), "true");

        for (String s : list) {
            invites.put(s, "true");
        }
    }

    public PendingMeeting getMeeting() {
        return meeting;
    }

    public void createInDB() {
        FirebaseClient.getInstance().makePendingMeeting(meeting, invites);
    }

    public PendingMeeting initProtoMeeting(Intent data) {
        meeting = (PendingMeeting) data.getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
        return meeting;
    }

    public PendingMeeting initTimeOptions(Intent data) {
        ArrayList<TimeOption> options = data.getExtras().getParcelableArrayList(Constants.MeetingNavigation.TIME_OPTION_KEY);
        meeting.setTimeOptions(options);
        return meeting;
    }

    public PendingMeeting initPlaceOptions(Intent data) {
        ArrayList<MeetingPlace> places = data.getExtras().getParcelableArrayList(Constants.MeetingNavigation.PLACE_OPTION_KEY);
        meeting.setMeetingPlaces(places);
        return meeting;
    }

}
