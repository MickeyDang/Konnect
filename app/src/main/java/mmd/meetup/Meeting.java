package mmd.meetup;

import java.io.Serializable;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class Meeting implements Serializable{

    private String title;
    private String description;
    private String inviteID;

    public Meeting() {

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

    //todo google calendar time, and google maps place

}
