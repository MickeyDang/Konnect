package mmd.meetup;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class Meeting {

    private String title;
    private String description;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //todo google calendar time, and google maps place

}
