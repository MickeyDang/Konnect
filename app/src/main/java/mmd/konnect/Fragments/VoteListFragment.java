package mmd.konnect.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mmd.konnect.Adapters.VoteAdapter;
import mmd.konnect.Models.DataWrapper;
import mmd.konnect.Models.MeetingPlace;
import mmd.konnect.Models.PendingMeeting;
import mmd.konnect.Models.TimeOption;
import mmd.konnect.R;


public class VoteListFragment extends Fragment {

    OnVoteSelectedListener mListener;
    RecyclerView mRecyclerView;
    PendingMeeting mPendingMeeting;
    VoteAdapter mAdapter;

    public VoteListFragment() {
        //mandatory empty constructor.
    }

    public static VoteListFragment newInstance(PendingMeeting pm) {
        VoteListFragment fragment = new VoteListFragment();
        fragment.mPendingMeeting = pm;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vote_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            List<DataWrapper> wrapperList = new ArrayList<>();

            //compile list of data wrappers

            //index of first divider always 0
            wrapperList.add(new DataWrapper<String>(getString(R.string.divider_places)));

            if (mPendingMeeting.getMeetingPlaces() != null)
                wrapperList.addAll(DataWrapper.wrapObjects(mPendingMeeting.getMeetingPlaces()));

            //assigns index of middle divider (indexing starts at 0)
            int middleDividerIndex = wrapperList.size();
            wrapperList.add(new DataWrapper<String>(getString(R.string.divider_times)));

            if (mPendingMeeting.getTimeOptions() != null)
                wrapperList.addAll(DataWrapper.wrapObjects(mPendingMeeting.getTimeOptions()));

            mAdapter = new VoteAdapter(wrapperList, mListener, middleDividerIndex);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVoteSelectedListener) {
            mListener = (OnVoteSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVoteSelectedListener {
        void onPlaceRemoved(MeetingPlace mp, int position);

        void onPlaceAdded(MeetingPlace mp, int position);

        void onTimeRemoved(TimeOption to, int position);

        void onTimeAdded(TimeOption to, int position);
    }
}
