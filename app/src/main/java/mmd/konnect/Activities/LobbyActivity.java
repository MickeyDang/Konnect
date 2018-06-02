package mmd.konnect.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmd.konnect.Constants;
import mmd.konnect.Firebase.FirebaseClient;
import mmd.konnect.Fragments.BackPressFragment;
import mmd.konnect.Fragments.FinalizedMeetingListFragment;
import mmd.konnect.Fragments.PendingMeetingListFragment;
import mmd.konnect.Fragments.SettingFragment;
import mmd.konnect.Models.FinalizedMeeting;
import mmd.konnect.Models.Meeting;
import mmd.konnect.Models.MeetingPlace;
import mmd.konnect.Models.PendingMeeting;
import mmd.konnect.Models.TimeOption;
import mmd.konnect.R;
import mmd.konnect.Utils;

public class LobbyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FinalizedMeetingListFragment.MeetingInteractionListener,
        PendingMeetingListFragment.PendingMeetingInteractionListener,
        SettingFragment.SettingInteractionListener {

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                assertBackPressFragment();
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //default state
        navigationView.getMenu().findItem(R.id.nav_meetings_confirmed).setChecked(true);

        //gets root header view (linear layout)
        final View navHeader = navigationView.getHeaderView(0);

        String name = FirebaseClient.getInstance().getUser().getName();

        ((TextView) navHeader.findViewById(R.id.name)).setText(name != null ? name : getString(R.string.title_temp_user));
        ((TextView) navHeader.findViewById(R.id.email)).setText(FirebaseClient.getInstance().getUser().getEmail());
        //get material icon view
        MaterialLetterIcon materialLetterIcon = navHeader.findViewById(R.id.materialLetterIcon);
        //init material icon view
        materialLetterIcon.setLetter(FirebaseClient.getInstance().getUser().getName().substring(0, 1));
        materialLetterIcon.setBorder(false);
        materialLetterIcon.setShapeColor(Utils.getRandomMaterialColors(FirebaseClient.getInstance().getUserID()));

        goToFragment(FinalizedMeetingListFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!assertBackPressFragment()) {
            super.onBackPressed();
        }
    }

    private boolean assertBackPressFragment() {
        boolean isBPF = getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof BackPressFragment;
        if (isBPF) {
            BackPressFragment backPressFragment = (BackPressFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            backPressFragment.onBackPress();
        }
        return isBPF;
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
            goToFragment(FinalizedMeetingListFragment.newInstance());
            changeToolbarText(getString(R.string.meeting_conf));
        } else if (id == R.id.nav_meetings_unconfirmed) {
            goToFragment(PendingMeetingListFragment.newInstance());
            changeToolbarText(getString(R.string.meeting_unconf));
        } else if (id == R.id.nav_find_meeting) {
            makeSearchMeetingDialog();
        } else if (id == R.id.nav_sign_out) {
            signOut();
        } else if (id == R.id.nav_settings) {
            goToFragment(SettingFragment.newInstance());
            changeToolbarText(getString(R.string.nav_settings));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeToolbarText(String s) {
        getSupportActionBar().setTitle(s);
    }

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

                    mMaker.meeting = (PendingMeeting) data.getExtras().getSerializable(Constants.MeetingNavigation.MEETING_OBJ_KEY);
                    startActivityForResult(navigate(Constants.MeetingNavigation.stepTime, mMaker.meeting), Constants.MeetingNavigation.RC_TIME);
                }
                break;
            case Constants.MeetingNavigation.RC_TIME :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {

                    ArrayList<TimeOption> options = data.getExtras().getParcelableArrayList(Constants.MeetingNavigation.TIME_OPTION_KEY);
                    mMaker.meeting.setTimeOptions(options);

                    startActivityForResult(navigate(Constants.MeetingNavigation.stepLocation, mMaker.meeting), Constants.MeetingNavigation.RC_LOCATION);
                }
                break;
            case Constants.MeetingNavigation.RC_LOCATION:
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {

                    ArrayList<MeetingPlace> places = data.getExtras().getParcelableArrayList(Constants.MeetingNavigation.PLACE_OPTION_KEY);
                    mMaker.meeting.setMeetingPlaces(places);

                    startActivityForResult(navigate(Constants.MeetingNavigation.stepInvite, mMaker.meeting), Constants.MeetingNavigation.RC_INVITE);
                }
                break;
            case Constants.MeetingNavigation.RC_INVITE :
                if (resultCode == Constants.MeetingNavigation.resultSuccess) {
                    mMaker.addInvites(data.getStringArrayListExtra(Constants.MeetingNavigation.INVITEE_KEY));
                    mMaker.createInDB();
                }
                break;
            case Constants.RC_VOTE :
                if (resultCode == Constants.resultSuccess) {
                    String id = data.getStringExtra(Constants.KEYS.PENDING_MEETING_ID);
                    onVoteCast(id);
                }
        }
    }

    private void onVoteCast(String id) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof PendingMeetingListFragment) {
            PendingMeetingListFragment pmlFragment = (PendingMeetingListFragment) fragment;
            pmlFragment.notifyAdapterVoteCast(id);
        }
    }

    @Override
    public void onFinalizedMeetingSelected(FinalizedMeeting fm) {
        Intent intent = new Intent(this, MeetingProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEYS.FINALIZED_MEETING_KEY, fm);
        intent.putExtras(bundle);
        startActivity(intent);
    }

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

    class MeetingMaker {

        PendingMeeting meeting;
        HashMap<String, String> invites = new HashMap<>();

        public void addInvites(List<String> list) {
            invites.put(FirebaseClient.getInstance().getUserID(), "true");

            for (String s : list) {
                invites.put(s, "true");
            }
        }

        public void createInDB() {
            FirebaseClient.getInstance().makePendingMeeting(meeting, invites);
        }

    }
}
