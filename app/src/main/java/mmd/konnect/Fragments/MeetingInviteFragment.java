package mmd.konnect.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mmd.konnect.Adapters.InvitedUserAdapter;
import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Models.Meeting;
import mmd.konnect.Models.User;
import mmd.konnect.R;

public class MeetingInviteFragment extends Fragment {
    private ShareIntentHandler mListener;

    private TextView ucv;
    private Meeting meeting;
    private FloatingActionButton inviteButton;
    private RecyclerView mRecyclerView;
    private InvitedUserAdapter mAdapter;

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new InvitedUserAdapter(mListener);
        mRecyclerView.setAdapter(mAdapter);

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

                        if (user == null) {
                            Toast.makeText(getContext(), getString(R.string.toast_no_user_added), Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            mAdapter.insertUser(user);
                            Toast.makeText(getContext(), getString(R.string.toast_user_added), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }));

        builder.show();
    }

    public ArrayList<String> getInvitees() {
        ArrayList<String> list = new ArrayList<>();
        for (User user : mAdapter.getUserList()) {
            list.add(user.getId());
        }
        return list;
    }

    public interface ShareIntentHandler {
        void onShareIntent(String inviteID);
    }
}
