package mmd.meetup.Activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.List;

import mmd.meetup.Constants;
import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Firebase.FirebaseDB;
import mmd.meetup.Fragments.VoteListFragment;
import mmd.meetup.Models.MeetingPlace;
import mmd.meetup.Models.PendingMeeting;
import mmd.meetup.Models.TimeOption;
import mmd.meetup.R;

public class VoteActivity extends AppCompatActivity implements VoteListFragment.OnVoteSelectedListener{

    final String LOG_TAG = this.getClass().getSimpleName();

    SparseArray<MeetingPlace> selectedPlaces = new SparseArray<>();
    SparseArray<TimeOption> selectedTimes = new SparseArray<>();

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
                setResult(Constants.resultSuccess);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reviewResults() {
        //show dialog of list of the chosen results
    }

    private void saveResults() {

        int size = selectedPlaces.size();
        FirebaseClient fbClient = FirebaseClient.getInstance();

        //size is the number of key value pairs existing not the actual size of the array
        for (int i = 0; i < size ; i++) {
            //i is the actual index of the array NOT the key (ie there could be key of 4 at index 0)
            int key = selectedPlaces.keyAt(i);

            Log.d(LOG_TAG, "key found is " + key);

            fbClient.incrimentVoteCount(pm.getId(),
                    FirebaseDB.PendingMeetings.Entries.locationOptions, String.valueOf(key));

        }

        size = selectedTimes.size();

        for (int i = 0; i < size; i++) {
            int key = selectedTimes.keyAt(i);

            Log.d(LOG_TAG, "key found is " + key);

            fbClient.incrimentVoteCount(pm.getId(),
                    FirebaseDB.PendingMeetings.Entries.timeOptions, String.valueOf(key));
        }

        fbClient.disableVotingStatus(pm.getId());

    }


    private void goToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onPlaceRemoved(MeetingPlace mp, int position) {
       selectedPlaces.remove(position);
       Log.d(LOG_TAG, "onPlaceRemoved");

    }

    @Override
    public void onPlaceAdded(MeetingPlace mp, int position) {
        selectedPlaces.append(position, mp);
        Log.d(LOG_TAG, "onPlaceAdded: " + position);
    }

    @Override
    public void onTimeRemoved(TimeOption to, int position) {
        selectedTimes.remove(position);
        Log.d(LOG_TAG, "onTimeRemoved");
    }

    @Override
    public void onTimeAdded(TimeOption to, int position) {
        selectedTimes.append(position, to);
        Log.d(LOG_TAG, "onTimeAdded: " + position);
    }
}
