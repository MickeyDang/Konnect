package mmd.meetup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mmd.meetup.Meeting;
import mmd.meetup.R;

public class MeetingDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private EditText title;
    private EditText descrption;

    public MeetingDetailsFragment() {
        // Required empty public constructor
    }
    public static MeetingDetailsFragment newInstance() {
        MeetingDetailsFragment fragment = new MeetingDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_meeting_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.title);
        descrption = view.findViewById(R.id.description);
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

    public boolean hasNullFields() {
        return (title.getText().toString().isEmpty() || descrption.getText().toString().isEmpty());
    }

    public Meeting getProtoMeeting() {
        String title = this.title.getText().toString();
        String description = this.descrption.getText().toString();

        Meeting meeting = new Meeting();
        meeting.setTitle(title);
        meeting.setDescription(description);
        meeting.setInviteID(Integer.toHexString(meeting.getTitle().hashCode()));
        return meeting;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
