package mmd.meetup.Fragments;

import com.google.firebase.database.ChildEventListener;

/**
 * Created by mickeydang on 2018-03-31.
 */

interface FirebaseListFragment {

    void startListening();
    void stopListening();

}
