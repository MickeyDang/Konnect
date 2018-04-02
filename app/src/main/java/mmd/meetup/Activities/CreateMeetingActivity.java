package mmd.meetup.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Constants;
import mmd.meetup.Fragments.MeetingDetailsFragment;
import mmd.meetup.Fragments.MeetingInviteFragment;
import mmd.meetup.Fragments.MeetingPlaceFragment;
import mmd.meetup.Fragments.MeetingTimeFragment;
import mmd.meetup.Manifest;
import mmd.meetup.Models.Meeting;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

public class CreateMeetingActivity extends AppCompatActivity implements MeetingDetailsFragment.OnFragmentInteractionListener,
    MeetingInviteFragment.OnFragmentInteractionListener, MeetingTimeFragment.OnFragmentInteractionListener,
        MeetingPlaceFragment.PlacePickerHandler{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final int PICKER_RC = 99;
    private final int PERMISSION_RC = 98;

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
        } else if (currentStep.equals(Constants.MeetingNavigation.stepTime)) {
            Meeting meeting = (Meeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
            goToFragment(MeetingTimeFragment.newInstance(meeting));
        } else if (currentStep.equals(Constants.MeetingNavigation.stepLocation)) {
            Meeting meeting = (Meeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
            goToFragment(MeetingPlaceFragment.newInstance(meeting));
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
                        this.finish();
                    }

                } else if (currentStep.equals(Constants.MeetingNavigation.stepTime)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.MeetingNavigation.TIME_OPTION_KEY, getTimeOptions());
                    intent.putExtras(bundle);
                    setResult(Constants.MeetingNavigation.resultSuccess, intent);
                    this.finish();

                } else if (currentStep.equals(Constants.MeetingNavigation.stepLocation)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList(Constants.MeetingNavigation.PLACE_OPTION_KEY, getMeetingPlaceList());
                    intent.putExtras(bundle);
                    setResult(Constants.MeetingNavigation.resultSuccess, intent);
                    this.finish();

                } else if (currentStep.equals(Constants.MeetingNavigation.stepInvite)) {

                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(Constants.MeetingNavigation.INVITEE_KEY, getInvitees());
                    setResult(Constants.MeetingNavigation.resultSuccess, intent);
                    this.finish();
                    //handle appropriately
                }
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

    //handle map selection


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
        //request permissions
        startPlacePickerWidget(null);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_RC);
//
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_RC:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //handle
                } else {
                    startPlacePickerWidget(null);
                }
        }

    }

    private void getUserLatLngBounds() {

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
    public void handlePlaceOption(Place place) {

        if (place != null) {
            MeetingPlaceFragment fragment = (MeetingPlaceFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.addPickedPlace(place);
        }
//        String toastMsg = String.format("Place: %s", place.getName());
//        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();

    }
}
