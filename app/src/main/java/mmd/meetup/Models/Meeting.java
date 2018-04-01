package mmd.meetup.Models;

import java.io.Serializable;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class Meeting implements Serializable{

    private String title;
    private String description;
    private String inviteID;
    private String organizerID;
    private String id;

    public Meeting() {
        //required empty constructor
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
