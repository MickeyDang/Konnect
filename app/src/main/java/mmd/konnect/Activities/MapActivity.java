package mmd.konnect.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mmd.konnect.Fragments.MapFragment;
import mmd.konnect.R;

public class MapActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportActionBar().setTitle(getString(R.string.title_placepicker_activity));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, MapFragment.newInstance());
        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction() {

    }
}
