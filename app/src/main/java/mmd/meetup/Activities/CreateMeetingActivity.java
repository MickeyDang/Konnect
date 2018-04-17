package mmd.meetup.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import mmd.meetup.Constants;
import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Fragments.MeetingDetailsFragment;
import mmd.meetup.Fragments.MeetingInviteFragment;
import mmd.meetup.Fragments.MeetingPlaceFragment;
import mmd.meetup.Fragments.MeetingTimeFragment;
import mmd.meetup.Fragments.NullFieldAsserter;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

public class CreateMeetingActivity extends AppCompatActivity implements MeetingDetailsFragment.OnFragmentInteractionListener,
        MeetingInviteFragment.ShareIntentHandler, MeetingTimeFragment.OnTimeOptionInteractionListener,
        MeetingPlaceFragment.PlacePickerHandler{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final int PICKER_RC = 99;
    private final int PERMISSION_RC = 98;

    private String currentStep;
    private Menu mMenu;

    //this meeting will not be fully initialized at all times.
    private Meeting protoMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        getSupportActionBar().setTitle(getString(R.string.title_create_meeting));

        currentStep = getIntent().getExtras().getString(Constants.MeetingNavigation.STEP_KEY);
        currentStep = currentStep == null ? "error" : currentStep;

        if (currentStep.equals(Constants.MeetingNavigation.stepDescription)) {
            goToFragment(MeetingDetailsFragment.newInstance());
            changeToolbarText(getString(R.string.title_description));
        } else if (currentStep.equals(Constants.MeetingNavigation.stepTime)) {
            protoMeeting = (Meeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
            goToFragment(MeetingTimeFragment.newInstance(protoMeeting));
            changeToolbarText(getString(R.string.title_time));
        } else if (currentStep.equals(Constants.MeetingNavigation.stepLocation)) {
            protoMeeting = (Meeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
            goToFragment(MeetingPlaceFragment.newInstance(protoMeeting));
            changeToolbarText(getString(R.string.title_place));
        } else if (currentStep.equals(Constants.MeetingNavigation.stepInvite)) {
            protoMeeting = (Meeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
            goToFragment(MeetingInviteFragment.newInstance(protoMeeting));
            changeToolbarText(getString(R.string.title_invite));
        }
    }

    private void changeToolbarText(String s) {
        getSupportActionBar().setTitle(s);
    }

    private void configureMenu() {
        if (currentStep.equals(Constants.MeetingNavigation.stepInvite)) {
            mMenu.findItem(R.id.share_button).setVisible(true);
        } else {
            mMenu.findItem(R.id.share_button).setVisible(false);
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
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_meeting_menu, menu);
        configureMenu();
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
                        this.finish();
                    }

                } else if (currentStep.equals(Constants.MeetingNavigation.stepTime)) {
                    if (assertFieldsNotNull()) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.MeetingNavigation.TIME_OPTION_KEY, getTimeOptions());
                        intent.putExtras(bundle);
                        setResult(Constants.MeetingNavigation.resultSuccess, intent);
                        this.finish();
                    }
                } else if (currentStep.equals(Constants.MeetingNavigation.stepLocation)) {
                    if (assertFieldsNotNull()) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.MeetingNavigation.PLACE_OPTION_KEY, getMeetingPlaceList());
                        intent.putExtras(bundle);
                        setResult(Constants.MeetingNavigation.resultSuccess, intent);
                        this.finish();
                    }
                } else if (currentStep.equals(Constants.MeetingNavigation.stepInvite)) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(Constants.MeetingNavigation.INVITEE_KEY, getInvitees());
                    setResult(Constants.MeetingNavigation.resultSuccess, intent);
                    this.finish();
                }
                return true;
            case R.id.share_button:
                if (protoMeeting != null || protoMeeting.getInviteID() != null)
                    onShareIntent(protoMeeting.getInviteID());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<TimeOption> getTimeOptions() {
        MeetingTimeFragment fragment = (MeetingTimeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment.getTimeOptions();
    }

    private PendingMeeting getProtoMeeting() {
        MeetingDetailsFragment detailsFragment = (MeetingDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return detailsFragment.getProtoMeeting();
    }

    private ArrayList<String> getInvitees() {
        MeetingInviteFragment inviteFragment = (MeetingInviteFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return inviteFragment.getInvitees();
    }

    private boolean assertFieldsNotNull() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment instanceof MeetingDetailsFragment) {
            return !evaluateIfNull((MeetingDetailsFragment) fragment);
        } else if (fragment instanceof MeetingTimeFragment) {
            return !evaluateIfNull((MeetingTimeFragment) fragment);
        } else if (fragment instanceof MeetingPlaceFragment) {
            return !evaluateIfNull((MeetingPlaceFragment) fragment);
        } else {
            //this case should be impossible. unhandled
            return false;
        }
    }

    private boolean evaluateIfNull(NullFieldAsserter nfa) {
        boolean hasNullFields = nfa.hasNullFields();
        if (hasNullFields) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_empty_field), Toast.LENGTH_SHORT)
                    .show();
        }
        return hasNullFields;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKER_RC :
                //default ok result constant
                if (resultCode == RESULT_OK) {
                    handlePlaceOption(PlacePicker.getPlace(this, data));
                }
        }
    }

    @Override
    public void makePlacePicker() {
        startPlacePickerWidget(null);
    }

    private void startPlacePickerWidget(@Nullable LatLngBounds bounds) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            if (bounds != null) builder.setLatLngBounds(bounds);

            startActivityForResult(builder.build(this), PICKER_RC);

        } catch (GooglePlayServicesRepairableException re) {
            Log.e(LOG_TAG, re.getMessage());
        } catch (GooglePlayServicesNotAvailableException nae) {
            Log.e(LOG_TAG, nae.getMessage());
        }
    }

    private ArrayList<MeetingPlace> getMeetingPlaceList() {
        MeetingPlaceFragment fragment = (MeetingPlaceFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment.getMeetingPlaceList();
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onShareIntent(String inviteID) {
        String promptText = "Send via...";
        String template = getString(R.string.intent_def_msg);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, template + inviteID);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.intent_def_title));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, promptText));
    }

    @Override
    public void handlePlaceOption(Place place) {

        if (place != null) {
            MeetingPlaceFragment fragment = (MeetingPlaceFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.addPickedPlace(place);
        }
    }
}
