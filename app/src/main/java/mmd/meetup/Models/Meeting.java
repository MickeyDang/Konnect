package mmd.meetup.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class Meeting implements Serializable{

    private String title;
    private String description;
    private String inviteID;
    private String organizerID;
    private String id;
    private List<String> invitedUsers;

    public Meeting() {
        //required empty constructor
    }

    public List<String> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(List<String> invitedUsers) {
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
