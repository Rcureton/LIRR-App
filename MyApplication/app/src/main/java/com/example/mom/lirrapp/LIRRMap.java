package com.example.mom.lirrapp;

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
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class LIRRMap extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

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
    private List<LatLng> mBabylon;
    private List<LatLng> mRonkonkoma;
    private List<LatLng> mAtlanticTerminal;
    private List<LatLng> mPortWashington;
    private List<LatLng> mLongBeach;
    private List<LatLng> mPennLine;
    private List<LatLng> mMontaukLine;
    private List<LatLng> mOysterBay;
    private List<LatLng> mWestHempstead;
    private List<LatLng> mHempstead;
    private List<LatLng> mPortJefferson;
    private List<LatLng> mFarRockaway;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mblank = (TextView) findViewById(R.id.blank2);
        pb = (ProgressBar) findViewById(R.id.pb);

        final Items items = new Items();


        floatingActionButton = (FloatingActionButton) findViewById(R.id.mapButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                items.setLatitude(latitude);
                items.setLongitude(longitude);

            }
        });
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasSufficientPermissions()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            }
        }



        // Keep methods as short as possible
        plotPoints();
        configureButton();

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    private void plotPoints() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mMap = mapboxMap;
                mapboxMap.setMyLocationEnabled(true);
                new MarkersAsyncTask().execute();

                //Long Island Train Lines

                babylonMarkers(mapboxMap);
                ronkonkomaMarkers(mapboxMap);
                longbeachBranchMarkers(mapboxMap);
                atlanticBranchMarkers(mapboxMap);
                oysterBayBranchMarkers(mapboxMap);
                portJeffersonBranchMarkers(mapboxMap);
                farRockawayBranchMarkers(mapboxMap);
                westHempsteadBranchMarkers(mapboxMap);
                hempsteadBranchMarkers(mapboxMap);
                portWashingtonBranchMarkers(mapboxMap);
                montaukBranchMarkers(mapboxMap);

//
//                //Queens and Brooklyn Train Lines
//                cityLineMarkers(mapboxMap);
//                cityLinePoly(mapboxMap);
//                atlanticTerminalMarkers(mapboxMap);
//                atlanticTerminalPolyline(mapboxMap);

                // Metro North Lines
