package mmd.meetup.Models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mickeydang on 2018-03-29.
 */

@IgnoreExtraProperties
public class Meeting implements Serializable{

    private String title;
    private String description;
    private String inviteID;
    private String organizerID;
    private String id;

    //database pulls a hashmap of Strings to "true" not a list of strings. Must be applied manually
    private HashMap<String, String> invitedUsers;

    public Meeting() {
        //required empty constructor
    }

    public HashMap<String, String> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(HashMap<String, String> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInviteID() {
        return inviteID;
    }

    public void setInviteID(String inviteID) {
        this.inviteID = inviteID;
    }

}
