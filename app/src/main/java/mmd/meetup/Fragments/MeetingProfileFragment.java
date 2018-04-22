package mmd.meetup.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import mmd.meetup.Models.FinalizedMeeting;
import mmd.meetup.R;


public class MeetingProfileFragment extends Fragment implements
        OnMapReadyCallback {

    final int RC_COARSE_LOC = 19;
    private final String LOG_TAG = this.getClass().getSimpleName();

    private FinalizedMeeting mFinalizedMeeting;
    private ItemInteractionListener mListener;

    FloatingActionButton mDirectionsButton;
    TextView mTitleView;
    TextView mDescriptionView;
    TextView mTimeView;
    TextView mPlaceView;
    ImageView mTimeLogo;
    ImageView mPlaceLogo;
    MapView mMapView;
    GoogleMap mGoogleMap;

    public MeetingProfileFragment() {
        // Required empty public constructor
    }

    public static MeetingProfileFragment newInstance(FinalizedMeeting fm) {
        MeetingProfileFragment fragment = new MeetingProfileFragment();
        fragment.mFinalizedMeeting = fm;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.mapView);
        mTimeView = view.findViewById(R.id.time);
        mTitleView = view.findViewById(R.id.title);
        mDescriptionView = view.findViewById(R.id.description);
        mPlaceView = view.findViewById(R.id.place);
        mPlaceLogo = view.findViewById(R.id.placeLogo);
        mTimeLogo = view.findViewById(R.id.clockLogo);
        mDirectionsButton = view.findViewById(R.id.directionsButton);

        configureView();

        mDirectionsButton.setOnClickListener(v -> getGoogleMapDirections());

        try {
            mMapView.onCreate(savedInstanceState);
            MapsInitializer.initialize(getContext());
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        animateToPosition(mFinalizedMeeting.getMeetingPlace().getLatitude(),
                mFinalizedMeeting.getMeetingPlace().getLongitude(),
                15f);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(mFinalizedMeeting.getMeetingPlace().getLatitude(), mFinalizedMeeting.getMeetingPlace().getLongitude()))
                .title(mFinalizedMeeting.getMeetingPlace().getName());
        mGoogleMap.addMarker(markerOptions);

        //check for permissions to display user location as dot
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    RC_COARSE_LOC);
        } else {
            mGoogleMap.setMyLocationEnabled(true);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == RC_COARSE_LOC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    private void animateToPosition(double lat, double lng, float zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(zoom)
                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void getGoogleMapDirections() {
        //sets up google maps navigation intent with latlng, driving mode, and avoiding tolls and ferries.
        Uri intentUri = Uri.parse("google.navigation:q=" + mFinalizedMeeting.getMeetingPlace().getLatitude()
                + "," + mFinalizedMeeting.getMeetingPlace().getLongitude() + "&mode=d&avoid=t,f");

        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri)
                .setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void configureView() {
        mTitleView.setText(mFinalizedMeeting.getTitle());
        mDescriptionView.setText(mFinalizedMeeting.getDescription());

        String timeText = mFinalizedMeeting.getTimeOption().getDate() + " from " + mFinalizedMeeting.getTimeOption().getStartTime() + " to " +
                mFinalizedMeeting.getTimeOption().getEndTime();
        mTimeView.setText(timeText);

        String fullAddress = mFinalizedMeeting.getMeetingPlace().getAddress();
//        String editAddress = fullAddress.length() > 30 ? fullAddress.substring(0, 30) + "..." : fullAddress;

        String locText = mFinalizedMeeting.getMeetingPlace().getName() + "\n"
                + fullAddress;
        mPlaceView.setText(locText);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemInteractionListener) {
            mListener = (ItemInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public interface ItemInteractionListener {}
}
