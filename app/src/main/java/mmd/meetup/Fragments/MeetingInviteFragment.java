package mmd.meetup.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Models.Meeting;
import mmd.meetup.R;

public class MeetingInviteFragment extends Fragment {
    private ShareIntentHandler mListener;

    private TextView ucv;
    private Meeting meeting;
    private FloatingActionButton inviteButton;
    private RecyclerView mRecyclerView;

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
        inviteButton = view.findViewById(R.id.inviteButton);
        mRecyclerView = view.findViewById(R.id.list);

        inviteButton.setOnClickListener(buttonView -> promptForEmail());

        if (meeting != null) {
            ucv.setText(meeting.getInviteID());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShareIntentHandler) {
            mListener = (ShareIntentHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ShareIntentHandler");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void promptForEmail() {
        final View v = View.inflate(getContext(), R.layout.view_find_meeting_dialog, null);
        //change the hint
        ((EditText) v.findViewById(R.id.inviteIdField)).setHint(R.string.hint_find_email);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(v)
                .setTitle(getString(R.string.title_find_people))
                .setPositiveButton(getString(R.string.title_search), ((dialogInterface, i) -> {

                    EditText editText = v.findViewById(R.id.inviteIdField);
                    FirebaseClient.getInstance().findUserByEmail(editText.getText().toString(), user -> {


                        //todo make adapter
                        //todo add logic to update adapter
                        //todo toast

                    });

                }));

        builder.show();

    }

    public ArrayList<String> getInvitees() {
        return invitees;
    }

    public interface ShareIntentHandler {
        void onShareIntent(String inviteID);
    }
}
