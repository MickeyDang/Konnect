package mmd.meetup.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Constants;
import mmd.meetup.Fragments.MeetingDetailsFragment;
import mmd.meetup.Fragments.MeetingInviteFragment;
import mmd.meetup.Meeting;
import mmd.meetup.R;

public class CreateMeetingActivity extends AppCompatActivity implements MeetingDetailsFragment.OnFragmentInteractionListener,
    MeetingInviteFragment.OnFragmentInteractionListener{

    private String currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        getSupportActionBar().setTitle(getString(R.string.title_create_meeting));

        currentStep = getIntent().getExtras().getString(Constants.MeetingNavigation.STEP_KEY);
        currentStep = currentStep == null ? "error" : currentStep;


        if (currentStep.equals(Constants.MeetingNavigation.stepDescription)) {
            goToFragment(MeetingDetailsFragment.newInstance());
        } else if (currentStep.equals(Constants.MeetingNavigation.stepInvite)) {
            Meeting meeting = (Meeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
            goToFragment(MeetingInviteFragment.newInstance(meeting));
        }

    }

    private void goToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_meeting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.next_button:
                if (currentStep.equals(Constants.MeetingNavigation.stepDescription)) {
                    if (assertFieldsNotNull()) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY, getProtoMeeting());
                        intent.putExtras(bundle);
                        setResult(Constants.MeetingNavigation.resultSuccess, intent);
                        finish();
                    }
                } else if (currentStep.equals(Constants.MeetingNavigation.stepInvite)) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(Constants.MeetingNavigation.INVITEE_KEY, getInvitees());
                    setResult(Constants.MeetingNavigation.resultSuccess, intent);
                    finish();
                    //handle appropriately
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Meeting getProtoMeeting() {
        MeetingDetailsFragment detailsFragment = (MeetingDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return detailsFragment.getProtoMeeting();
    }

    private ArrayList<String> getInvitees() {
        MeetingInviteFragment inviteFragment = (MeetingInviteFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return inviteFragment.getInvitees();
    }

    private boolean assertFieldsNotNull() {
        MeetingDetailsFragment detailsFragment = (MeetingDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (detailsFragment.hasNullFields()) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_empty_field), Toast.LENGTH_SHORT)
                    .show();
        }

        return !detailsFragment.hasNullFields();
    }

    @Override
    public void onFragmentInteraction() {

    }
}
