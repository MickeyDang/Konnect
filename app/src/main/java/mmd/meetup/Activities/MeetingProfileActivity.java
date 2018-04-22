package mmd.meetup.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import mmd.meetup.Constants;
import mmd.meetup.Fragments.MeetingProfileFragment;
import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.R;

public class MeetingProfileActivity extends AppCompatActivity implements MeetingProfileFragment.ItemInteractionListener{

    TextView mTitleView;
    TextView mDescriptionView;
    TextView mTimeView;
    TextView mPlaceView;
    ImageView mTimeLogo;
    ImageView mPlaceLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_profile);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        mTimeView = findViewById(R.id.time);
        mTitleView = findViewById(R.id.title);
        mDescriptionView = findViewById(R.id.description);
        mPlaceView = findViewById(R.id.place);
        mPlaceLogo = findViewById(R.id.placeLogo);
        mTimeLogo = findViewById(R.id.clockLogo);

        FinalizedMeeting fm = null;
        if (getIntent().getExtras() != null)
            fm = (FinalizedMeeting) getIntent().getExtras()
                    .getSerializable(Constants.KEYS.FINALIZED_MEETING_KEY);

        if (fm != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            configureView(fm);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, MeetingProfileFragment.newInstance(fm));
        fragmentTransaction.commit();

    }

    private void configureView(FinalizedMeeting finalizedMeeting) {
        mTitleView.setText(finalizedMeeting.getTitle());
        mDescriptionView.setText(finalizedMeeting.getDescription());

        String timeText = finalizedMeeting.getTimeOption().getDate() + " from " + finalizedMeeting.getTimeOption().getStartTime() + " to " +
                finalizedMeeting.getTimeOption().getEndTime();
        mTimeView.setText(timeText);

        String fullAddress = finalizedMeeting.getMeetingPlace().getAddress();

        String locText = finalizedMeeting.getMeetingPlace().getName() + "\n"
                + fullAddress;
        mPlaceView.setText(locText);
    }


}
