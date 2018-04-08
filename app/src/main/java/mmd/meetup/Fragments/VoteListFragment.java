package mmd.meetup.Fragments;

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

import mmd.meetup.Adapters.VoteAdapter;
import mmd.meetup.Models.DataWrapper;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;


public class VoteListFragment extends Fragment {

    OnListFragmentInteractionListener mListener;
    RecyclerView mRecyclerView;
    PendingMeeting mPendingMeeting;
    VoteAdapter mAdapter;

    public VoteListFragment() {
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
            wrapperList.add(new DataWrapper<String>("List of Possible Places"));
            wrapperList.addAll(DataWrapper.wrapObjects(mPendingMeeting.getMeetingPlaces()));
            //assigns index of middle divider (indexing starts at 0)
            int index = wrapperList.size();
            wrapperList.add(new DataWrapper<String>("List of Possible Times"));
            wrapperList.addAll(DataWrapper.wrapObjects(mPendingMeeting.getTimeOptions()));

            mAdapter = new VoteAdapter(wrapperList, mListener, index);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    public interface OnListFragmentInteractionListener {
    }
}
