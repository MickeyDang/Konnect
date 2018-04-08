package mmd.meetup.Activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import mmd.meetup.Constants;
import mmd.meetup.Fragments.VoteListFragment;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.R;

public class VoteActivity extends AppCompatActivity implements VoteListFragment.OnListFragmentInteractionListener{

    PendingMeeting pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        /* pending meeting is provided by Lobby Activity
        sent through callback from vote adapter due to button click */
        pm = (PendingMeeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);

        this.getSupportActionBar().setTitle(pm.getTitle());
    }

    private void goToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
