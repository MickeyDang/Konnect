package mmd.meetup.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mmd.meetup.Fragments.MapFragment;
import mmd.meetup.R;

public class MapActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, MapFragment.newInstance());
        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction() {

    }
}
