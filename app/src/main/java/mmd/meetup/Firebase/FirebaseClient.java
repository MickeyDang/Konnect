package mmd.meetup.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mmd.meetup.Models.Meeting;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class FirebaseClient {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private static FirebaseClient fcInstance;

    public static FirebaseClient getInstance() {
        if (fcInstance == null) {
            fcInstance = new FirebaseClient();
        }

        return fcInstance;
    }

    private FirebaseClient() {
        mAuth = FirebaseAuth.getInstance();
    }

    public String getUserID () {
        return mUser.getUid();
    }

    public void initUser(final GoogleSignInAccount acct, final Callback<Boolean> callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();
                            createUserInDB(acct.getDisplayName());

                            Log.d("test", acct.getDisplayName() + " : "
                            + acct.getFamilyName() + " : " + acct.getGivenName());

                            callback.onResult(true);
                        } else {
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            callback.onResult(false);
                        }
                    }
                });
    }

    private void createUserInDB(final String name) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(mUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null)
                        FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseDB.Users.path)
                                .child(mUser.getUid())
                                .child(FirebaseDB.Users.Entries.name)
                                .setValue(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("test", databaseError.getMessage());
                    }
                });
    }

    public void makeMeeting(Meeting meeting, List<String> invitees) {

        //make meeting
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .push();

        ref.setValue(meeting);

        //add meeting id to all invites
        for (String s : invitees) {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.Users.path)
                    .child(s)
                    .child(FirebaseDB.Users.Entries.pendingMeetings)
                    .child(ref.getKey()).setValue("true");
        }
    }

    public interface Callback<T> {
        void onResult(T t);
    }

}
