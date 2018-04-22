package mmd.meetup.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mmd.meetup.Constants;
import mmd.meetup.Fragments.MeetingProfileFragment;
import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.R;

public class MeetingProfileActivity extends AppCompatActivity implements MeetingProfileFragment.ItemInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);


        FinalizedMeeting fm = null;
        if (getIntent().getExtras() != null)
            fm = (FinalizedMeeting) getIntent().getExtras()
                    .getSerializable(Constants.KEYS.FINALIZED_MEETING_KEY);

        if (fm != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, MeetingProfileFragment.newInstance(fm));
        fragmentTransaction.commit();

    }

}
