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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.Models.TimeOption;

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

    public void signOutUser() {
        mAuth.signOut();
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

    public void addUserToMeeting(String meetingCode, final Callback<String> callback) {

        //query for meeting with inviteCode equal to meetingCode
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .orderByChild(FirebaseDB.PendingMeetings.Entries.inviteID)
                .equalTo(meetingCode);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //condition returns true is search unsuccessful
                if (dataSnapshot == null) {
                    //handle appropriately in view
                    callback.onResult(Callback.NULL);
                } else {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        //handle appropriately in view
                        callback.onResult(dataSnapshot.getKey());
                        //add the meeting under user field
                        FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseDB.Users.path)
                                .child(getUserID())
                                .child(FirebaseDB.Users.Entries.pendingMeetings)
                                .child(snap.getKey())
                                .setValue("true");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void makeMeeting(PendingMeeting meeting, List<String> invitees) {

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

    public void isOwnerOfVote(String voteID, final Callback<Boolean> callback) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .child(voteID)
                .child(FirebaseDB.PendingMeetings.Entries.organizerID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = (String) dataSnapshot.getValue();
                        s = (s == null ? "false" : s);
                        callback.onResult(s.equals(getUserID()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getVoteStatus(String voteID, final Callback<Boolean> callback) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(getUserID())
                .child(FirebaseDB.Users.Entries.pendingMeetings)
                .child(voteID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String s = (String) dataSnapshot.getValue();
                        s = (s == null ? "false" : s);
                        //returns true indicates that the user's vote is not submitted and active
                        callback.onResult(s.equalsIgnoreCase("true"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void incrimentVoteCount(String meetingID, String optionTypePath, String indexPath) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .child(meetingID)
                .child(optionTypePath) /*specifies whether it is place or time vote*/
                .child(indexPath)
                .child(FirebaseDB.VOTE_COUNT);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int count =
                                (dataSnapshot == null || dataSnapshot.getValue() == null ?
                                        0 : (int) dataSnapshot.getValue());

                        ref.setValue(++count);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void resolveVote(String pendingMeetingID, Callback<FinalizedMeeting> callback) {

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .child(pendingMeetingID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        MeetingPlace bestPlace = new MeetingPlace();
                        int highestVote = -1;

                        for (DataSnapshot snap : dataSnapshot.child(FirebaseDB.PendingMeetings.Entries.locationOptions).getChildren()) {
                            int count = (int) snap.child(FirebaseDB.VOTE_COUNT).getValue();
                            if (count > highestVote) {
                                highestVote = count;
                                bestPlace = snap.getValue(MeetingPlace.class);
                            }
                        }

                        TimeOption bestTime = new TimeOption();
                        highestVote = -1;

                        for (DataSnapshot snap : dataSnapshot.child(FirebaseDB.PendingMeetings.Entries.timeOptions).getChildren()) {
                            int count = (int) snap.child(FirebaseDB.VOTE_COUNT).getValue();
                            if (count > highestVote) {
                                highestVote = count;
                                bestTime = snap.getValue(TimeOption.class);
                            }
                        }

                        PendingMeeting pm = dataSnapshot.getValue(PendingMeeting.class);

                        if (pm != null) {
                            FinalizedMeeting fm = FinalizedMeeting
                                    .makeFinalizedMeeting(pm, bestTime, bestPlace);
                            callback.onResult(fm);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    //called after incrementing vote
    public void disableVotingStatus(String meetingID) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(getUserID())
                .child(FirebaseDB.Users.Entries.pendingMeetings)
                .child(meetingID)
                .setValue("false");
    }

    public interface Callback<T> {
        String NULL = "request_nothing";

        void onResult(T t);
    }

}
