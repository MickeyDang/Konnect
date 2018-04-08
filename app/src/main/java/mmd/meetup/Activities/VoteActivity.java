package mmd.meetup.Activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;

import mmd.meetup.Constants;
import mmd.meetup.Fragments.VoteListFragment;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

public class VoteActivity extends AppCompatActivity implements VoteListFragment.OnVoteSelectedListener{

    HashMap<String, MeetingPlace> selectedPlaces = new HashMap<>();
    HashMap<String, TimeOption> selectedTimes = new HashMap<>();

    PendingMeeting pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        /* pending meeting is provided by Lobby Activity
        sent through callback from vote adapter due to button click */
        pm = (PendingMeeting) getIntent().getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);

        this.getSupportActionBar().setTitle(pm.getTitle());

        goToFragment(VoteListFragment.newInstance(pm));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.voting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_button:
                saveResults();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveResults() {

    }


    private void goToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onPlaceRemoved(MeetingPlace mp) {

    }

    @Override
    public void onPlaceAdded(MeetingPlace mp) {

    }

    @Override
    public void onTimeRemoved(TimeOption to) {

    }

    @Override
    public void onTimeAdded(TimeOption to) {

    }
}
