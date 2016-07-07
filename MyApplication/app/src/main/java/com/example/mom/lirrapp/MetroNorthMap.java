package com.example.mom.lirrapp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class MetroNorthMap extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private static final int PERMISSIONS_LOCATION = 0;
    private static final String TAG = LIRRMap.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private MapView mMapView;
    private TextView mblank;
    private MapboxMap mMap;
    private ProgressBar pb;
    private Location mLastLocationCoordinates;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private double longitude;
    private double latitude;
    private FloatingActionButton floatingActionButton;
    private List<LatLng> mHudsonLine, mHarlemLine, mNewHavenLine, mNewCanaanLine,mDanburyLine,mWaterburyLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_north_map);
        mblank = (TextView) findViewById(R.id.blank2Metro);
        pb = (ProgressBar) findViewById(R.id.pbMetro);

        final Items items = new Items();


        floatingActionButton = (FloatingActionButton) findViewById(R.id.mapButtonMetro);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                items.setLatitude(latitude);
                items.setLongitude(longitude);

            }
        });

        mMapView = (MapView) findViewById(R.id.mapviewMetroNorth);
        mMapView.onCreate(savedInstanceState);
        plotPoints();
        configureButton();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasSufficientPermissions()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            }
        }

    }

    private void plotPoints() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mMap = mapboxMap;
                mapboxMap.setMyLocationEnabled(true);
                new MetroLinesAsyncTask().execute();


                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(latitude, longitude)) // Sets the new camera position
                                .zoom(17) // Sets the zoom
                                .bearing(180) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 7000);
                    }
                });
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates && hasSufficientPermissions()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        stopLocationUpdates();
    }

    private boolean hasSufficientPermissions() {
        return (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void displayLocation() {
        if (!hasSufficientPermissions()) {
            return;
        }
        mLastLocationCoordinates = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocationCoordinates != null) {
            latitude = mLastLocationCoordinates.getLatitude();
            longitude = mLastLocationCoordinates.getLongitude();
            mblank.setText("");
        } else {
            Toast.makeText(MetroNorthMap.this, R.string.location_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePeriodLocation() {
        if (!mRequestLocationUpdates) {
            mRequestLocationUpdates = true;
            startLocationUpdates();
        } else {
            mRequestLocationUpdates = false;

            stopLocationUpdates();
        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Constants.DISPLACEMENT);

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(MetroNorthMap.this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    // Use private instead of protected
    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
        }
    }

    private void configureButton() {
//        mLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ReportIconsFragment reportIconsFragment = ReportIconsFragment.newInstance("Report");
        reportIconsFragment.show(getFragmentManager(), "custom_fragment_dialog");
    }

    // Add the mMapView lifecycle to the activity's lifecycle methods


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (hasSufficientPermissions()) {
            displayLocation();
            if (mRequestLocationUpdates) {
                startLocationUpdates();
            }
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocationCoordinates = location;
        Toast.makeText(MetroNorthMap.this, "Location Changed", Toast.LENGTH_SHORT).show();
        displayLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed" + connectionResult.getErrorMessage());
    }

    private class MetroLinesAsyncTask extends AsyncTask<Void,Void,List<LatLng>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<LatLng> doInBackground(Void... params) {
            mHudsonLine= new ArrayList<>();
            mDanburyLine= new ArrayList<>();
            mHarlemLine= new ArrayList<>();
            mNewCanaanLine= new ArrayList<>();
            mNewHavenLine= new ArrayList<>();
            mWaterburyLine= new ArrayList<>();

            //Hudson Line
            mHudsonLine.add(new LatLng(40.7528, -73.976522));//Grand Central Terminal
            mHudsonLine.add(new LatLng(40.8052, -73.939));// Harlem-125th Street
            mHudsonLine.add(new LatLng(40.825375, -73.930267));//Yankee Stadium- 153rd Street
            mHudsonLine.add(new LatLng(40.854, -73.9199));//Morris Heights
            mHudsonLine.add(new LatLng(40.8614, -73.9147));//University Heights
            mHudsonLine.add(new LatLng(40.8747, -73.912));//Marble Hill
            mHudsonLine.add(new LatLng(40.8789, -73.9227));//Spuyten Duyvil
            mHudsonLine.add(new LatLng(40.90444, -73.9139));//Riverdale
            mHudsonLine.add(new LatLng(40.9238, -73.9056));//Ludlow
            mHudsonLine.add(new LatLng(40.9356, -73.9023));//Yonkers
            mHudsonLine.add(new LatLng(40.9506, -73.8991));//Glenwood
            mHudsonLine.add(new LatLng(40.9721, -73.8896));//Greystone
            mHudsonLine.add(new LatLng(40.9946, -73.8847));//Hastings on Hudson
            mHudsonLine.add(new LatLng(41.0125, -73.879444));//Dobbs Ferry
            mHudsonLine.add(new LatLng(41.027, -73.8769));//Ardsley on Hudson
            mHudsonLine.add(new LatLng(41.0395, -73.8733));//Irvington
            mHudsonLine.add(new LatLng(41.0755, -73.8656));//Tarrytown
            mHudsonLine.add(new LatLng(41.094722, -73.869444));//Philipse Manor
            mHudsonLine.add(new LatLng(41.137, -73.8664));//Scarborough
            mHudsonLine.add(new LatLng(41.156, -73.8696));//Ossining
            mHudsonLine.add(new LatLng(41.1898, -73.8827));//Croton-Harmon Station
            mHudsonLine.add(new LatLng(41.247, -73.9232));//Cortlandt Station
            mHudsonLine.add(new LatLng(41.285, -73.930833));//Peekskill
            mHudsonLine.add(new LatLng(41.3324, -73.9709));//Manitou
            mHudsonLine.add(new LatLng(41.3828, -73.9471));//Garrison
            mHudsonLine.add(new LatLng(41.4152, -73.9585));//Cold Spring
            mHudsonLine.add(new LatLng(41.4508, -73.9829));//Breakneck Ridge
            mHudsonLine.add(new LatLng(41.5055, -73.9853));//Beacon
            mHudsonLine.add(new LatLng(41.5889, -73.9479));//New Hamburg
            mHudsonLine.add(new LatLng(41.707222, -73.938333));//Poughkeepsie

            //Harlem Line
            mHarlemLine.add(new LatLng(40.7528, -73.976522));//Grand Central Terminal
            mHarlemLine.add(new LatLng(40.8052, -73.939));// Harlem-125th Street
            mHarlemLine.add(new LatLng(40.8257, -73.9154));//Melrose Station
            mHarlemLine.add(new LatLng(40.8472, -73.8997));//Tremont
            mHarlemLine.add(new LatLng(40.861534, -73.890561));//Fordham
            mHarlemLine.add(new LatLng(40.8671, -73.8819));//Botanical Garden
            mHarlemLine.add(new LatLng(40.8788, -73.8707));//Williams Bridge
            mHarlemLine.add(new LatLng(40.8955, -73.8628));//Woodlawn Station
            mHarlemLine.add(new LatLng(40.9062, -73.8554));//Wakefield
            mHarlemLine.add(new LatLng(40.913, -73.8502));//Mount Vernon West
            mHarlemLine.add(new LatLng(40.927, -73.84));//Fleetwood
            mHarlemLine.add(new LatLng(40.941, -73.8351));//Bronxville
            mHarlemLine.add(new LatLng(40.9505, -73.8284));//Tuckahoe
            mHarlemLine.add(new LatLng(40.959, -73.8209));//Crestwood
            mHarlemLine.add(new LatLng(40.9899, -73.8083));//Scarsdale
            mHarlemLine.add(new LatLng(41.011111, -73.795833));//Hartsdale
            mHarlemLine.add(new LatLng(41.0338, -73.7747));//White Plains
            mHarlemLine.add(new LatLng(41.051389, -73.7725));//North White Plains
            mHarlemLine.add(new LatLng(41.0732, -73.7729));//Valhalla
            mHarlemLine.add(new LatLng(41.096, -73.7938));//Mount Pleasant
            mHarlemLine.add(new LatLng(41.109, -73.796));//Hawthorne
            mHarlemLine.add(new LatLng(41.1348, -73.7923));//Pleasantville
            mHarlemLine.add(new LatLng(41.1579, -73.7749));//Chappaqua
            mHarlemLine.add(new LatLng(41.2084, -73.7296));//Mount Kisko
            mHarlemLine.add(new LatLng(41.2373, -73.7001));//Bedford Hills
            mHarlemLine.add(new LatLng(41.2598, -73.6841));//Katonah
            mHarlemLine.add(new LatLng(41.2945, -73.6776));//Goldens Bridge
            mHarlemLine.add(new LatLng(41.3256, -73.659));//Purdy's
            mHarlemLine.add(new LatLng(41.3479, -73.6622));//Croton Falls
            mHarlemLine.add(new LatLng(41.3947, -73.6198));//Brewster
            mHarlemLine.add(new LatLng(41.4127, -73.623));//Southeast
            mHarlemLine.add(new LatLng(41.51174, -73.60428));//Patterson
            mHarlemLine.add(new LatLng(41.5646, -73.6004));//Pawling
            mHarlemLine.add(new LatLng(41.5929, -73.588));//Appalachian Trail
            mHarlemLine.add(new LatLng(41.6374, -73.5717));//Harlem Valley- Wingdale
            mHarlemLine.add(new LatLng(41.7427, -73.5762));//Dover Plains
            mHarlemLine.add(new LatLng(41.7795, -73.559));//Tenmile River
            mHarlemLine.add(new LatLng(41.8147, -73.5623));//Wassiac

            //New Haven Line
            mNewHavenLine.add(new LatLng(40.861534, -73.890561));//Fordham
            mNewHavenLine.add(new LatLng(40.911942, -73.831678));//Mount Vernon East
            mNewHavenLine.add(new LatLng(40.912961, -73.810251));//Pelham
            mNewHavenLine.add(new LatLng(40.912317, -73.784936));//New Rochelle
            mNewHavenLine.add(new LatLng(40.934444, -73.759782));//Larchmont
            mNewHavenLine.add(new LatLng(40.955109, -73.736115));//Marmaroneck
            mNewHavenLine.add(new LatLng(40.97, -73.711));//Harrison
            mNewHavenLine.add(new LatLng(40.987803, -73.679123));//Rye
            mNewHavenLine.add(new LatLng(41.00178, -73.664703));//Port Chester
            mNewHavenLine.add(new LatLng(41.022326, -73.62462));//Greenwich
            mNewHavenLine.add(new LatLng(41.031111, -73.598333));//Cos Cob
            mNewHavenLine.add(new LatLng(41.032735, -73.594215));//Riverside
            mNewHavenLine.add(new LatLng(41.033333, -73.567778));//Old Greenwich
            mNewHavenLine.add(new LatLng(41.046937, -73.541493));//Stamford Terminal
            mNewHavenLine.add(new LatLng(41.070099, -73.49787));//Noroton Heights
            mNewHavenLine.add(new LatLng(41.078187, -73.472958));//Darien
            mNewHavenLine.add(new LatLng(41.07851, -73.445535));//Rowayton
            mNewHavenLine.add(new LatLng(41.0957, -73.42185));//South Norwalk
            mNewHavenLine.add(new LatLng(41.104, -73.4045));//East Norwalk
            mNewHavenLine.add(new LatLng(41.119986, -73.37142));//Westport
            mNewHavenLine.add(new LatLng(41.123316, -73.315415));//Green's Farm
            mNewHavenLine.add(new LatLng(41.136389, -73.286111));//Southport
            mNewHavenLine.add(new LatLng(41.142778, -73.258056));//Fairfield
            mNewHavenLine.add(new LatLng(41.1611, -73.2343));//Fairfield Metro
            mNewHavenLine.add(new LatLng(41.1778, -73.1871));//Bridgeport
            mNewHavenLine.add(new LatLng(41.195303, -73.131523));//Stratford
            mNewHavenLine.add(new LatLng(41.22235, -73.059619));//Milford
            mNewHavenLine.add(new LatLng(41.271142, -72.963199));//West Haven
            mNewHavenLine.add(new LatLng(41.2975, -72.926667));//Union Station- New Haven
            mNewHavenLine.add(new LatLng(41.305763, -72.921753));//New Haven State Street

            //New Canaan
            mNewCanaanLine.add(new LatLng(41.046937, -73.541493));//Stamford Terminal
            mNewCanaanLine.add(new LatLng(41.0705, -73.5199));//Glenbrook
            mNewCanaanLine.add(new LatLng(41.0888, -73.5178));//Springdale
            mNewCanaanLine.add(new LatLng(41.116, -73.4981));//Talmadge Hill
            mNewCanaanLine.add(new LatLng(41.1463, -73.4956));//New Canaan

            //Danbury
            mDanburyLine.add(new LatLng(41.0957, -73.42185));//South Norwalk
            mDanburyLine.add(new LatLng(41.1466, -73.4278));//Merrit 7
            mDanburyLine.add(new LatLng(41.1959, -73.4321));//Wilton
            mDanburyLine.add(new LatLng(41.216667, -73.426667));//Canonndale
            mDanburyLine.add(new LatLng(41.2667, -73.4409));//Branchville
            mDanburyLine.add(new LatLng(41.3256, -73.4335));//Redding
            mDanburyLine.add(new LatLng(41.376, -73.418));//Bethel
            mDanburyLine.add(new LatLng(41.3963, -73.4493));//Danbury

            //Waterbury
            mWaterburyLine.add(new LatLng(41.1778, -73.1871));//Bridgeport
            mWaterburyLine.add(new LatLng(41.195303, -73.131523));//Stratford
            mWaterburyLine.add(new LatLng(41.320284, -73.083565));//Derby-Shelton
            mWaterburyLine.add(new LatLng(41.3442, -73.0799));//Asonia
            mWaterburyLine.add(new LatLng(41.3953, -73.0725));//Seymour
            mWaterburyLine.add(new LatLng(41.4407, -73.0631));//Beacon Falls
            mWaterburyLine.add(new LatLng(41.492778, -73.052222));//Naugatuck
            mWaterburyLine.add(new LatLng(41.5544, -73.047));//Waterbury



            return null;
        }

        @Override
        protected void onPostExecute(List<LatLng> latLngs) {
            super.onPostExecute(latLngs);
            pb.setVisibility(View.GONE);
            mMap.addPolyline(new PolylineOptions().addAll(mHudsonLine).color(Color.parseColor("#8BC34A")));
            mMap.addPolyline(new PolylineOptions().addAll(mHarlemLine).color(Color.parseColor("#09A6C9")));
            mMap.addPolyline(new PolylineOptions().addAll(mNewHavenLine).color(Color.parseColor("#AB2A07")));
            mMap.addPolyline(new PolylineOptions().addAll(mNewCanaanLine).color(Color.parseColor("#FFC300")));
            mMap.addPolyline(new PolylineOptions().addAll(mDanburyLine).color(Color.parseColor("#D3720B")));
            mMap.addPolyline(new PolylineOptions().addAll(mWaterburyLine).color(Color.parseColor("#5FECC9")));

            //Hudson Markers
            mMap.addMarker(new MarkerOptions().title("Grand Central Terminal").position(new LatLng(40.7528, -73.976522)));
            mMap.addMarker(new MarkerOptions().title("Harlem 125th Street Station").position(new LatLng(40.8052, -73.939)));
            mMap.addMarker(new MarkerOptions().title("Yankee Stadium 153rd Street Station").position(new LatLng(40.825375, -73.930267)));
            mMap.addMarker(new MarkerOptions().title("Morris Heights Station").position(new LatLng(40.854, -73.9199)));
            mMap.addMarker(new MarkerOptions().title("University Heights Station").position(new LatLng(40.8614, -73.9147)));
            mMap.addMarker(new MarkerOptions().title("Marble Hill Station").position(new LatLng(40.8747, -73.912)));
            mMap.addMarker(new MarkerOptions().title("Spuyten Duyvil Station").position(new LatLng(40.8789, -73.9227)));
            mMap.addMarker(new MarkerOptions().title("Riverdale Station").position(new LatLng(40.90444, -73.9139)));
            mMap.addMarker(new MarkerOptions().title("Ludlow Station").position(new LatLng(40.9238, -73.9056)));
            mMap.addMarker(new MarkerOptions().title("Yonkers Station").position(new LatLng(40.9356, -73.9023)));
            mMap.addMarker(new MarkerOptions().title("Glenwood Station").position(new LatLng(40.9506, -73.8991)));
            mMap.addMarker(new MarkerOptions().title("Greystone Station").position(new LatLng(40.9721, -73.8896)));
            mMap.addMarker(new MarkerOptions().title("Hastings on Hudson Station").position(new LatLng(40.9946, -73.8847)));
            mMap.addMarker(new MarkerOptions().title("Dobbs Ferry Station").position(new LatLng(41.0125, -73.879444)));
            mMap.addMarker(new MarkerOptions().title("Ardsley on Hudson Station").position(new LatLng(41.027, -73.8769)));
            mMap.addMarker(new MarkerOptions().title("Irvington Station").position(new LatLng(41.0395, -73.8733)));
            mMap.addMarker(new MarkerOptions().title("Tarrytown Station").position(new LatLng(41.0755, -73.8656)));
            mMap.addMarker(new MarkerOptions().title("Philipse Manor Station").position(new LatLng(41.094722, -73.869444)));
            mMap.addMarker(new MarkerOptions().title("Scarborough Station").position(new LatLng(41.137, -73.8664)));
            mMap.addMarker(new MarkerOptions().title("Ossining Station").position(new LatLng(41.156, -73.8696)));
            mMap.addMarker(new MarkerOptions().title("Croton-Harmon Station").position(new LatLng(41.1898, -73.8827)));
            mMap.addMarker(new MarkerOptions().title("Cortlandt Station").position(new LatLng(41.247, -73.9232)));
            mMap.addMarker(new MarkerOptions().title("Peekskill Station").position(new LatLng(41.285, -73.930833)));
            mMap.addMarker(new MarkerOptions().title("Manitou Station").position(new LatLng(41.3324, -73.9709)));
            mMap.addMarker(new MarkerOptions().title("Garrison Station").position(new LatLng(41.3828, -73.9471)));
            mMap.addMarker(new MarkerOptions().title("Cold Spring Station").position(new LatLng(41.4152, -73.9585)));
            mMap.addMarker(new MarkerOptions().title("Breakneck Ridge Station").position(new LatLng(41.4508, -73.9829)));
            mMap.addMarker(new MarkerOptions().title("Beacon Station").position(new LatLng(41.5055, -73.9853)));
            mMap.addMarker(new MarkerOptions().title("New Hamburg Station").position(new LatLng(41.5889, -73.9479)));
            mMap.addMarker(new MarkerOptions().title("Poughkeepsie Station").position(new LatLng(41.707222, -73.938333)));

            //Harlem Markers
            mMap.addMarker(new MarkerOptions().title("Melrose Station").position(new LatLng(40.8257, -73.9154)));
            mMap.addMarker(new MarkerOptions().title("Tremont Station").position(new LatLng(40.8472, -73.8997)));
            mMap.addMarker(new MarkerOptions().title("Fordham Station").position(new LatLng(40.861534, -73.890561)));
            mMap.addMarker(new MarkerOptions().title("Botanical Gardens Station").position(new LatLng(40.8671, -73.8819)));
            mMap.addMarker(new MarkerOptions().title("Williams Bridge Station").position(new LatLng(40.8788, -73.8707)));
            mMap.addMarker(new MarkerOptions().title("Woodlawn Station").position(new LatLng(40.8955, -73.8628)));
            mMap.addMarker(new MarkerOptions().title("Wakefield Station").position(new LatLng(40.9062, -73.8554)));
            mMap.addMarker(new MarkerOptions().title("Mount Vernon West Station").position(new LatLng(40.913, -73.8502)));
            mMap.addMarker(new MarkerOptions().title("Fleetwood Station").position(new LatLng(40.927, -73.84)));
            mMap.addMarker(new MarkerOptions().title("Bronxville Station").position(new LatLng(40.941, -73.8351)));
            mMap.addMarker(new MarkerOptions().title("Tuckahoe Station").position(new LatLng(40.9505, -73.8284)));
            mMap.addMarker(new MarkerOptions().title("Crestwood Station").position(new LatLng(40.959, -73.8209)));
            mMap.addMarker(new MarkerOptions().title("Scarsdale Station").position(new LatLng(40.9899, -73.8083)));
            mMap.addMarker(new MarkerOptions().title("Hartsdale Station").position(new LatLng(41.011111, -73.795833)));
            mMap.addMarker(new MarkerOptions().title("White Plains Station").position(new LatLng(41.0338, -73.7747)));
            mMap.addMarker(new MarkerOptions().title("North White Plains Station").position(new LatLng(41.051389, -73.7725)));
            mMap.addMarker(new MarkerOptions().title("Valhalla Station").position(new LatLng(41.0732, -73.7729)));
            mMap.addMarker(new MarkerOptions().title("Mount Pleasant Station").position(new LatLng(41.096, -73.7938)));
            mMap.addMarker(new MarkerOptions().title("Hawthorne Station").position(new LatLng(41.109, -73.796)));
            mMap.addMarker(new MarkerOptions().title("Pleasantville Station").position(new LatLng(41.1348, -73.7923)));
            mMap.addMarker(new MarkerOptions().title("Chappaqua Station").position(new LatLng(41.1579, -73.7749)));
            mMap.addMarker(new MarkerOptions().title("Mount Kisko Station").position(new LatLng(41.2084, -73.7296)));
            mMap.addMarker(new MarkerOptions().title("Bedford Hills Station").position(new LatLng(41.2373, -73.7001)));
            mMap.addMarker(new MarkerOptions().title("Katonah Station").position(new LatLng(41.2598, -73.6841)));
            mMap.addMarker(new MarkerOptions().title("Golden's Bridge Station").position(new LatLng(41.2945, -73.6776)));
            mMap.addMarker(new MarkerOptions().title("Purdy's Station").position(new LatLng(41.3256, -73.659)));
            mMap.addMarker(new MarkerOptions().title("Croton-Falls Station").position(new LatLng(41.3479, -73.6622)));
            mMap.addMarker(new MarkerOptions().title("Brewster Station").position(new LatLng(41.3947, -73.6198)));
            mMap.addMarker(new MarkerOptions().title("Southeast Station").position(new LatLng(41.4127, -73.623)));
            mMap.addMarker(new MarkerOptions().title("Patterson Station").position(new LatLng(41.51174, -73.60428)));
            mMap.addMarker(new MarkerOptions().title("Pawling Station").position(new LatLng(41.5646, -73.6004)));
            mMap.addMarker(new MarkerOptions().title("Appalachian Trail Station").position(new LatLng(41.5929, -73.588)));
            mMap.addMarker(new MarkerOptions().title("Harlem Valley-Wingdale Station").position(new LatLng(41.6374, -73.5717)));
            mMap.addMarker(new MarkerOptions().title("Dover Plains Station").position(new LatLng(41.7427, -73.5762)));
            mMap.addMarker(new MarkerOptions().title("Tenmile River Station").position(new LatLng(41.7795, -73.559)));
            mMap.addMarker(new MarkerOptions().title("Wassiac Station").position(new LatLng(41.8147, -73.5623)));

            //New Haven Markers
            mMap.addMarker(new MarkerOptions().title("Fordham Station").position(new LatLng(40.861534, -73.890561)));
            mMap.addMarker(new MarkerOptions().title("Mount Vernon East Station").position(new LatLng(40.911942, -73.831678)));
            mMap.addMarker(new MarkerOptions().title("Pelham Station").position(new LatLng(40.912961, -73.810251)));
            mMap.addMarker(new MarkerOptions().title("New Rochelle Station").position(new LatLng(40.912317, -73.784936)));
            mMap.addMarker(new MarkerOptions().title("Larchmont Station").position(new LatLng(40.934444, -73.759782)));
            mMap.addMarker(new MarkerOptions().title("Marmaroneck Station").position(new LatLng(40.955109, -73.736115)));
            mMap.addMarker(new MarkerOptions().title("Harrison Station").position(new LatLng(40.97, -73.711)));
            mMap.addMarker(new MarkerOptions().title("Rye Station").position(new LatLng(40.987803, -73.679123)));
            mMap.addMarker(new MarkerOptions().title("Port Chester Station").position(new LatLng(41.00178, -73.664703)));
            mMap.addMarker(new MarkerOptions().title("Greenwich Station").position(new LatLng(41.022326, -73.62462)));
            mMap.addMarker(new MarkerOptions().title("Cos Cob Station").position(new LatLng(41.031111, -73.598333)));
            mMap.addMarker(new MarkerOptions().title("Riverside Station").position(new LatLng(41.032735, -73.594215)));
            mMap.addMarker(new MarkerOptions().title("Old Greenwich Station").position(new LatLng(41.033333, -73.567778)));
            mMap.addMarker(new MarkerOptions().title("Stamford Terminal").position(new LatLng(41.046937, -73.541493)));
            mMap.addMarker(new MarkerOptions().title("Noroton Heights Station").position(new LatLng(41.070099, -73.49787)));
            mMap.addMarker(new MarkerOptions().title("Darien Station").position(new LatLng(41.078187, -73.472958)));
            mMap.addMarker(new MarkerOptions().title("Rowayton Station").position(new LatLng(41.07851, -73.445535)));
            mMap.addMarker(new MarkerOptions().title("South Norwalk Station").position(new LatLng(41.0957, -73.42185)));
            mMap.addMarker(new MarkerOptions().title("East Norwalk Station").position(new LatLng(41.104, -73.4045)));
            mMap.addMarker(new MarkerOptions().title("Westport Station").position(new LatLng(41.119986, -73.37142)));
            mMap.addMarker(new MarkerOptions().title("Green's Farm Station").position(new LatLng(41.123316, -73.315415)));
            mMap.addMarker(new MarkerOptions().title("Southport Station").position(new LatLng(41.136389, -73.286111)));
            mMap.addMarker(new MarkerOptions().title("Fairfieled Station").position(new LatLng(41.142778, -73.258056)));
            mMap.addMarker(new MarkerOptions().title("Fairfield Metro Station").position(new LatLng(41.1611, -73.2343)));
            mMap.addMarker(new MarkerOptions().title("Bridgeport Station").position(new LatLng(41.1778, -73.1871)));
            mMap.addMarker(new MarkerOptions().title("Stratford Station").position(new LatLng(41.195303, -73.131523)));
            mMap.addMarker(new MarkerOptions().title("Milford Station").position(new LatLng(41.22235, -73.059619)));
            mMap.addMarker(new MarkerOptions().title("West Haven Station").position(new LatLng(41.271142, -72.963199)));
            mMap.addMarker(new MarkerOptions().title("Union Station- New Haven").position(new LatLng(41.2975, -72.926667)));
            mMap.addMarker(new MarkerOptions().title("New Haven State Street Station").position(new LatLng(41.305763, -72.921753)));

            //New Canaan Markers
            mMap.addMarker(new MarkerOptions().title("Stamford Terminal").position(new LatLng(41.046937, -73.541493)));
            mMap.addMarker(new MarkerOptions().title("Glenbrook Station").position(new LatLng(41.0705, -73.5199)));
            mMap.addMarker(new MarkerOptions().title("Srpingdale Station").position(new LatLng(41.0888, -73.5178)));
            mMap.addMarker(new MarkerOptions().title("Talmadge Hill Station").position(new LatLng(41.116, -73.4981)));
            mMap.addMarker(new MarkerOptions().title("New Canaan Station").position(new LatLng(41.1463, -73.4956)));

            //Danbury Markers
            mMap.addMarker(new MarkerOptions().title("Merrit 7 Station").position(new LatLng(41.0957, -73.42185)));
            mMap.addMarker(new MarkerOptions().title("Wilton Station").position(new LatLng(41.1959, -73.4321)));
            mMap.addMarker(new MarkerOptions().title("Cannondale Station").position(new LatLng(41.216667, -73.426667)));
            mMap.addMarker(new MarkerOptions().title("Branchville Station").position(new LatLng(41.2667, -73.4409)));
            mMap.addMarker(new MarkerOptions().title("Redding Station").position(new LatLng(41.3256, -73.4335)));
            mMap.addMarker(new MarkerOptions().title("Bethel Station").position(new LatLng(41.376, -73.418)));
            mMap.addMarker(new MarkerOptions().title("Danbury Station").position(new LatLng(41.3963, -73.4493)));

            //Waterbury Markers
            mMap.addMarker(new MarkerOptions().title("Derby-Shelton Station").position(new LatLng(41.320284, -73.083565)));
            mMap.addMarker(new MarkerOptions().title("Asonia Station").position(new LatLng(41.3442, -73.0799)));
            mMap.addMarker(new MarkerOptions().title("Seymour Station").position(new LatLng(41.3953, -73.0725)));
            mMap.addMarker(new MarkerOptions().title("Beacon Falls Station").position(new LatLng(41.4407, -73.0631)));
            mMap.addMarker(new MarkerOptions().title("Naugatuck Station").position(new LatLng(41.492778, -73.052222)));
            mMap.addMarker(new MarkerOptions().title("Waterbury Station").position(new LatLng(41.5544, -73.047)));

        }
    }

}
