package mmd.meetup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mmd.meetup.Models.Meeting;
import mmd.meetup.R;

public class MeetingInviteFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TextView ucv;
    private Meeting meeting;

    private ArrayList<String> invitees = new ArrayList<>();

    public MeetingInviteFragment() {
        // Required empty public constructor
    }

    public static MeetingInviteFragment newInstance(Meeting meeting) {
        MeetingInviteFragment fragment = new MeetingInviteFragment();
        fragment.meeting = meeting;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ucv = view.findViewById(R.id.uniqueCodeView);

        if (meeting != null) {
            ucv.setText(meeting.getInviteID());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ArrayList<String> getInvitees() {
        return invitees;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
