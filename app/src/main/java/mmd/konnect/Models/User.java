package mmd.konnect.Models;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class User implements Serializable{

    private final String DEF_NAME = "User";

    @PropertyName("DisplayName")
    private String name;

    @PropertyName("Email")
    private String email;

    @PropertyName("Uid")
    private String id;

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



    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return id;
    }

    public void setUid(String id) {
        this.id = id;
    }
}
