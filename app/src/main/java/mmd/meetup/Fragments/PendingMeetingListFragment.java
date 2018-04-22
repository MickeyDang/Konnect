package mmd.meetup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.List;

import mmd.meetup.Adapters.PendingMeetingAdapter;
import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Firebase.FirebaseDB;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;


public class PendingMeetingListFragment extends Fragment implements FirebaseListFragment{

    PendingMeetingInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private PendingMeetingAdapter mAdapter;
    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if (mAdapter.containsItem(dataSnapshot.getKey()) == -1)
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseDB.PendingMeetings.path)
                    .child(dataSnapshot.getKey())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            PendingMeeting meeting = dataSnapshot.getValue(PendingMeeting.class);
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
            int deleteIndex = mAdapter.containsItem(dataSnapshot.getKey());
            if (deleteIndex != -1) {
                mAdapter.onDelete(deleteIndex);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public PendingMeetingListFragment() {

    }

    public static PendingMeetingListFragment newInstance(int columnCount) {
        PendingMeetingListFragment fragment = new PendingMeetingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendingmeeting_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mAdapter = new PendingMeetingAdapter(mListener);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PendingMeetingInteractionListener) {
            mListener = (PendingMeetingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PendingMeetingInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public void notifyAdapterVoteCast(String id) {
        if (id == null || id.isEmpty()) {
            mAdapter.notifyDataSetChanged();
        } else {
            int index = findElementIndex(id, mAdapter.getFullList());
            if (index != -1) {
                mAdapter.notifyItemChanged(index);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private int findElementIndex(String id, List<PendingMeeting> pendingMeetings) {
        for (int i = 0; i < pendingMeetings.size(); i++) {
            if (id.equals(pendingMeetings.get(i).getId()))
                return i;
        }

        return -1;
    }

    @Override
    public void startListening() {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(FirebaseClient.getInstance().getUserID())
                .child(FirebaseDB.Users.Entries.pendingMeetings)
                .addChildEventListener(mChildEventListener);
    }

    @Override
    public void stopListening() {
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseDB.Users.path)
                .child(FirebaseClient.getInstance().getUserID())
                .child(FirebaseDB.Users.Entries.pendingMeetings)
                .removeEventListener(mChildEventListener);
    }

    public interface PendingMeetingInteractionListener {
        void onCastVote(PendingMeeting pm);
        void onResolveVote(PendingMeeting pm);
    }
}
