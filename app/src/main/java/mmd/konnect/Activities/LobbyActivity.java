package mmd.konnect.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import mmd.konnect.Constants;
import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Fragments.SaveStateFragment;
import mmd.konnect.Fragments.FinalizedMeetingListFragment;
import mmd.konnect.Fragments.PendingMeetingListFragment;
import mmd.konnect.Fragments.SettingFragment;
import mmd.konnect.Fragments.ShortListFragment;
import mmd.konnect.Models.FinalizedMeeting;
import mmd.konnect.Models.Meeting;
import mmd.konnect.Models.MeetingBuilder;
import mmd.konnect.Models.PendingMeeting;
import mmd.konnect.R;
import mmd.konnect.Utils;

public class LobbyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FinalizedMeetingListFragment.MeetingInteractionListener,
        PendingMeetingListFragment.PendingMeetingInteractionListener,
        SettingFragment.SettingInteractionListener, ShortListFragment.OnListFragmentInteractionListener {

    MeetingBuilder mMeetingBuilder = new MeetingBuilder();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> goToMakeMeeting());

        //set up drawer listeners
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                requiresSaveFragmentState();
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //set up navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //default state
        navigationView.getMenu().findItem(R.id.nav_meetings_confirmed).setChecked(true);

        //gets root header view (linear layout)
        final View navHeader = navigationView.getHeaderView(0);

        String name = FirebaseClient.getInstance().getUser().getName();

        //init name and email textviews
        ((TextView) navHeader.findViewById(R.id.name)).setText(name != null ? name : getString(R.string.title_temp_user));
        ((TextView) navHeader.findViewById(R.id.email)).setText(FirebaseClient.getInstance().getUser().getEmail());
        //get material icon view
        MaterialLetterIcon materialLetterIcon = navHeader.findViewById(R.id.materialLetterIcon);
        //init material icon view
        materialLetterIcon.setLetter(FirebaseClient.getInstance().getUser().getName().substring(0, 1));
        materialLetterIcon.setBorder(false);
        materialLetterIcon.setShapeColor(Utils.getHashedMaterialColor(FirebaseClient.getInstance().getUserID()));

        goToFragment(FinalizedMeetingListFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!requiresSaveFragmentState()) {
            super.onBackPressed();
        }
    }

    //runs to check if a fragment in foreground needs to save state before exiting
    private boolean requiresSaveFragmentState() {
        boolean isSSF = getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SaveStateFragment;
        if (isSSF) {
            SaveStateFragment saveStateFragment = (SaveStateFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            saveStateFragment.onBackPress();
        }
        return isSSF;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the action bar if it is present.
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meetings_confirmed) {
            goToFragment(FinalizedMeetingListFragment.newInstance());
            changeToolbarText(getString(R.string.meeting_conf));
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_meetings_unconfirmed) {
            goToFragment(PendingMeetingListFragment.newInstance());
            fab.setVisibility(View.VISIBLE);
            changeToolbarText(getString(R.string.meeting_unconf));
        } else if (id == R.id.nav_find_meeting) {
            makeSearchMeetingDialog();
        } else if (id == R.id.nav_sign_out) {
            signOut();
        } else if (id == R.id.nav_settings) {
            goToFragment(SettingFragment.newInstance());
            changeToolbarText(getString(R.string.nav_settings));
            fab.setVisibility(View.GONE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeToolbarText(String s) {
        getSupportActionBar().setTitle(s);
    }

    //makes dialog to find meeting
    private void makeSearchMeetingDialog() {
        final View v = View.inflate(this, R.layout.view_find_meeting_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(v)
                .setTitle(getString(R.string.title_search))
                .setPositiveButton(getString(R.string.title_search), (DialogInterface dialogInterface, int i) -> {
                        EditText editText = v.findViewById(R.id.inviteIdField);
                        FirebaseClient.getInstance().addUserToMeeting(editText.getText().toString(), s -> {

                                if (!s.equals(FirebaseClient.Callback.NULL)) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.toast_add_success), Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.toast_add_fail), Toast.LENGTH_SHORT)
                                            .show();
                                }
                        });
                });
        builder.show();
    }

    private void signOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.KEYS.SIGN_OUT_KEY, true);
        startActivity(intent);
        this.finish();
        FirebaseClient.getInstance().signOutUser();
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


    //returns intent to move to next step of meeting creation
    private Intent navigate(String nextStep, @Nullable Meeting meeting) {
        Intent intent = new Intent(getApplicationContext(), CreateMeetingActivity.class);
        intent.putExtra(Constants.MeetingNavigation.STEP_KEY, nextStep);
        if (meeting != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY, mMeetingBuilder.getMeeting());
            intent.putExtras(bundle);
        }
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //meeting creation results
            case Constants.MeetingNavigation.RC_DESCRIPTION :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    startActivityForResult(
                            navigate(Constants.MeetingNavigation.stepTime, mMeetingBuilder.initProtoMeeting(data)), Constants.MeetingNavigation.RC_TIME);
                }
                break;
            case Constants.MeetingNavigation.RC_TIME :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    startActivityForResult(
                            navigate(Constants.MeetingNavigation.stepLocation, mMeetingBuilder.initTimeOptions(data)), Constants.MeetingNavigation.RC_LOCATION);
                }
                break;
            case Constants.MeetingNavigation.RC_LOCATION:
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    startActivityForResult(
                            navigate(Constants.MeetingNavigation.stepInvite, mMeetingBuilder.initPlaceOptions(data)), Constants.MeetingNavigation.RC_INVITE);
                }
                break;
            case Constants.MeetingNavigation.RC_INVITE :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    mMeetingBuilder.addInvites(data.getStringArrayListExtra(Constants.MeetingNavigation.INVITEE_KEY));
                    mMeetingBuilder.createInDB();
                }
                break;
            //user vote result
            case Constants.RC_VOTE :
                if (resultCode == Constants.resultSuccess) {
                    String id = data.getStringExtra(Constants.KEYS.PENDING_MEETING_ID);
                    onVoteCast(id);
                }
        }
    }

    //runs when vote has been cast by logged in user
    private void onVoteCast(String id) {
        //notifies fragment of change
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof PendingMeetingListFragment) {
            PendingMeetingListFragment pmlFragment = (PendingMeetingListFragment) fragment;
            pmlFragment.notifyAdapterVoteCast(id);
        }
    }


    //Callbacks
    @Override
    public void onFinalizedMeetingSelected(FinalizedMeeting fm) {
        Intent intent = new Intent(this, MeetingProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEYS.FINALIZED_MEETING_KEY, fm);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //runs when user chooses to cast vote
    @Override
    public void onCastVote(PendingMeeting pm) {
        Intent intent = new Intent(this, VoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY, pm);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.RC_VOTE);
    }

    @Override
    public void onResolveVote(final PendingMeeting pm) {

        FirebaseClient.getInstance().resolveVote(pm, finalizedMeeting ->  {
            FirebaseClient.getInstance().makeFinalizedMeeting(finalizedMeeting, aBoolean -> {

                FirebaseClient.getInstance().deletePendingMeetingTrace(pm);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_resolve_vote_success), Toast.LENGTH_SHORT)
                        .show();

            });
        });
    }

    @Override
    public void onDeleteMeeting(PendingMeeting pm) {
        FirebaseClient.getInstance().deletePendingMeetingTrace(pm);
    }

    @Override
    public void onNameChanged(String name) {
        final View navHeader = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.name))
                .setText(name != null ? name : getString(R.string.title_temp_user));
    }
}
