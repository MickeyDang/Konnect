package mmd.konnect.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mmd.konnect.Adapters.FinalizedMeetingAdapter;
import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Firebase.FirebaseDB;
import mmd.konnect.Models.FinalizedMeeting;
import mmd.konnect.R;


public class FinalizedMeetingListFragment extends Fragment implements FirebaseListFragment {

    private int mColumnCount = 1;
    private MeetingInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private FinalizedMeetingAdapter mAdapter;

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if (mAdapter.containsItem(dataSnapshot.getKey()) == -1)
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseDB.FinalizedMeetings.path)
                        .child(dataSnapshot.getKey())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                FinalizedMeeting meeting = dataSnapshot.getValue(FinalizedMeeting.class);
                                meeting.setId(dataSnapshot.getKey());
                                mAdapter.onInsert(meeting);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public FinalizedMeetingListFragment() {
    }

    public static FinalizedMeetingListFragment newInstance() {
        FinalizedMeetingListFragment fragment = new FinalizedMeetingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finalizedmeeting_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new FinalizedMeetingAdapter(mListener);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MeetingInteractionListener) {
            mListener = (MeetingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MeetingInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void startListening() {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(FirebaseClient.getInstance().getUserID())
                .child(FirebaseDB.Users.Entries.finalizedMeetings)
                .addChildEventListener(mChildEventListener);
    }

    @Override
    public void stopListening() {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(FirebaseClient.getInstance().getUserID())
                .child(FirebaseDB.Users.Entries.finalizedMeetings)
                .removeEventListener(mChildEventListener);
    }

    public interface MeetingInteractionListener {
        void onFinalizedMeetingSelected(FinalizedMeeting fm);
    }
}