////                hudsonLinePolyLine(mapboxMap);
//                hudsonLineMarkers(mapboxMap);
//                harlemLinePoly(mapboxMap);
//                harlemLineMarkers(mapboxMap);
//                newHavenLinePoly(mapboxMap);
//                newHavenMarkers(mapboxMap);
//                newCanaanPoly(mapboxMap);
//                newCananMarkers(mapboxMap);
//                danburyMarkers(mapboxMap);
//                danburyPoly(mapboxMap);
//                waterburyMarkers(mapboxMap);
//                waterburyPoly(mapboxMap);


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
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
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
            Toast.makeText(LIRRMap.this, R.string.location_error, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LIRRMap.this, "This device is not supported", Toast.LENGTH_SHORT).show();
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


    private void cityLinePoly(MapboxMap map) {
        List<LatLng> pennPolyline = new ArrayList<>();

        pennPolyline.add(new LatLng(40.699511, -73.808727));//Jamaica
        pennPolyline.add(new LatLng(40.7096, -73.83066));// Kew Gardens
        pennPolyline.add(new LatLng(40.719483, -73.844883));//Forrest Hills
        pennPolyline.add(new LatLng(40.746072, -73.903201));//Woodside
        pennPolyline.add(new LatLng(40.750638, -73.993899));// Penn Station

        map.addPolyline(new PolylineOptions().addAll(pennPolyline).color(Color.parseColor("#009688")));
    }

    private void cityLineMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Kew Gardens Train Station").position(new LatLng(40.7096, -73.83066)));
        map.addMarker(new MarkerOptions().title("Forrest Hills Train Station").position(new LatLng(40.719483, -73.844883)));

    }

    private void atlanticTerminalPolyline(MapboxMap map) {
        List<LatLng> atlanticTermPoly = new ArrayList<>();

        atlanticTermPoly.add(new LatLng(40.699511, -73.808727));//Jamaica
        atlanticTermPoly.add(new LatLng(40.676053, -73.905925));//East New York
        atlanticTermPoly.add(new LatLng(40.67845, -73.9494));// Nostrand Avenue
        atlanticTermPoly.add(new LatLng(40.684226, -73.977234));//Atlantic Terminal

        map.addPolyline(new PolylineOptions().addAll(atlanticTermPoly).color(Color.parseColor("#311B92")));
    }

    private void atlanticTerminalMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("East New York Train Station").position(new LatLng(40.676053, -73.905925)));
        map.addMarker(new MarkerOptions().title("Nostrand Avenue Train Station").position(new LatLng(40.67845, -73.9494)));
        map.addMarker(new MarkerOptions().title("Atlantic Terminal").position(new LatLng(40.684226, -73.977234)));

    }

    private void montaukBranchPolyline(MapboxMap map) {
        List<LatLng> montaukPolyline = new ArrayList<>();

        montaukPolyline.add(new LatLng(41.046793, -71.954452));// Montauk
        montaukPolyline.add(new LatLng(40.98, -72.1325));//Amagansett
        montaukPolyline.add(new LatLng(40.964936, -72.193461));//East Hampton
        montaukPolyline.add(new LatLng(40.939163, -72.309646));//BridgeHampton
        montaukPolyline.add(new LatLng(40.894779, -72.38975));//Southampton
        montaukPolyline.add(new LatLng(40.876464, -72.524566));//Hampton Bays
        montaukPolyline.add(new LatLng(40.830191, -72.650973));//Westhampton
        montaukPolyline.add(new LatLng(40.821224, -72.704853));//Speonk
        montaukPolyline.add(new LatLng(40.7989, -72.86454));//Mastic-Shirley
        montaukPolyline.add(new LatLng(40.773717, -72.943769));//Bellport
        montaukPolyline.add(new LatLng(40.761841, -73.015735));//Patchogue
        montaukPolyline.add(new LatLng(40.740388, -73.086497));//Sayville
        montaukPolyline.add(new LatLng(40.7434, -73.1324));//Oakdale
        montaukPolyline.add(new LatLng(40.740476, -73.17));//Great River
        montaukPolyline.add(new LatLng(40.736, -73.209));//Islip
        montaukPolyline.add(new LatLng(40.72505, -73.253057));//Bayshore
        montaukPolyline.add(new LatLng(40.700614, -73.32421)); //Babylon

        map.addPolyline(new PolylineOptions().addAll(montaukPolyline).color(Color.parseColor("#FF4081")));
    }

    private void montaukBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Montauk Train Station").position(new LatLng(41.046793, -71.954452)));
        map.addMarker(new MarkerOptions().title("Amagansett Train Station").position(new LatLng(40.98, -72.1325)));
        map.addMarker(new MarkerOptions().title("East Hampton Train Station").position(new LatLng(40.964936, -72.193461)));
        map.addMarker(new MarkerOptions().title("Bridgehampton Train Station").position(new LatLng(40.939163, -72.309646)));
        map.addMarker(new MarkerOptions().title("Southampton Train Station").position(new LatLng(40.894779, -72.38975)));
        map.addMarker(new MarkerOptions().title("Hampton Bays Train Station").position(new LatLng(40.876464, -72.524566)));
        map.addMarker(new MarkerOptions().title("Westhampton Train Station").position(new LatLng(40.830191, -72.650973)));
        map.addMarker(new MarkerOptions().title("Speonk Train Station").position(new LatLng(40.821224, -72.704853)));
        map.addMarker(new MarkerOptions().title("Mastic-Shirley Train Station").position(new LatLng(40.7989, -72.86454)));
        map.addMarker(new MarkerOptions().title("Bellport Train Station").position(new LatLng(40.773717, -72.943769)));
        map.addMarker(new MarkerOptions().title("Patchogue Train Station").position(new LatLng(40.761841, -73.015735)));
        map.addMarker(new MarkerOptions().title("Sayville Train Station").position(new LatLng(40.740388, -73.086497)));
        map.addMarker(new MarkerOptions().title("Oakdale Train Station").position(new LatLng(40.7434, -73.1324)));
        map.addMarker(new MarkerOptions().title("Great River Train Station").position(new LatLng(40.740476, -73.17)));
        map.addMarker(new MarkerOptions().title("Islip Train Station").position(new LatLng(40.736, -73.209)));
        map.addMarker(new MarkerOptions().title("Bayshore Train Station").position(new LatLng(40.72505, -73.253057)));


    }

    private void portWashingtonBranchPolyLine(MapboxMap map) {
        List<LatLng> portWashPoly = new ArrayList<>();

        portWashPoly.add(new LatLng(40.829349, -73.68733));//Port Washington
        portWashPoly.add(new LatLng(40.810687, -73.695216));//Plandome Station
        portWashPoly.add(new LatLng(40.79669, -73.6999996));//Manhasset
        portWashPoly.add(new LatLng(40.787235, -73.725986));//Great Neck
        portWashPoly.add(new LatLng(40.775, -73.740744));//Little Neck
        portWashPoly.add(new LatLng(40.768, -73.7496));//Douglaston
        portWashPoly.add(new LatLng(40.763105, -73.771804));//Bayside
        portWashPoly.add(new LatLng(40.76139, -73.7905));//Auburndale
        portWashPoly.add(new LatLng(40.761626, -73.801383));//Broadway Station
        portWashPoly.add(new LatLng(40.762703, -73.81446));//Murray Hill
        portWashPoly.add(new LatLng(40.757989, -73.831086));//Flusing-Main Street
        portWashPoly.add(new LatLng(40.752516, -73.843725));// Mets Stadium- Willets Point
        portWashPoly.add(new LatLng(40.746072, -73.903201));// Woodside
        portWashPoly.add(new LatLng(40.750638, -73.993899));//Penn Station

        map.addPolyline(new PolylineOptions().addAll(portWashPoly).color(Color.parseColor("#E65100")));
    }

    private void portWashingtonBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Port Washington Train Station").position(new LatLng(40.829349, -73.68733)));
        map.addMarker(new MarkerOptions().title("Plandome Train Station").position(new LatLng(40.810687, -73.695216)));
        map.addMarker(new MarkerOptions().title("Manhasset Train Station").position(new LatLng(40.79669, -73.6999996)));
        map.addMarker(new MarkerOptions().title("Great Neck Train Station").position(new LatLng(40.787235, -73.725986)));
        map.addMarker(new MarkerOptions().title("Little Neck Train Station").position(new LatLng(40.775, -73.740744)));
        map.addMarker(new MarkerOptions().title("Douglaston Train Station").position(new LatLng(40.768, -73.7496)));
        map.addMarker(new MarkerOptions().title("Bayside Train Station").position(new LatLng(40.763105, -73.771804)));
        map.addMarker(new MarkerOptions().title("Auburndale Train Station").position(new LatLng(40.76139, -73.7905)));
        map.addMarker(new MarkerOptions().title("Broadway Train Station").position(new LatLng(40.761626, -73.801383)));
        map.addMarker(new MarkerOptions().title("Murray Hill Train Station").position(new LatLng(40.762703, -73.81446)));
        map.addMarker(new MarkerOptions().title("Flushing- Main Street Train Station").position(new LatLng(40.757989, -73.831086)));
        map.addMarker(new MarkerOptions().title("Mets Stadium - Willets Point Train Station").position(new LatLng(40.752516, -73.843725)));
        map.addMarker(new MarkerOptions().title("Woodside Train Station").position(new LatLng(40.746072, -73.903201)));
        map.addMarker(new MarkerOptions().title("Penn Station").position(new LatLng(40.750638, -73.993899)));
    }

    private void westHempsteadBranchPolyline(MapboxMap map) {
        List<LatLng> westHempsteadPolyline = new ArrayList<>();

        westHempsteadPolyline.add(new LatLng(40.701944, -73.641667));// West Hempstead
        westHempsteadPolyline.add(new LatLng(40.694722, -73.646111));// Hempstead Gardens
        westHempsteadPolyline.add(new LatLng(40.685556, -73.652222));// Lakeview
        westHempsteadPolyline.add(new LatLng(40.675556, -73.668611));//Malverne
        westHempsteadPolyline.add(new LatLng(40.668278, -73.681417));// Westwood
        westHempsteadPolyline.add(new LatLng(40.661483, -73.704679));//Valley Stream

        map.addPolyline(new PolylineOptions().addAll(westHempsteadPolyline).color(Color.parseColor("#E040FB")));
    }

    private void westHempsteadBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("West Hempstead Train Station").position(new LatLng(40.701944, -73.641667)));
        map.addMarker(new MarkerOptions().title("Hempstead Gardens Train Station").position(new LatLng(40.694722, -73.646111)));
        map.addMarker(new MarkerOptions().title("Lakeview Train Station").position(new LatLng(40.685556, -73.652222)));
        map.addMarker(new MarkerOptions().title("Malverne Train Station").position(new LatLng(40.675556, -73.668611)));
        map.addMarker(new MarkerOptions().title("Westwood Train Station").position(new LatLng(40.668278, -73.681417)));

    }

    private void hempsteadBranchPolyline(MapboxMap map) {
        List<LatLng> hempsteadBranchPoly = new ArrayList<>();

        hempsteadBranchPoly.add(new LatLng(40.713102, -73.625307));//Hempstead
        hempsteadBranchPoly.add(new LatLng(40.721234, -73.629405));//Country Life Press
        hempsteadBranchPoly.add(new LatLng(40.723136, -73.64007));//Garden City
        hempsteadBranchPoly.add(new LatLng(40.722933, -73.662751));//Nassau Blvd
        hempsteadBranchPoly.add(new LatLng(40.723006, -73.68114));//Stewart Manor
        hempsteadBranchPoly.add(new LatLng(40.724622, -73.706398));//Floral Park

        map.addPolyline(new PolylineOptions().addAll(hempsteadBranchPoly).color(Color.parseColor("#01579B")));
    }

    private void hempsteadBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Hempstead Train Station").position(new LatLng(40.713102, -73.625307)));
        map.addMarker(new MarkerOptions().title("Country Life Press Train Station").position(new LatLng(40.721234, -73.629405)));
        map.addMarker(new MarkerOptions().title("Garden City Train Station").position(new LatLng(40.723136, -73.64007)));
        map.addMarker(new MarkerOptions().title("Nassau Boulevard Train Station").position(new LatLng(40.722933, -73.662751)));
        map.addMarker(new MarkerOptions().title("Stewart Manor Train Station").position(new LatLng(40.723006, -73.68114)));

    }


    private void ronkonkomaTrainPolyline(MapboxMap map) {
        List<LatLng> ronkonkomaLine = new ArrayList<>();

        ronkonkomaLine.add(new LatLng(40.699511, -73.808727)); //Jamaica
        ronkonkomaLine.add(new LatLng(40.7102, -73.7666)); //Hollis
        ronkonkomaLine.add(new LatLng(40.717469, -73.73638)); //Queens Village
        ronkonkomaLine.add(new LatLng(40.724622, -73.706398)); //Floral Park
        ronkonkomaLine.add(new LatLng(40.730932, -73.680569)); //New Hyde Park
        ronkonkomaLine.add(new LatLng(40.735164, -73.662523)); //Merillion Avenue
        ronkonkomaLine.add(new LatLng(40.740291, -73.641025)); //Mineola
        ronkonkomaLine.add(new LatLng(40.749195, -73.603705)); //Carle Place
        ronkonkomaLine.add(new LatLng(40.753404, -73.586273)); //Westbury
        ronkonkomaLine.add(new LatLng(40.767101, -73.528686)); //Hicksville
        ronkonkomaLine.add(new LatLng(40.742994, -73.483359)); //Bethpage
        ronkonkomaLine.add(new LatLng(40.735665, -73.441713)); //Farmingdale
        ronkonkomaLine.add(new LatLng(40.745339, -73.399572)); //Pinelawn
        ronkonkomaLine.add(new LatLng(40.754889, -73.35781)); //Wyandanch
        ronkonkomaLine.add(new LatLng(40.76948, -73.293577)); //Deer Park
        ronkonkomaLine.add(new LatLng(40.780826, -73.243607)); //Brentwood
        ronkonkomaLine.add(new LatLng(40.79188, -73.19467)); //Central Islip
        ronkonkomaLine.add(new LatLng(40.808088, -73.1059)); //Ronkonkoma
        ronkonkomaLine.add(new LatLng(40.817356, -72.999159));//Medford
        ronkonkomaLine.add(new LatLng(40.825656, -72.915841));//Yaphank
        ronkonkomaLine.add(new LatLng(40.919763, -72.6669));//Riverhead
        ronkonkomaLine.add(new LatLng(40.991761, -72.536009));//Mattituck
        ronkonkomaLine.add(new LatLng(41.066295, -72.427859));//Southold
        ronkonkomaLine.add(new LatLng(41.099722, -72.363611));//Greenport

        map.addPolyline(new PolylineOptions().addAll(ronkonkomaLine).color(Color.parseColor("#9C27B0")));
    }

    private void ronkonkomaMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Jamaica Train Station").position(new LatLng(40.699511, -73.808727)));
        map.addMarker(new MarkerOptions().title("Hollis Train Station").position(new LatLng(40.7102, -73.7666)));
        map.addMarker(new MarkerOptions().title("Queens Village Train Station").position(new LatLng(40.717469, -73.73638)));
        map.addMarker(new MarkerOptions().title("Floral Park Train Station").position(new LatLng(40.724622, -73.706398)));
        map.addMarker(new MarkerOptions().title("New Hyde Park Train Station").position(new LatLng(40.730932, -73.680569)));
        map.addMarker(new MarkerOptions().title("Merillion Avenue Train Station").position(new LatLng(40.735164, -73.662523)));
        map.addMarker(new MarkerOptions().title("Mineola Train Station").position(new LatLng(40.740291, -73.641025)));
        map.addMarker(new MarkerOptions().title("Carle Place Train Station").position(new LatLng(40.749195, -73.603705)));
        map.addMarker(new MarkerOptions().title("Westbury Train Station").position(new LatLng(40.753404, -73.586273)));
        map.addMarker(new MarkerOptions().title("Hicksville Train Station").position(new LatLng(40.767101, -73.528686)));
        map.addMarker(new MarkerOptions().title("Bethpage Train Station").position(new LatLng(40.742994, -73.483359)));
        map.addMarker(new MarkerOptions().title("Farmingdale Train Station").position(new LatLng(40.735665, -73.441713)));
        map.addMarker(new MarkerOptions().title("Pinelawn Train Station").position(new LatLng(40.745339, -73.399572)));
        map.addMarker(new MarkerOptions().title("Wyandanch Park Station").position(new LatLng(40.754889, -73.35781)));
        map.addMarker(new MarkerOptions().title("Deer Park Train Station").position(new LatLng(40.76948, -73.293577)));
        map.addMarker(new MarkerOptions().title("Brentwood Train Station").position(new LatLng(40.780826, -73.243607)));
        map.addMarker(new MarkerOptions().title("Central Islip Train Station").position(new LatLng(40.79188, -73.19467)));
        map.addMarker(new MarkerOptions().title("Ronkonkoma Train Station").position(new LatLng(40.808088, -73.1059)));
        map.addMarker(new MarkerOptions().title("Medford Train Station").position(new LatLng(40.817356, -72.999159)));
        map.addMarker(new MarkerOptions().title("Yaphank Train Station").position(new LatLng(40.825656, -72.915841)));
        map.addMarker(new MarkerOptions().title("Riverhead Train Station").position(new LatLng(40.919763, -72.6669)));
        map.addMarker(new MarkerOptions().title("Mattituck Train Station").position(new LatLng(40.991761, -72.536009)));
        map.addMarker(new MarkerOptions().title("Southold Train Station").position(new LatLng(41.066295, -72.427859)));
        map.addMarker(new MarkerOptions().title("Greenport Train Station").position(new LatLng(41.099722, -72.363611)));
    }

    private void drawPolyLinesBabylon(MapboxMap map) {
        List<LatLng> babylonTrainLine = new ArrayList<>();

        babylonTrainLine.add(new LatLng(40.699511, -73.808727)); //Jamaica
        babylonTrainLine.add(new LatLng(40.691052, -73.765426)); // St.Albans
        babylonTrainLine.add(new LatLng(40.65603, -73.6758)); //Lynbrook
        babylonTrainLine.add(new LatLng(40.6583, -73.6466)); //Rockville Centre
        babylonTrainLine.add(new LatLng(40.656746, -73.607444)); //Baldwin
        babylonTrainLine.add(new LatLng(40.657425, -73.582601)); //Freeport
        babylonTrainLine.add(new LatLng(40.663769, -73.550709)); //Merrick
        babylonTrainLine.add(new LatLng(40.668766, -73.528833)); //Bellmore
        babylonTrainLine.add(new LatLng(40.672937, -73.509098)); //Wantagh
        babylonTrainLine.add(new LatLng(40.67573, -73.486454)); //Seaford
        babylonTrainLine.add(new LatLng(40.676901, -73.469052)); //Massapequa
        babylonTrainLine.add(new LatLng(40.677851, -73.45474)); //Massapequa Park
        babylonTrainLine.add(new LatLng(40.680263, -73.420472)); //Amityville
        babylonTrainLine.add(new LatLng(40.681, -73.399)); //Copiague
        babylonTrainLine.add(new LatLng(40.688243, -73.369242)); //Lindenhurst
        babylonTrainLine.add(new LatLng(40.700614, -73.32421)); //Babylon

        map.addPolyline(new PolylineOptions()
                .addAll(babylonTrainLine).color(Color.parseColor("#3bb2d0")));
    }

    private void babylonMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Jamaica Train Station").position(new LatLng(40.699511, -73.808727)));
        map.addMarker(new MarkerOptions().title("St. Albans Train Station").position(new LatLng(40.691052, -73.765426)));
        map.addMarker(new MarkerOptions().title("Lynbrook Train Sation").position(new LatLng(40.65603, -73.6758)));
        map.addMarker(new MarkerOptions().title("Rockville Centre Train Station").position(new LatLng(40.6583, -73.6466)));
        map.addMarker(new MarkerOptions().title("Baldwin Train Station").position(new LatLng(40.656746, -73.607444)));
        map.addMarker(new MarkerOptions().title("Freeport Train Station").position(new LatLng(40.657425, -73.582601)));
        map.addMarker(new MarkerOptions().title("Merrick Train Station").position(new LatLng(40.663769, -73.550709)));
        map.addMarker(new MarkerOptions().title("Bellmore Train Station").position(new LatLng(40.668766, -73.528833)));
        map.addMarker(new MarkerOptions().title("Wantagh Train Station").position(new LatLng(40.672937, -73.509098)));
        map.addMarker(new MarkerOptions().title("Seaford Train Station").position(new LatLng(40.67573, -73.486454)));
        map.addMarker(new MarkerOptions().title("Massapequa Train Station").position(new LatLng(40.676901, -73.469052)));
        map.addMarker(new MarkerOptions().title("Massapequa Park Station").position(new LatLng(40.677851, -73.45474)));
        map.addMarker(new MarkerOptions().title("Amityville Train Station").position(new LatLng(40.680263, -73.420472)));
        map.addMarker(new MarkerOptions().title("Copiague Train Station").position(new LatLng(40.681, -73.399)));
        map.addMarker(new MarkerOptions().title("Lindenhurst Train Station").position(new LatLng(40.688243, -73.369242)));
        map.addMarker(new MarkerOptions().title("Babylon Train Station").position(new LatLng(40.700614, -73.32421)));


    }

    private void longBeachPolyLine(MapboxMap map) {
        List<LatLng> longbeachBranch = new ArrayList<>();

        longbeachBranch.add(new LatLng(40.699511, -73.808727)); //Jamaica
        longbeachBranch.add(new LatLng(40.691052, -73.765426)); // St.Albans
        longbeachBranch.add(new LatLng(40.65603, -73.6758)); //Lynbrook
        longbeachBranch.add(new LatLng(40.648272, -73.663915)); //Centre Avenue
        longbeachBranch.add(new LatLng(40.640938, -73.657067)); //East Rockaway
        longbeachBranch.add(new LatLng(40.634986, -73.654613)); //Oceanside
        longbeachBranch.add(new LatLng(40.600433, -73.655388)); //Island Park
        longbeachBranch.add(new LatLng(40.589368, -73.664854)); //Long Beach

        map.addPolyline(new PolylineOptions().addAll(longbeachBranch)
                .color(Color.parseColor("#009688")));
    }

    private void longbeachBranchMarkers(MapboxMap map) {

        map.addMarker(new MarkerOptions().title("Lynbrook Train Station").position(new LatLng(40.65603, -73.6758)));
        map.addMarker(new MarkerOptions().title("Centre Avenue Train Station").position(new LatLng(40.648272, -73.663915)));
        map.addMarker(new MarkerOptions().title("East Rockaway Train Station").position(new LatLng(40.640938, -73.657067)));
        map.addMarker(new MarkerOptions().title("Oceanside Train Station").position(new LatLng(40.634986, -73.654613)));
        map.addMarker(new MarkerOptions().title("Island Park Train Station").position(new LatLng(40.600433, -73.655388)));
        map.addMarker(new MarkerOptions().title("Long Beach Train Station").position(new LatLng(40.589368, -73.664854)));
    }

    private void atlanticBranchPolyline(MapboxMap map) {
        List<LatLng> atlanticPolylines = new ArrayList<>();

        atlanticPolylines.add(new LatLng(40.699511, -73.808727)); //Jamaica
        atlanticPolylines.add(new LatLng(40.675022, -73.764897));//Locust Manor
        atlanticPolylines.add(new LatLng(40.66853, -73.7518));//Laurelton
        atlanticPolylines.add(new LatLng(40.6659, -73.7356));//Rosedale
        atlanticPolylines.add(new LatLng(40.661483, -73.704679));//Valley Stream

        map.addPolyline(new PolylineOptions().addAll(atlanticPolylines).color(Color.parseColor("#FFA000")));
    }

    private void atlanticBranchMarkers(MapboxMap map) {

        map.addMarker(new MarkerOptions().title("Jamaica Train Station").position(new LatLng(40.699511, -73.808727)));
        map.addMarker(new MarkerOptions().title("Locust Manor Train Station").position(new LatLng(40.675022, -73.764897)));
        map.addMarker(new MarkerOptions().title("Laurelton Train Station").position(new LatLng(40.66853, -73.7518)));
        map.addMarker(new MarkerOptions().title("Rosedale Train Station").position(new LatLng(40.6659, -73.7356)));
        map.addMarker(new MarkerOptions().title("Valley Stream Train Station").position(new LatLng(40.661483, -73.704679)));
    }

    private void oysterBayBranchPolyLine(MapboxMap map) {
        List<LatLng> oysterBayLine = new ArrayList<>();

        oysterBayLine.add(new LatLng(40.874992, -73.531603));//Oyster Bay
        oysterBayLine.add(new LatLng(40.874251, -73.598678));//Locust Valley
        oysterBayLine.add(new LatLng(40.865189, -73.616976));// Glen Cove
        oysterBayLine.add(new LatLng(40.857862, -73.621461));//Glen Street
        oysterBayLine.add(new LatLng(40.852564, -73.625408));//Sea Cliff
        oysterBayLine.add(new LatLng(40.832284, -73.626128));// Glen Head
        oysterBayLine.add(new LatLng(40.815547, -73.626916));//Greenvale
        oysterBayLine.add(new LatLng(40.79072, -73.643267));//Roslyn
        oysterBayLine.add(new LatLng(40.771872, -73.641679));// Albertson
        oysterBayLine.add(new LatLng(40.75614, -73.639426));// East Williston
        oysterBayLine.add(new LatLng(40.740291, -73.641025)); //Mineola
        oysterBayLine.add(new LatLng(40.735164, -73.662523)); //Merillion Avenue
        oysterBayLine.add(new LatLng(40.730932, -73.680569)); //New Hyde Park
        oysterBayLine.add(new LatLng(40.724622, -73.706398)); //Floral Park
        oysterBayLine.add(new LatLng(40.717469, -73.73638)); //Queens Village
        oysterBayLine.add(new LatLng(40.7102, -73.7666)); //Hollis
        oysterBayLine.add(new LatLng(40.699511, -73.808727)); //Jamaica

        map.addPolyline(new PolylineOptions().addAll(oysterBayLine).color(Color.parseColor("#00695C")));

    }

    private void oysterBayBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Oyster Bay Train Station").position(new LatLng(40.874992, -73.531603)));
        map.addMarker(new MarkerOptions().title("Locust Valley Train Station").position(new LatLng(40.874251, -73.598678)));
        map.addMarker(new MarkerOptions().title("Glen Cove Train Station").position(new LatLng(40.865189, -73.616976)));
        map.addMarker(new MarkerOptions().title("Glen Street Train Station").position(new LatLng(40.857862, -73.621461)));
        map.addMarker(new MarkerOptions().title("Sea Cliff Train Station").position(new LatLng(40.852564, -73.625408)));
        map.addMarker(new MarkerOptions().title("Glen Head Train Station").position(new LatLng(40.832284, -73.626128)));
        map.addMarker(new MarkerOptions().title("Greenvale Train Station").position(new LatLng(40.815547, -73.626916)));
        map.addMarker(new MarkerOptions().title("Roslyn Train Station").position(new LatLng(40.79072, -73.643267)));
        map.addMarker(new MarkerOptions().title("Albertson Train Station").position(new LatLng(40.771872, -73.641679)));
        map.addMarker(new MarkerOptions().title("East Williston Train Station").position(new LatLng(40.75614, -73.639426)));

    }

    private void portJeffersonBranchPolyLine(MapboxMap map) {
        List<LatLng> portJeffLine = new ArrayList<>();

        portJeffLine.add(new LatLng(40.934719, -73.053692));// Port Jefferson
        portJeffLine.add(new LatLng(40.920275, -73.128514));// Stony Brook
        portJeffLine.add(new LatLng(40.883272, -73.158153));// Saint James
        portJeffLine.add(new LatLng(40.856264, -73.199272));// Smithtown
        portJeffLine.add(new LatLng(40.883719, -73.255819));//Kings Park
        portJeffLine.add(new LatLng(40.880714, -73.3285));//Northport
        portJeffLine.add(new LatLng(40.868658, -73.362972));// Greenlawn
        portJeffLine.add(new LatLng(40.852692, -73.410639));//Huntington
        portJeffLine.add(new LatLng(40.835056, -73.451611));//Cold Spring Harbor
        portJeffLine.add(new LatLng(40.824892, -73.500492));//Syosset
        portJeffLine.add(new LatLng(40.767101, -73.528686));//Hicksville

        map.addPolyline(new PolylineOptions().addAll(portJeffLine).color(Color.parseColor("#FF5722")));
    }

    private void portJeffersonBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Port Jefferson Train Station").position(new LatLng(40.934719, -73.053692)));
        map.addMarker(new MarkerOptions().title("Stony Brook Train Station").position(new LatLng(40.920275, -73.128514)));
        map.addMarker(new MarkerOptions().title("Saint James Train Station").position(new LatLng(40.883272, -73.158153)));
        map.addMarker(new MarkerOptions().title("Smithtown Train Station").position(new LatLng(40.856264, -73.199272)));
        map.addMarker(new MarkerOptions().title("Kings Park Train Station").position(new LatLng(40.883719, -73.255819)));
        map.addMarker(new MarkerOptions().title("Northport Train Station").position(new LatLng(40.880714, -73.3285)));
        map.addMarker(new MarkerOptions().title("Greenlawn Train Station").position(new LatLng(40.868658, -73.362972)));
        map.addMarker(new MarkerOptions().title("Huntington Train Station").position(new LatLng(40.852692, -73.410639)));
        map.addMarker(new MarkerOptions().title("Cold Spring Harbor Train Station").position(new LatLng(40.835056, -73.451611)));
        map.addMarker(new MarkerOptions().title("Syosset Train Station").position(new LatLng(40.824892, -73.500492)));

    }

    private void farRockawayBranchPolyline(MapboxMap map) {
        List<LatLng> farRockawayLine = new ArrayList<>();

        farRockawayLine.add(new LatLng(40.608610, -73.750792));//Far Rockaway
        farRockawayLine.add(new LatLng(40.612291, -73.74431));//Inwood
        farRockawayLine.add(new LatLng(40.615638, -73.736050));//Lawrence
        farRockawayLine.add(new LatLng(40.622214, -73.727121));//Cedarhurst
        farRockawayLine.add(new LatLng(40.631298, -73.713740));//Woodmere
        farRockawayLine.add(new LatLng(40.636737, -73.705151));//Hewlett
        farRockawayLine.add(new LatLng(40.649927, -73.701694));//Gibson
        farRockawayLine.add(new LatLng(40.661483, -73.704679)); //Valley Stream

        map.addPolyline(new PolylineOptions().addAll(farRockawayLine).color(Color.parseColor("#64FFDA")));
    }

    private void farRockawayBranchMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Far Rockaway Train Station").position(new LatLng(40.608610, -73.750792)));
        map.addMarker(new MarkerOptions().title("Inwood Train Station").position(new LatLng(40.612291, -73.74431)));
        map.addMarker(new MarkerOptions().title("Lawrence Train Station").position(new LatLng(40.615638, -73.736050)));
        map.addMarker(new MarkerOptions().title("Cedarhurst Train Station").position(new LatLng(40.622214, -73.727121)));
        map.addMarker(new MarkerOptions().title("Woodmere Train Station").position(new LatLng(40.631298, -73.713740)));
        map.addMarker(new MarkerOptions().title("Hewlett Train Station").position(new LatLng(40.636737, -73.705151)));
        map.addMarker(new MarkerOptions().title("Gibson Train Station").position(new LatLng(40.649927, -73.701694)));

    }

    private void hudsonLinePolyLine(MapboxMap map) {
        List<LatLng> hudsonLine = new ArrayList<>();

        hudsonLine.add(new LatLng(40.7528, -73.976522));//Grand Central Terminal
        hudsonLine.add(new LatLng(40.8052, -73.939));// Harlem-125th Street
        hudsonLine.add(new LatLng(40.825375, -73.930267));//Yankee Stadium- 153rd Street
        hudsonLine.add(new LatLng(40.854, -73.9199));//Morris Heights
        hudsonLine.add(new LatLng(40.8614, -73.9147));//University Heights
        hudsonLine.add(new LatLng(40.8747, -73.912));//Marble Hill
        hudsonLine.add(new LatLng(40.8789, -73.9227));//Spuyten Duyvil
        hudsonLine.add(new LatLng(40.90444, -73.9139));//Riverdale
        hudsonLine.add(new LatLng(40.9238, -73.9056));//Ludlow
        hudsonLine.add(new LatLng(40.9356, -73.9023));//Yonkers
        hudsonLine.add(new LatLng(40.9506, -73.8991));//Glenwood
        hudsonLine.add(new LatLng(40.9721, -73.8896));//Greystone
        hudsonLine.add(new LatLng(40.9946, -73.8847));//Hastings on Hudson
        hudsonLine.add(new LatLng(41.0125, -73.879444));//Dobbs Ferry
        hudsonLine.add(new LatLng(41.027, -73.8769));//Ardsley on Hudson
        hudsonLine.add(new LatLng(41.0395, -73.8733));//Irvington
        hudsonLine.add(new LatLng(41.0755, -73.8656));//Tarrytown
        hudsonLine.add(new LatLng(41.094722, -73.869444));//Philipse Manor
        hudsonLine.add(new LatLng(41.137, -73.8664));//Scarborough
        hudsonLine.add(new LatLng(41.156, -73.8696));//Ossining
        hudsonLine.add(new LatLng(41.1898, -73.8827));//Croton-Harmon Station
        hudsonLine.add(new LatLng(41.247, -73.9232));//Cortlandt Station
        hudsonLine.add(new LatLng(41.285, -73.930833));//Peekskill
        hudsonLine.add(new LatLng(41.3324, -73.9709));//Manitou
        hudsonLine.add(new LatLng(41.3828, -73.9471));//Garrison
        hudsonLine.add(new LatLng(41.4152, -73.9585));//Cold Spring
        hudsonLine.add(new LatLng(41.4508, -73.9829));//Breakneck Ridge
        hudsonLine.add(new LatLng(41.5055, -73.9853));//Beacon
        hudsonLine.add(new LatLng(41.5889, -73.9479));//New Hamburg
        hudsonLine.add(new LatLng(41.707222, -73.938333));//Poughkeepsie

        map.addPolyline(new PolylineOptions().addAll(hudsonLine).color(Color.parseColor("#8BC34A")));

    }

    private void hudsonLineMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Grand Central Terminal").position(new LatLng(40.7528, -73.976522)));
        map.addMarker(new MarkerOptions().title("Harlem 125th Street Station").position(new LatLng(40.8052, -73.939)));
        map.addMarker(new MarkerOptions().title("Yankee Stadium 153rd Street Station").position(new LatLng(40.825375, -73.930267)));
        map.addMarker(new MarkerOptions().title("Morris Heights Station").position(new LatLng(40.854, -73.9199)));
        map.addMarker(new MarkerOptions().title("University Heights Station").position(new LatLng(40.8614, -73.9147)));
        map.addMarker(new MarkerOptions().title("Marble Hill Station").position(new LatLng(40.8747, -73.912)));
        map.addMarker(new MarkerOptions().title("Spuyten Duyvil Station").position(new LatLng(40.8789, -73.9227)));
        map.addMarker(new MarkerOptions().title("Riverdale Station").position(new LatLng(40.90444, -73.9139)));
        map.addMarker(new MarkerOptions().title("Ludlow Station").position(new LatLng(40.9238, -73.9056)));
        map.addMarker(new MarkerOptions().title("Yonkers Station").position(new LatLng(40.9356, -73.9023)));
        map.addMarker(new MarkerOptions().title("Glenwood Station").position(new LatLng(40.9506, -73.8991)));
        map.addMarker(new MarkerOptions().title("Greystone Station").position(new LatLng(40.9721, -73.8896)));
        map.addMarker(new MarkerOptions().title("Hastings on Hudson Station").position(new LatLng(40.9946, -73.8847)));
        map.addMarker(new MarkerOptions().title("Dobbs Ferry Station").position(new LatLng(41.0125, -73.879444)));
        map.addMarker(new MarkerOptions().title("Ardsley on Hudson Station").position(new LatLng(41.027, -73.8769)));
        map.addMarker(new MarkerOptions().title("Irvington Station").position(new LatLng(41.0395, -73.8733)));
        map.addMarker(new MarkerOptions().title("Tarrytown Station").position(new LatLng(41.0755, -73.8656)));
        map.addMarker(new MarkerOptions().title("Philipse Manor Station").position(new LatLng(41.094722, -73.869444)));
        map.addMarker(new MarkerOptions().title("Scarborough Station").position(new LatLng(41.137, -73.8664)));
        map.addMarker(new MarkerOptions().title("Ossining Station").position(new LatLng(41.156, -73.8696)));
        map.addMarker(new MarkerOptions().title("Croton-Harmon Station").position(new LatLng(41.1898, -73.8827)));
        map.addMarker(new MarkerOptions().title("Cortlandt Station").position(new LatLng(41.247, -73.9232)));
        map.addMarker(new MarkerOptions().title("Peekskill Station").position(new LatLng(41.285, -73.930833)));
        map.addMarker(new MarkerOptions().title("Manitou Station").position(new LatLng(41.3324, -73.9709)));
        map.addMarker(new MarkerOptions().title("Garrison Station").position(new LatLng(41.3828, -73.9471)));
        map.addMarker(new MarkerOptions().title("Cold Spring Station").position(new LatLng(41.4152, -73.9585)));
        map.addMarker(new MarkerOptions().title("Breakneck Ridge Station").position(new LatLng(41.4508, -73.9829)));
        map.addMarker(new MarkerOptions().title("Beacon Station").position(new LatLng(41.5055, -73.9853)));
        map.addMarker(new MarkerOptions().title("New Hamburg Station").position(new LatLng(41.5889, -73.9479)));
        map.addMarker(new MarkerOptions().title("Poughkeepsie Station").position(new LatLng(41.707222, -73.938333)));


    }

    private void harlemLinePoly(MapboxMap map) {
        List<LatLng> harlemline = new ArrayList<>();

        harlemline.add(new LatLng(40.7528, -73.976522));//Grand Central Terminal
        harlemline.add(new LatLng(40.8052, -73.939));// Harlem-125th Street
        harlemline.add(new LatLng(40.8257, -73.9154));//Melrose Station
        harlemline.add(new LatLng(40.8472, -73.8997));//Tremont
        harlemline.add(new LatLng(40.861534, -73.890561));//Fordham
        harlemline.add(new LatLng(40.8671, -73.8819));//Botanical Garden
        harlemline.add(new LatLng(40.8788, -73.8707));//Williams Bridge
        harlemline.add(new LatLng(40.8955, -73.8628));//Woodlawn Station
        harlemline.add(new LatLng(40.9062, -73.8554));//Wakefield
        harlemline.add(new LatLng(40.913, -73.8502));//Mount Vernon West
        harlemline.add(new LatLng(40.927, -73.84));//Fleetwood
        harlemline.add(new LatLng(40.941, -73.8351));//Bronxville
        harlemline.add(new LatLng(40.9505, -73.8284));//Tuckahoe
        harlemline.add(new LatLng(40.959, -73.8209));//Crestwood
        harlemline.add(new LatLng(40.9899, -73.8083));//Scarsdale
        harlemline.add(new LatLng(41.011111, -73.795833));//Hartsdale
        harlemline.add(new LatLng(41.0338, -73.7747));//White Plains
        harlemline.add(new LatLng(41.051389, -73.7725));//North White Plains
        harlemline.add(new LatLng(41.0732, -73.7729));//Valhalla
        harlemline.add(new LatLng(41.096, -73.7938));//Mount Pleasant
        harlemline.add(new LatLng(41.109, -73.796));//Hawthorne
        harlemline.add(new LatLng(41.1348, -73.7923));//Pleasantville
        harlemline.add(new LatLng(41.1579, -73.7749));//Chappaqua
        harlemline.add(new LatLng(41.2084, -73.7296));//Mount Kisko
        harlemline.add(new LatLng(41.2373, -73.7001));//Bedford Hills
        harlemline.add(new LatLng(41.2598, -73.6841));//Katonah
        harlemline.add(new LatLng(41.2945, -73.6776));//Goldens Bridge
        harlemline.add(new LatLng(41.3256, -73.659));//Purdy's
        harlemline.add(new LatLng(41.3479, -73.6622));//Croton Falls
        harlemline.add(new LatLng(41.3947, -73.6198));//Brewster
        harlemline.add(new LatLng(41.4127, -73.623));//Southeast
        harlemline.add(new LatLng(41.51174, -73.60428));//Patterson
        harlemline.add(new LatLng(41.5646, -73.6004));//Pawling
        harlemline.add(new LatLng(41.5929, -73.588));//Appalachian Trail
        harlemline.add(new LatLng(41.6374, -73.5717));//Harlem Valley- Wingdale
        harlemline.add(new LatLng(41.7427, -73.5762));//Dover Plains
        harlemline.add(new LatLng(41.7795, -73.559));//Tenmile River
        harlemline.add(new LatLng(41.8147, -73.5623));//Wassiac

        map.addPolyline(new PolylineOptions().addAll(harlemline).color(Color.parseColor("#09A6C9")));

    }

    private void harlemLineMarkers(MapboxMap map) {

        map.addMarker(new MarkerOptions().title("Melrose Station").position(new LatLng(40.8257, -73.9154)));
        map.addMarker(new MarkerOptions().title("Tremont Station").position(new LatLng(40.8472, -73.8997)));
        map.addMarker(new MarkerOptions().title("Fordham Station").position(new LatLng(40.861534, -73.890561)));
        map.addMarker(new MarkerOptions().title("Botanical Gardens Station").position(new LatLng(40.8671, -73.8819)));
        map.addMarker(new MarkerOptions().title("Williams Bridge Station").position(new LatLng(40.8788, -73.8707)));
        map.addMarker(new MarkerOptions().title("Woodlawn Station").position(new LatLng(40.8955, -73.8628)));
        map.addMarker(new MarkerOptions().title("Wakefield Station").position(new LatLng(40.9062, -73.8554)));
        map.addMarker(new MarkerOptions().title("Mount Vernon West Station").position(new LatLng(40.913, -73.8502)));
        map.addMarker(new MarkerOptions().title("Fleetwood Station").position(new LatLng(40.927, -73.84)));
        map.addMarker(new MarkerOptions().title("Bronxville Station").position(new LatLng(40.941, -73.8351)));
        map.addMarker(new MarkerOptions().title("Tuckahoe Station").position(new LatLng(40.9505, -73.8284)));
        map.addMarker(new MarkerOptions().title("Crestwood Station").position(new LatLng(40.959, -73.8209)));
        map.addMarker(new MarkerOptions().title("Scarsdale Station").position(new LatLng(40.9899, -73.8083)));
        map.addMarker(new MarkerOptions().title("Hartsdale Station").position(new LatLng(41.011111, -73.795833)));
        map.addMarker(new MarkerOptions().title("White Plains Station").position(new LatLng(41.0338, -73.7747)));
        map.addMarker(new MarkerOptions().title("North White Plains Station").position(new LatLng(41.051389, -73.7725)));
        map.addMarker(new MarkerOptions().title("Valhalla Station").position(new LatLng(41.0732, -73.7729)));
        map.addMarker(new MarkerOptions().title("Mount Pleasant Station").position(new LatLng(41.096, -73.7938)));
        map.addMarker(new MarkerOptions().title("Hawthorne Station").position(new LatLng(41.109, -73.796)));
        map.addMarker(new MarkerOptions().title("Pleasantville Station").position(new LatLng(41.1348, -73.7923)));
        map.addMarker(new MarkerOptions().title("Chappaqua Station").position(new LatLng(41.1579, -73.7749)));
        map.addMarker(new MarkerOptions().title("Mount Kisko Station").position(new LatLng(41.2084, -73.7296)));
        map.addMarker(new MarkerOptions().title("Bedford Hills Station").position(new LatLng(41.2373, -73.7001)));
        map.addMarker(new MarkerOptions().title("Katonah Station").position(new LatLng(41.2598, -73.6841)));
        map.addMarker(new MarkerOptions().title("Golden's Bridge Station").position(new LatLng(41.2945, -73.6776)));
        map.addMarker(new MarkerOptions().title("Purdy's Station").position(new LatLng(41.3256, -73.659)));
        map.addMarker(new MarkerOptions().title("Croton-Falls Station").position(new LatLng(41.3479, -73.6622)));
        map.addMarker(new MarkerOptions().title("Brewster Station").position(new LatLng(41.3947, -73.6198)));
        map.addMarker(new MarkerOptions().title("Southeast Station").position(new LatLng(41.4127, -73.623)));
        map.addMarker(new MarkerOptions().title("Patterson Station").position(new LatLng(41.51174, -73.60428)));
        map.addMarker(new MarkerOptions().title("Pawling Station").position(new LatLng(41.5646, -73.6004)));
        map.addMarker(new MarkerOptions().title("Appalachian Trail Station").position(new LatLng(41.5929, -73.588)));
        map.addMarker(new MarkerOptions().title("Harlem Valley-Wingdale Station").position(new LatLng(41.6374, -73.5717)));
        map.addMarker(new MarkerOptions().title("Dover Plains Station").position(new LatLng(41.7427, -73.5762)));
        map.addMarker(new MarkerOptions().title("Tenmile River Station").position(new LatLng(41.7795, -73.559)));
        map.addMarker(new MarkerOptions().title("Wassiac Station").position(new LatLng(41.8147, -73.5623)));

    }

    private void newHavenLinePoly(MapboxMap map) {
        List<LatLng> newHavenLine = new ArrayList<>();

        newHavenLine.add(new LatLng(40.861534, -73.890561));//Fordham
        newHavenLine.add(new LatLng(40.911942, -73.831678));//Mount Vernon East
        newHavenLine.add(new LatLng(40.912961, -73.810251));//Pelham
        newHavenLine.add(new LatLng(40.912317, -73.784936));//New Rochelle
        newHavenLine.add(new LatLng(40.934444, -73.759782));//Larchmont
        newHavenLine.add(new LatLng(40.955109, -73.736115));//Marmaroneck
        newHavenLine.add(new LatLng(40.97, -73.711));//Harrison
        newHavenLine.add(new LatLng(40.987803, -73.679123));//Rye
        newHavenLine.add(new LatLng(41.00178, -73.664703));//Port Chester
        newHavenLine.add(new LatLng(41.022326, -73.62462));//Greenwich
        newHavenLine.add(new LatLng(41.031111, -73.598333));//Cos Cob
        newHavenLine.add(new LatLng(41.032735, -73.594215));//Riverside
        newHavenLine.add(new LatLng(41.033333, -73.567778));//Old Greenwich
        newHavenLine.add(new LatLng(41.046937, -73.541493));//Stamford Terminal
        newHavenLine.add(new LatLng(41.070099, -73.49787));//Noroton Heights
        newHavenLine.add(new LatLng(41.078187, -73.472958));//Darien
        newHavenLine.add(new LatLng(41.07851, -73.445535));//Rowayton
        newHavenLine.add(new LatLng(41.0957, -73.42185));//South Norwalk
        newHavenLine.add(new LatLng(41.104, -73.4045));//East Norwalk
        newHavenLine.add(new LatLng(41.119986, -73.37142));//Westport
        newHavenLine.add(new LatLng(41.123316, -73.315415));//Green's Farm
        newHavenLine.add(new LatLng(41.136389, -73.286111));//Southport
        newHavenLine.add(new LatLng(41.142778, -73.258056));//Fairfield
        newHavenLine.add(new LatLng(41.1611, -73.2343));//Fairfield Metro
        newHavenLine.add(new LatLng(41.1778, -73.1871));//Bridgeport
        newHavenLine.add(new LatLng(41.195303, -73.131523));//Stratford
        newHavenLine.add(new LatLng(41.22235, -73.059619));//Milford
        newHavenLine.add(new LatLng(41.271142, -72.963199));//West Haven
        newHavenLine.add(new LatLng(41.2975, -72.926667));//Union Station- New Haven
        newHavenLine.add(new LatLng(41.305763, -72.921753));//New Haven State Street

        map.addPolyline(new PolylineOptions().addAll(newHavenLine).color(Color.parseColor("#AB2A07")));
    }

    private void newHavenMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Fordham Station").position(new LatLng(40.861534, -73.890561)));
        map.addMarker(new MarkerOptions().title("Mount Vernon East Station").position(new LatLng(40.911942, -73.831678)));
        map.addMarker(new MarkerOptions().title("Pelham Station").position(new LatLng(40.912961, -73.810251)));
        map.addMarker(new MarkerOptions().title("New Rochelle Station").position(new LatLng(40.912317, -73.784936)));
        map.addMarker(new MarkerOptions().title("Larchmont Station").position(new LatLng(40.934444, -73.759782)));
        map.addMarker(new MarkerOptions().title("Marmaroneck Station").position(new LatLng(40.955109, -73.736115)));
        map.addMarker(new MarkerOptions().title("Harrison Station").position(new LatLng(40.97, -73.711)));
        map.addMarker(new MarkerOptions().title("Rye Station").position(new LatLng(40.987803, -73.679123)));
        map.addMarker(new MarkerOptions().title("Port Chester Station").position(new LatLng(41.00178, -73.664703)));
        map.addMarker(new MarkerOptions().title("Greenwich Station").position(new LatLng(41.022326, -73.62462)));
        map.addMarker(new MarkerOptions().title("Cos Cob Station").position(new LatLng(41.031111, -73.598333)));
        map.addMarker(new MarkerOptions().title("Riverside Station").position(new LatLng(41.032735, -73.594215)));
        map.addMarker(new MarkerOptions().title("Old Greenwich Station").position(new LatLng(41.033333, -73.567778)));
        map.addMarker(new MarkerOptions().title("Stamford Terminal").position(new LatLng(41.046937, -73.541493)));
        map.addMarker(new MarkerOptions().title("Noroton Heights Station").position(new LatLng(41.070099, -73.49787)));
        map.addMarker(new MarkerOptions().title("Darien Station").position(new LatLng(41.078187, -73.472958)));
        map.addMarker(new MarkerOptions().title("Rowayton Station").position(new LatLng(41.07851, -73.445535)));
        map.addMarker(new MarkerOptions().title("South Norwalk Station").position(new LatLng(41.0957, -73.42185)));
        map.addMarker(new MarkerOptions().title("East Norwalk Station").position(new LatLng(41.104, -73.4045)));
        map.addMarker(new MarkerOptions().title("Westport Station").position(new LatLng(41.119986, -73.37142)));
        map.addMarker(new MarkerOptions().title("Green's Farm Station").position(new LatLng(41.123316, -73.315415)));
        map.addMarker(new MarkerOptions().title("Southport Station").position(new LatLng(41.136389, -73.286111)));
        map.addMarker(new MarkerOptions().title("Fairfieled Station").position(new LatLng(41.142778, -73.258056)));
        map.addMarker(new MarkerOptions().title("Fairfield Metro Station").position(new LatLng(41.1611, -73.2343)));
        map.addMarker(new MarkerOptions().title("Bridgeport Station").position(new LatLng(41.1778, -73.1871)));
        map.addMarker(new MarkerOptions().title("Stratford Station").position(new LatLng(41.195303, -73.131523)));
        map.addMarker(new MarkerOptions().title("Milford Station").position(new LatLng(41.22235, -73.059619)));
        map.addMarker(new MarkerOptions().title("West Haven Station").position(new LatLng(41.271142, -72.963199)));
        map.addMarker(new MarkerOptions().title("Union Station- New Haven").position(new LatLng(41.2975, -72.926667)));
        map.addMarker(new MarkerOptions().title("New Haven State Street Station").position(new LatLng(41.305763, -72.921753)));

    }

    private void newCanaanPoly(MapboxMap map) {
        List<LatLng> newCananLine = new ArrayList<>();

        newCananLine.add(new LatLng(41.046937, -73.541493));//Stamford Terminal
        newCananLine.add(new LatLng(41.0705, -73.5199));//Glenbrook
        newCananLine.add(new LatLng(41.0888, -73.5178));//Springdale
        newCananLine.add(new LatLng(41.116, -73.4981));//Talmadge Hill
        newCananLine.add(new LatLng(41.1463, -73.4956));//New Canaan
        map.addPolyline(new PolylineOptions().addAll(newCananLine).color(Color.parseColor("#FFC300")));

    }

    private void newCananMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Stamford Terminal").position(new LatLng(41.046937, -73.541493)));
        map.addMarker(new MarkerOptions().title("Glenbrook Station").position(new LatLng(41.0705, -73.5199)));
        map.addMarker(new MarkerOptions().title("Srpingdale Station").position(new LatLng(41.0888, -73.5178)));
        map.addMarker(new MarkerOptions().title("Talmadge Hill Station").position(new LatLng(41.116, -73.4981)));
        map.addMarker(new MarkerOptions().title("New Canaan Station").position(new LatLng(41.1463, -73.4956)));


    }

    private void danburyPoly(MapboxMap map) {
        List<LatLng> danburyLines = new ArrayList<>();

        danburyLines.add(new LatLng(41.0957, -73.42185));//South Norwalk
        danburyLines.add(new LatLng(41.1466, -73.4278));//Merrit 7
        danburyLines.add(new LatLng(41.1959, -73.4321));//Wilton
        danburyLines.add(new LatLng(41.216667, -73.426667));//Canonndale
        danburyLines.add(new LatLng(41.2667, -73.4409));//Branchville
        danburyLines.add(new LatLng(41.3256, -73.4335));//Redding
        danburyLines.add(new LatLng(41.376, -73.418));//Bethel
        danburyLines.add(new LatLng(41.3963, -73.4493));//Danbury
        map.addPolyline(new PolylineOptions().addAll(danburyLines).color(Color.parseColor("#D3720B")));
    }

    private void danburyMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Merrit 7 Station").position(new LatLng(41.0957, -73.42185)));
        map.addMarker(new MarkerOptions().title("Wilton Station").position(new LatLng(41.1959, -73.4321)));
        map.addMarker(new MarkerOptions().title("Cannondale Station").position(new LatLng(41.216667, -73.426667)));
        map.addMarker(new MarkerOptions().title("Branchville Station").position(new LatLng(41.2667, -73.4409)));
        map.addMarker(new MarkerOptions().title("Redding Station").position(new LatLng(41.3256, -73.4335)));
        map.addMarker(new MarkerOptions().title("Bethel Station").position(new LatLng(41.376, -73.418)));
        map.addMarker(new MarkerOptions().title("Danbury Station").position(new LatLng(41.3963, -73.4493)));

    }

    private void waterburyPoly(MapboxMap map) {
        List<LatLng> waterLines = new ArrayList<>();

        waterLines.add(new LatLng(41.1778, -73.1871));//Bridgeport
        waterLines.add(new LatLng(41.195303, -73.131523));//Stratford
        waterLines.add(new LatLng(41.320284, -73.083565));//Derby-Shelton
        waterLines.add(new LatLng(41.3442, -73.0799));//Asonia
        waterLines.add(new LatLng(41.3953, -73.0725));//Seymour
        waterLines.add(new LatLng(41.4407, -73.0631));//Beacon Falls
        waterLines.add(new LatLng(41.492778, -73.052222));//Naugatuck
        waterLines.add(new LatLng(41.5544, -73.047));//Waterbury
        map.addPolyline(new PolylineOptions().addAll(waterLines).color(Color.parseColor("#5FECC9")));

    }

    private void waterburyMarkers(MapboxMap map) {
        map.addMarker(new MarkerOptions().title("Derby-Shelton Station").position(new LatLng(41.320284, -73.083565)));
        map.addMarker(new MarkerOptions().title("Asonia Station").position(new LatLng(41.3442, -73.0799)));
        map.addMarker(new MarkerOptions().title("Seymour Station").position(new LatLng(41.3953, -73.0725)));
        map.addMarker(new MarkerOptions().title("Beacon Falls Station").position(new LatLng(41.4407, -73.0631)));
        map.addMarker(new MarkerOptions().title("Naugatuck Station").position(new LatLng(41.492778, -73.052222)));
        map.addMarker(new MarkerOptions().title("Waterbury Station").position(new LatLng(41.5544, -73.047)));

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
        Toast.makeText(LIRRMap.this, "Location Changed", Toast.LENGTH_SHORT).show();
        displayLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed" + connectionResult.getErrorMessage());
    }

    private class MarkersAsyncTask extends AsyncTask<Void,Void,List<LatLng>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<LatLng> doInBackground(Void... params) {

            // Store the route LatLng points in a list so we can query them.
            List<LatLng> points = new ArrayList<>();
            mBabylon= new ArrayList<>();
            mMontaukLine= new ArrayList<>();
            mPennLine= new ArrayList<>();
            mAtlanticTerminal= new ArrayList<>();
            mPortWashington= new ArrayList<>();
            mLongBeach= new ArrayList<>();
            mHempstead= new ArrayList<>();
            mWestHempstead= new ArrayList<>();
            mPortJefferson= new ArrayList<>();
            mOysterBay= new ArrayList<>();
            mFarRockaway= new ArrayList<>();
            mRonkonkoma= new ArrayList<>();

            //Bablyon Line
            mBabylon.add(new LatLng(40.699511, -73.808727));//Jamaica
            mBabylon.add(new LatLng(40.676053, -73.905925));//East New York
            mBabylon.add(new LatLng(40.67845, -73.9494));// Nostrand Avenue
            mBabylon.add(new LatLng(40.684226, -73.977234));//Atlantic Terminal
            mBabylon.add(new LatLng(40.699511, -73.808727)); //Jamaica
            mBabylon.add(new LatLng(40.691052, -73.765426)); // St.Albans
            mBabylon.add(new LatLng(40.65603, -73.6758)); //Lynbrook
            mBabylon.add(new LatLng(40.6583, -73.6466)); //Rockville Centre
            mBabylon.add(new LatLng(40.656746, -73.607444)); //Baldwin
            mBabylon.add(new LatLng(40.657425, -73.582601)); //Freeport
            mBabylon.add(new LatLng(40.663769, -73.550709)); //Merrick
            mBabylon.add(new LatLng(40.668766, -73.528833)); //Bellmore
            mBabylon.add(new LatLng(40.672937, -73.509098)); //Wantagh
            mBabylon.add(new LatLng(40.67573, -73.486454)); //Seaford
            mBabylon.add(new LatLng(40.676901, -73.469052)); //Massapequa
            mBabylon.add(new LatLng(40.677851, -73.45474)); //Massapequa Park
            mBabylon.add(new LatLng(40.680263, -73.420472)); //Amityville
            mBabylon.add(new LatLng(40.681, -73.399)); //Copiague
            mBabylon.add(new LatLng(40.688243, -73.369242)); //Lindenhurst
            mBabylon.add(new LatLng(40.700614, -73.32421)); //Babylon

            //Ronkonkoma Line
            mRonkonkoma.add(new LatLng(40.699511, -73.808727)); //Jamaica
            mRonkonkoma.add(new LatLng(40.7102, -73.7666)); //Hollis
            mRonkonkoma.add(new LatLng(40.717469, -73.73638)); //Queens Village
            mRonkonkoma.add(new LatLng(40.724622, -73.706398)); //Floral Park
            mRonkonkoma.add(new LatLng(40.730932, -73.680569)); //New Hyde Park
            mRonkonkoma.add(new LatLng(40.735164, -73.662523)); //Merillion Avenue
            mRonkonkoma.add(new LatLng(40.740291, -73.641025)); //Mineola
            mRonkonkoma.add(new LatLng(40.749195, -73.603705)); //Carle Place
            mRonkonkoma.add(new LatLng(40.753404, -73.586273)); //Westbury
            mRonkonkoma.add(new LatLng(40.767101, -73.528686)); //Hicksville
            mRonkonkoma.add(new LatLng(40.742994, -73.483359)); //Bethpage
            mRonkonkoma.add(new LatLng(40.735665, -73.441713)); //Farmingdale
            mRonkonkoma.add(new LatLng(40.745339, -73.399572)); //Pinelawn
            mRonkonkoma.add(new LatLng(40.754889, -73.35781)); //Wyandanch
            mRonkonkoma.add(new LatLng(40.76948, -73.293577)); //Deer Park
            mRonkonkoma.add(new LatLng(40.780826, -73.243607)); //Brentwood
            mRonkonkoma.add(new LatLng(40.79188, -73.19467)); //Central Islip
            mRonkonkoma.add(new LatLng(40.808088, -73.1059)); //Ronkonkoma
            mRonkonkoma.add(new LatLng(40.817356, -72.999159));//Medford
            mRonkonkoma.add(new LatLng(40.825656, -72.915841));//Yaphank
            mRonkonkoma.add(new LatLng(40.919763, -72.6669));//Riverhead
            mRonkonkoma.add(new LatLng(40.991761, -72.536009));//Mattituck
            mRonkonkoma.add(new LatLng(41.066295, -72.427859));//Southold
            mRonkonkoma.add(new LatLng(41.099722, -72.363611));//Greenport

            //Montauk Line
            mMontaukLine.add(new LatLng(41.046793, -71.954452));// Montauk
            mMontaukLine.add(new LatLng(40.98, -72.1325));//Amagansett
            mMontaukLine.add(new LatLng(40.964936, -72.193461));//East Hampton
            mMontaukLine.add(new LatLng(40.939163, -72.309646));//BridgeHampton
            mMontaukLine.add(new LatLng(40.894779, -72.38975));//Southampton
            mMontaukLine.add(new LatLng(40.876464, -72.524566));//Hampton Bays
            mMontaukLine.add(new LatLng(40.830191, -72.650973));//Westhampton
            mMontaukLine.add(new LatLng(40.821224, -72.704853));//Speonk
            mMontaukLine.add(new LatLng(40.7989, -72.86454));//Mastic-Shirley
            mMontaukLine.add(new LatLng(40.773717, -72.943769));//Bellport
            mMontaukLine.add(new LatLng(40.761841, -73.015735));//Patchogue
            mMontaukLine.add(new LatLng(40.740388, -73.086497));//Sayville
            mMontaukLine.add(new LatLng(40.7434, -73.1324));//Oakdale
            mMontaukLine.add(new LatLng(40.740476, -73.17));//Great River
            mMontaukLine.add(new LatLng(40.736, -73.209));//Islip
            mMontaukLine.add(new LatLng(40.72505, -73.253057));//Bayshore
            mMontaukLine.add(new LatLng(40.700614, -73.32421)); //Babylon

            //Port Washington Line
            mPortWashington.add(new LatLng(40.829349, -73.68733));//Port Washington
            mPortWashington.add(new LatLng(40.810687, -73.695216));//Plandome Station
            mPortWashington.add(new LatLng(40.79669, -73.6999996));//Manhasset
            mPortWashington.add(new LatLng(40.787235, -73.725986));//Great Neck
            mPortWashington.add(new LatLng(40.775, -73.740744));//Little Neck
            mPortWashington.add(new LatLng(40.768, -73.7496));//Douglaston
            mPortWashington.add(new LatLng(40.763105, -73.771804));//Bayside
            mPortWashington.add(new LatLng(40.76139, -73.7905));//Auburndale
            mPortWashington.add(new LatLng(40.761626, -73.801383));//Broadway Station
            mPortWashington.add(new LatLng(40.762703, -73.81446));//Murray Hill
            mPortWashington.add(new LatLng(40.757989, -73.831086));//Flusing-Main Street
            mPortWashington.add(new LatLng(40.752516, -73.843725));// Mets Stadium- Willets Point
            mPortWashington.add(new LatLng(40.746072, -73.903201));// Woodside
            mPortWashington.add(new LatLng(40.750638, -73.993899));//Penn Station

            //Long Beach Line
            mLongBeach.add(new LatLng(40.699511, -73.808727)); //Jamaica
            mLongBeach.add(new LatLng(40.691052, -73.765426)); // St.Albans
            mLongBeach.add(new LatLng(40.65603, -73.6758)); //Lynbrook
            mLongBeach.add(new LatLng(40.648272, -73.663915)); //Centre Avenue
            mLongBeach.add(new LatLng(40.640938, -73.657067)); //East Rockaway
            mLongBeach.add(new LatLng(40.634986, -73.654613)); //Oceanside
            mLongBeach.add(new LatLng(40.600433, -73.655388)); //Island Park
            mLongBeach.add(new LatLng(40.589368, -73.664854)); //Long Beach

            //Penn Station Line
            mPennLine.add(new LatLng(40.699511, -73.808727));//Jamaica
            mPennLine.add(new LatLng(40.7096, -73.83066));// Kew Gardens
            mPennLine.add(new LatLng(40.719483, -73.844883));//Forrest Hills
            mPennLine.add(new LatLng(40.746072, -73.903201));//Woodside
            mPennLine.add(new LatLng(40.750638, -73.993899));// Penn Station

            //Atlantic Terminal Line
            mAtlanticTerminal.add(new LatLng(40.699511, -73.808727));//Jamaica
            mAtlanticTerminal.add(new LatLng(40.676053, -73.905925));//East New York
            mAtlanticTerminal.add(new LatLng(40.67845, -73.9494));// Nostrand Avenue
            mAtlanticTerminal.add(new LatLng(40.684226, -73.977234));//Atlantic Terminal

            //West Hempstead
            mWestHempstead.add(new LatLng(40.701944, -73.641667));// West Hempstead
            mWestHempstead.add(new LatLng(40.694722, -73.646111));// Hempstead Gardens
            mWestHempstead.add(new LatLng(40.685556, -73.652222));// Lakeview
            mWestHempstead.add(new LatLng(40.675556, -73.668611));//Malverne
            mWestHempstead.add(new LatLng(40.668278, -73.681417));// Westwood
            mWestHempstead.add(new LatLng(40.661483, -73.704679));//Valley Stream

            //Hempstead Line
            mHempstead.add(new LatLng(40.713102, -73.625307));//Hempstead
            mHempstead.add(new LatLng(40.721234, -73.629405));//Country Life Press
            mHempstead.add(new LatLng(40.723136, -73.64007));//Garden City
            mHempstead.add(new LatLng(40.722933, -73.662751));//Nassau Blvd
            mHempstead.add(new LatLng(40.723006, -73.68114));//Stewart Manor
            mHempstead.add(new LatLng(40.724622, -73.706398));//Floral Park

            //Port Jefferson Line
            mPortJefferson.add(new LatLng(40.934719, -73.053692));// Port Jefferson
            mPortJefferson.add(new LatLng(40.920275, -73.128514));// Stony Brook
            mPortJefferson.add(new LatLng(40.883272, -73.158153));// Saint James
            mPortJefferson.add(new LatLng(40.856264, -73.199272));// Smithtown
            mPortJefferson.add(new LatLng(40.883719, -73.255819));//Kings Park
            mPortJefferson.add(new LatLng(40.880714, -73.3285));//Northport
            mPortJefferson.add(new LatLng(40.868658, -73.362972));// Greenlawn
            mPortJefferson.add(new LatLng(40.852692, -73.410639));//Huntington
            mPortJefferson.add(new LatLng(40.835056, -73.451611));//Cold Spring Harbor
            mPortJefferson.add(new LatLng(40.824892, -73.500492));//Syosset
            mPortJefferson.add(new LatLng(40.767101, -73.528686));//Hicksville

            //Oyster Bay Line
            mOysterBay.add(new LatLng(40.874992, -73.531603));//Oyster Bay
            mOysterBay.add(new LatLng(40.874251, -73.598678));//Locust Valley
            mOysterBay.add(new LatLng(40.865189, -73.616976));// Glen Cove
            mOysterBay.add(new LatLng(40.857862, -73.621461));//Glen Street
            mOysterBay.add(new LatLng(40.852564, -73.625408));//Sea Cliff
            mOysterBay.add(new LatLng(40.832284, -73.626128));// Glen Head
            mOysterBay.add(new LatLng(40.815547, -73.626916));//Greenvale
            mOysterBay.add(new LatLng(40.79072, -73.643267));//Roslyn
            mOysterBay.add(new LatLng(40.771872, -73.641679));// Albertson
            mOysterBay.add(new LatLng(40.75614, -73.639426));// East Williston
            mOysterBay.add(new LatLng(40.740291, -73.641025)); //Mineola
            mOysterBay.add(new LatLng(40.735164, -73.662523)); //Merillion Avenue
            mOysterBay.add(new LatLng(40.730932, -73.680569)); //New Hyde Park
            mOysterBay.add(new LatLng(40.724622, -73.706398)); //Floral Park
            mOysterBay.add(new LatLng(40.717469, -73.73638)); //Queens Village
            mOysterBay.add(new LatLng(40.7102, -73.7666)); //Hollis
            mOysterBay.add(new LatLng(40.699511, -73.808727)); //Jamaica

            //Far Rockaway
            mFarRockaway.add(new LatLng(40.608610, -73.750792));//Far Rockaway
            mFarRockaway.add(new LatLng(40.612291, -73.74431));//Inwood
            mFarRockaway.add(new LatLng(40.615638, -73.736050));//Lawrence
            mFarRockaway.add(new LatLng(40.622214, -73.727121));//Cedarhurst
            mFarRockaway.add(new LatLng(40.631298, -73.713740));//Woodmere
            mFarRockaway.add(new LatLng(40.636737, -73.705151));//Hewlett
            mFarRockaway.add(new LatLng(40.649927, -73.701694));//Gibson
            mFarRockaway.add(new LatLng(40.661483, -73.704679)); //Valley Stream




            return points;
        }

        @Override
        protected void onPostExecute(final List<LatLng> points) {
            super.onPostExecute(points);
            pb.setVisibility(View.INVISIBLE);
            if(points.size()>0){
                LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);

                // Draw a polyline showing the route the marker will be taking.
                mMap.addPolyline(new PolylineOptions()
                        .add(pointsArray)
                        .color(Color.parseColor("#F13C6E")));

            }
            mMap.addPolyline(new PolylineOptions().addAll(mBabylon).color(Color.parseColor("#3bb2d0")));
            mMap.addPolyline(new PolylineOptions().addAll(mMontaukLine).color(Color.parseColor("#FF4081")));
            mMap.addPolyline(new PolylineOptions().addAll(mPennLine).color(Color.parseColor("#009688")));
            mMap.addPolyline(new PolylineOptions().addAll(mAtlanticTerminal).color(Color.parseColor("#311B92")));
            mMap.addPolyline(new PolylineOptions().addAll(mAtlanticTerminal).color(Color.parseColor("#E65100")));
            mMap.addPolyline(new PolylineOptions().addAll(mLongBeach).color(Color.parseColor("#009688")));
            mMap.addPolyline(new PolylineOptions().addAll(mRonkonkoma).color(Color.parseColor("#9C27B0")));
            mMap.addPolyline(new PolylineOptions().addAll(mWestHempstead).color(Color.parseColor("#E040FB")));
            mMap.addPolyline(new PolylineOptions().addAll(mHempstead).color(Color.parseColor("#01579B")));
            mMap.addPolyline(new PolylineOptions().addAll(mPortJefferson).color(Color.parseColor("#FF5722")));
            mMap.addPolyline(new PolylineOptions().addAll(mOysterBay).color(Color.parseColor("#00695C")));
            mMap.addPolyline(new PolylineOptions().addAll(mFarRockaway).color(Color.parseColor("#64FFDA")));

        }
    }

}


