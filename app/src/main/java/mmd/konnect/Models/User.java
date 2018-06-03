package mmd.konnect.Models;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{

    private final String DEF_NAME = "User";

    @PropertyName("name")
    private String name;

    @PropertyName("email")
    private String email;

    @PropertyName("id")
    private String id;

    @PropertyName("shortlist")
    private List<String> shortlist = new ArrayList<>();

    public User() {

    }

    public User(FirebaseUser fUser) {
        this.name = fUser.getDisplayName();
        this.email = fUser.getEmail();
        this.id = fUser.getUid();

        if (name == null) {
            if (email != null) {
                String[] emailParts = email.split("@");
                if (emailParts.length > 0) {
                    name = emailParts[0];
                } else {
                    name = email;
                }
            } else {
                name = DEF_NAME;
            }
        }
    }

    public List<String> getShortlist() {


        return shortlist;
    }

    public void setShortlist(List<String> shortlist) {
        this.shortlist = shortlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public boolean addToShortList(String s) {
        if (shortlist.contains(s) || shortlist.size() > 5) {
            return false;
        } else {
            shortlist.add(s);
            return true;
        }
    }

}
