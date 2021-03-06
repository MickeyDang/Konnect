package mmd.konnect.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Models.PendingMeeting;
import mmd.konnect.R;

public class MeetingDetailsFragment extends Fragment implements NullFieldAsserter {

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

        descrption.setOnEditorActionListener((v, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                if (!hasNullFields()) {
                    mListener.onFieldSubmitted();
                    return true;
                }
            }
            return false;
        });
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

    @Override
    public boolean hasNullFields() {
        if (title.getText().toString().isEmpty()) {
            title.requestFocus();
            title.setError(getString(R.string.toast_empty_field));
            return true;
        } else if (descrption.getText().toString().isEmpty()) {
            descrption.requestFocus();
            descrption.setError(getString(R.string.toast_empty_field));
            return true;
        } else {
            return false;
        }
    }

    public PendingMeeting getProtoMeeting() {
        String title = this.title.getText().toString();
        String description = this.descrption.getText().toString();

        PendingMeeting meeting = new PendingMeeting();
        meeting.setTitle(title);
        meeting.setDescription(description);

        //pseudo-random inviteID generation
        String inviteID = Integer.toHexString((meeting.getDescription().concat(meeting.getTitle())).hashCode());
        inviteID = inviteID.length() > 5 ? inviteID.substring(0, 5) : inviteID;

        meeting.setInviteID(inviteID);
        meeting.setOrganizerID(FirebaseClient.getInstance().getUserID());
        return meeting;
    }

    public interface OnFragmentInteractionListener {
        void onFieldSubmitted();
    }
}
