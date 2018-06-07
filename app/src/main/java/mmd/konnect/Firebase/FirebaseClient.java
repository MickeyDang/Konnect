package mmd.konnect.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import mmd.konnect.Models.FinalizedMeeting;
import mmd.konnect.Models.MeetingPlace;
import mmd.konnect.Models.PendingMeeting;
import mmd.konnect.Models.TimeOption;
import mmd.konnect.Models.User;

/**
 * Created by mickeydang on 2018-03-29.
 */

public class FirebaseClient {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private FirebaseAuth mAuth;
    private User mUser;

    //singleton implementation
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

    //user logic
    public User getUser() {
        return mUser;
    }

    public String getUserID() {
        return mUser.getId();
    }

    public void signOutUser() {
        mAuth.signOut();
    }

    public void initUser(String email, String password, Callback<Integer> callback) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(loginTask -> {

            if (loginTask.isSuccessful() && mAuth.getCurrentUser() != null) {
                //fetch user if it already exists in database
                fetchUserFromDB(mAuth.getUid(), user -> {
                    mUser = user;
                    callback.onResult(Callback.SUCCESS);
                });
            } else {
                //create user if it does not already exist
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(createTask -> {
                    if (createTask.isSuccessful()) {
                        //check to see if user is now created before proceeding to DB creation
                        if (mAuth.getCurrentUser() != null) {
                            mUser = new User(mAuth.getCurrentUser());
                            createUserInDB();
                            callback.onResult(Callback.SUCCESS);
                        } else {
                            callback.onResult(Callback.AUTH_FAILED);
                        }
                    } else {
                        callback.onResult(Callback.CONNECTION_FAILED);
                    }
                });
            }

        });
    }

    public void initUser(final GoogleSignInAccount acct, final Callback<Boolean> callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser() != null) {

                            //determines if user already created or not. callback is boolean
                            userExistsInDB(mAuth.getUid(), noUserInDB -> {

                                if (noUserInDB) {
                                    //create new user with Auth properties if first time login
                                    mUser = new User(mAuth.getCurrentUser());
                                    createUserInDB();
                                    callback.onResult(true);
                                } else {
                                    //continue with auth if found by fetching data
                                    fetchUserFromDB(mAuth.getUid(), user -> {
                                        mUser = user;
                                        callback.onResult(true);
                                    });
                                }
                            });
                        } else {
                            callback.onResult(false);
                        }
                    } else {
                        Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                        callback.onResult(false);
                    }
                });
    }

    private void fetchUserFromDB(String Uid, Callback<User> callback) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(Uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        callback.onResult(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //todo handle
                    }
                });
    }

    private void userExistsInDB(String Uid, Callback<Boolean> noUserCallback) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(Uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            noUserCallback.onResult(true);
                        } else {
                            noUserCallback.onResult(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void createUserInDB() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(mUser.getId());

        ref.child(FirebaseDB.Users.Entries.name)
                .setValue(mUser.getName());

        ref.child(FirebaseDB.Users.Entries.email)
                .setValue(mUser.getEmail());

        ref.child(FirebaseDB.Users.Entries.id)
                .setValue(mUser.getId());
    }

    public void changeUserName(String name) {
        getUser().setName(name);

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(getUserID())
                .child(FirebaseDB.Users.Entries.name)
                .setValue(name);
    }

    //adds logged in user to meeting
    public void addUserToMeeting(String meetingCode, final Callback<String> callback) {

        //query for meeting with inviteCode equal to meetingCode
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .orderByChild(FirebaseDB.PendingMeetings.Entries.inviteID)
                .equalTo(meetingCode);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //condition returns true if search unsuccessful
                if (dataSnapshot == null) {
                    //returns flag indicating no meeting found
                    callback.onResult(Callback.NULL);
                } else {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {

                        //returns the key of meeting upon success
                        callback.onResult(dataSnapshot.getKey());

                        //add the meeting under user field
                        FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseDB.Users.path)
                                .child(getUserID())
                                .child(FirebaseDB.Users.Entries.pendingMeetings)
                                .child(snap.getKey())
                                .setValue("true");

                        //adds the user under meeting invited list field
                        FirebaseDatabase.getInstance().getReference()
                                .child(FirebaseDB.PendingMeetings.path)
                                .child(snap.getKey())
                                .child(FirebaseDB.PendingMeetings.Entries.invitedUsers)
                                .child(getUserID())
                                .setValue("true");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    //todo add findUserByName and handling for collisions

    //querying for email (a unique field)
    public void findUserByEmail(String email, Callback<User> callback) {

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path);
        query.orderByChild(FirebaseDB.Users.Entries.email).equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null || dataSnapshot.getChildren() == null) {
                    callback.onResult(null);
                    return;
                }

                for (DataSnapshot matchingUser : dataSnapshot.getChildren()) {

                    User user = matchingUser.getValue(User.class);

                    if (user.getId() == null) {
                        user.setId(matchingUser.getKey());
                    }

                    callback.onResult(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //todo implement
    public void updateShortlist(User user) {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(user.getId())
                .child(FirebaseDB.Users.Entries.shortlist)
                .setValue(user.getShortlist());
    }

    //meeting logic
    public void makePendingMeeting(PendingMeeting meeting, HashMap<String, String> invitees) {
        meeting.setInvitedUsers(invitees);

        //make meeting
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .push();

        ref.setValue(meeting);
        ref.child(FirebaseDB.PendingMeetings.Entries.id).setValue(ref.getKey());

        //add meeting id to all invites
        for (String s : invitees.keySet()) {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.Users.path)
                    .child(s)
                    .child(FirebaseDB.Users.Entries.pendingMeetings)
                    .child(ref.getKey()).setValue("true");
        }
    }

    //determines if event is active or not
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

    //user submits vote to database
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
                                0 : Integer.valueOf(String.valueOf(dataSnapshot.getValue())));

                ref.setValue(++count);
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

    //called on button click to resolve a pending meeting
    //todo make this server side logic
    public void resolveVote(final PendingMeeting pendingMeeting, Callback<FinalizedMeeting> callback) {

        //fetches for most recent vote counts
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .child(pendingMeeting.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        MeetingPlace bestPlace = new MeetingPlace();
                        int highestVote = -1;

                        for (DataSnapshot snap : dataSnapshot.child(FirebaseDB.PendingMeetings.Entries.locationOptions).getChildren()) {
                            long holdingValue = snap.child(FirebaseDB.VOTE_COUNT).getValue() == null ? 0 : (long) snap.child(FirebaseDB.VOTE_COUNT).getValue();
                            int count = Integer.valueOf(String.valueOf(holdingValue));
                            if (count > highestVote) {
                                highestVote = count;
                                bestPlace = snap.getValue(MeetingPlace.class);
                            }
                        }

                        TimeOption bestTime = new TimeOption();
                        highestVote = -1;

                        for (DataSnapshot snap : dataSnapshot.child(FirebaseDB.PendingMeetings.Entries.timeOptions).getChildren()) {
                            long holdingValue = snap.child(FirebaseDB.VOTE_COUNT).getValue() == null ? 0 : (long) snap.child(FirebaseDB.VOTE_COUNT).getValue();
                            int count = Integer.valueOf(String.valueOf(holdingValue));
                            if (count > highestVote) {
                                highestVote = count;
                                bestTime = snap.getValue(TimeOption.class);
                            }
                        }

                        FinalizedMeeting fm = FinalizedMeeting
                                .createFromPendingMeeting(pendingMeeting, bestTime, bestPlace);

                        callback.onResult(fm);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    /*this method is called by passing in the created finalized meeting from a resolved pending meeting.
    We assume that finalized meeting will always have a list of invited users from pending meeting*/
    public void makeFinalizedMeeting(FinalizedMeeting
                                             finalizedMeeting, Callback<Boolean> callback) {

        Queue<Boolean> requestQueue = new LinkedList<>();

        //make meeting
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.FinalizedMeetings.path)
                .child(finalizedMeeting.getId());

        //add meeting id to all invites
        for (String s : finalizedMeeting.getInvitedUsers().keySet()) {
            requestQueue.add(true);
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.Users.path)
                    .child(s)
                    .child(FirebaseDB.Users.Entries.finalizedMeetings)
                    .child(finalizedMeeting.getId()).setValue("true").addOnCompleteListener(task -> {
                requestQueue.poll();
                if (requestQueue.isEmpty())
                    callback.onResult(true);

            });
        }

        requestQueue.add(true);
        ref.setValue(finalizedMeeting).addOnCompleteListener(task -> {
            requestQueue.poll();
            if (requestQueue.isEmpty())
                callback.onResult(true);
        });
    }

    //called immediately after finalized meeting created and added
    public void deletePendingMeetingTrace(PendingMeeting meeting) {

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.PendingMeetings.path)
                .child(meeting.getId())
                .setValue(null);

        for (String s : meeting.getInvitedUsers().keySet()) {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.Users.path)
                    .child(s)
                    .child(FirebaseDB.Users.Entries.pendingMeetings)
                    .child(meeting.getId()).setValue(null);
        }
    }

    public interface Callback<T> {
        String NULL = "request_nothing";
        int SUCCESS = 101;
        int CONNECTION_FAILED = 102;
        int AUTH_FAILED = 103;

        void onResult(T t);
    }

}
