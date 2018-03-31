package mmd.meetup.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import mmd.meetup.Constants;
import mmd.meetup.Firebase.FirebaseClient;
import mmd.meetup.Fragments.MeetingListFragment;
import mmd.meetup.Fragments.VoteListFragment;
import mmd.meetup.Models.Meeting;
import mmd.meetup.R;

public class LobbyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MeetingListFragment.OnListFragmentInteractionListener,
        VoteListFragment.OnListFragmentInteractionListener {

    MeetingMaker mMaker = new MeetingMaker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMakeMeeting();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //default state
        navigationView.getMenu().findItem(R.id.nav_meetings_confirmed).setChecked(true);
        goToFragment(MeetingListFragment.newInstance(1));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meetings_confirmed) {
            goToFragment(MeetingListFragment.newInstance(1));
        } else if (id == R.id.nav_meetings_unconfirmed) {
            goToFragment(VoteListFragment.newInstance(1));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }

    private void goToMakeMeeting() {
        Intent intent = new Intent(this, CreateMeetingActivity.class);
        intent.putExtra(Constants.MeetingNavigation.STEP_KEY, Constants.MeetingNavigation.stepDescription);
        startActivityForResult(intent, Constants.MeetingNavigation.RC_DESCRIPTION);
    }

    private Intent navigate(String nextStep, @Nullable Meeting meeting) {

        Intent intent = new Intent(getApplicationContext(), CreateMeetingActivity.class);
        intent.putExtra(Constants.MeetingNavigation.STEP_KEY, nextStep);
        if (meeting != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY, mMaker.meeting);
            intent.putExtras(bundle);
        }
        return intent;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case Constants.MeetingNavigation.RC_DESCRIPTION :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    mMaker.meeting = (Meeting) data.getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
                    startActivityForResult(navigate(Constants.MeetingNavigation.stepTime, mMaker.meeting), Constants.MeetingNavigation.RC_TIME);
                }
                break;
            case Constants.MeetingNavigation.RC_TIME :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    startActivityForResult(navigate(Constants.MeetingNavigation.stepInvite, mMaker.meeting), Constants.MeetingNavigation.RC_INVITE);
                }
                break;
            case Constants.MeetingNavigation.RC_INVITE :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    mMaker.addInvites(data.getStringArrayListExtra(Constants.MeetingNavigation.INVITEE_KEY));
                    mMaker.createInDB();
                }
                break;

        }
    }

    @Override
    public void onListFragmentInteraction() {

    }

    class MeetingMaker {

        Meeting meeting;
        List<String> invites = new ArrayList<>();

        public void addInvites(List<String> list) {
            invites.add(FirebaseClient.getInstance().getUserID());
            invites.addAll(list);
        }

        public void createInDB() {
            FirebaseClient.getInstance().makeMeeting(meeting, invites);
        }

    }
}
