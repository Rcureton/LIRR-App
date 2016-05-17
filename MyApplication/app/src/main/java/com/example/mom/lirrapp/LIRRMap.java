package com.example.mom.lirrapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.widgets.UserLocationView;

import java.util.ArrayList;
import java.util.List;

public class LIRRMap extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private MapView mapView;
    private static final int PERMISSIONS_LOCATION = 0;
    private MapboxMap map1;
    private static final String TAG= LIRRMap.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST= 1000;
    private Location mLastLocationCoordinates;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL= 10000;
    private static int FASTEST_INTERVAL= 5000;
    private static int DISPLACEMENT= 10;
    double lon;
    double lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);



        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                myLocation();
                IconFactory iconFactory = IconFactory.getInstance(LIRRMap.this);
                Drawable iconDrawable = ContextCompat.getDrawable(LIRRMap.this, R.drawable.purple_marker);
                Icon icon = iconFactory.fromDrawable(iconDrawable);
                mapboxMap.addMarker(new MarkerOptions().title("Current Location").position(new LatLng(lat,lon)).icon(icon));

                drawPolyLinesBabylon(mapboxMap);
                ronkonkomaTrainPolyline(mapboxMap);
                babylonMarkers(mapboxMap);
                ronkonkomaMarkers(mapboxMap);
                longBeachPolyLine(mapboxMap);
                longbeachBranchMarkers(mapboxMap);
                atlanticBranchMarkers(mapboxMap);
                atlanticBranchPolyline(mapboxMap);
                oysterBayBranchMarkers(mapboxMap);
                oysterBayBranchPolyLine(mapboxMap);
                portJeffersonBranchMarkers(mapboxMap);
                portJeffersonBranchPolyLine(mapboxMap);
                farRockawayBranchMarkers(mapboxMap);
                farRockawayBranchPolyline(mapboxMap);
                westHempsteadBranchMarkers(mapboxMap);
                westHempsteadBranchPolyline(mapboxMap);
                hempsteadBranchMarkers(mapboxMap);
                hempsteadBranchPolyline(mapboxMap);


            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(LIRRMap.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10 );

            return;

        }else{
            configureButton();

        }





        if(checkPlayServices() ){
            buildGoogleApiClient();
            createLocationRequest();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mGoogleApiClient !=null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        checkPlayServices();
        if(mGoogleApiClient.isConnected() && mRequestLocationUpdates){
            startLocationUpdates();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocationUpdates();

    }

    private void displayLocation(){
        mLastLocationCoordinates= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocationCoordinates !=null){
            lat= mLastLocationCoordinates.getLatitude();
            lon= mLastLocationCoordinates.getLongitude();

            Toast.makeText(LIRRMap.this, " ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(LIRRMap.this, "Couldn't get the location", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePeriodLocation(){
        if(!mRequestLocationUpdates){
            mRequestLocationUpdates=true;
            startLocationUpdates();
        }else{
            mRequestLocationUpdates= false;

            stopLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private boolean checkPlayServices(){
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Toast.makeText(LIRRMap.this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
        }
    }

    private void configureButton() {
//        mLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
//                Toast.makeText(LocationPage.this, "Currently getting your location", Toast.LENGTH_LONG).show();
//                myDate.setLon(lon);
//                myDate.setLat(lat);
//                Intent intent = new Intent(LocationPage.this, VenueType.class);
//                intent.putExtra(MyDateItems.MY_ITEMS, myDate);
//                startActivity(intent);
//
//            }
//        });

    }


    private void myLocation(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        } else {
           return;
        }
        map1.setMyLocationEnabled(true);
        map1.getMyLocation();

    }

    private void westHempsteadBranchPolyline(MapboxMap map){
        List<LatLng> westHempsteadPolyline= new ArrayList<>();

        westHempsteadPolyline.add(new LatLng(40.701944, -73.641667));// West Hempstead
        westHempsteadPolyline.add(new LatLng(40.694722, -73.646111));// Hempstead Gardens
        westHempsteadPolyline.add(new LatLng(40.685556, -73.652222));// Lakeview
        westHempsteadPolyline.add(new LatLng(40.675556, -73.668611));//Malverne
        westHempsteadPolyline.add(new LatLng(40.668278, -73.681417));// Westwood
        westHempsteadPolyline.add(new LatLng(40.661483, -73.704679));//Valley Stream

        map.addPolyline(new PolylineOptions().addAll(westHempsteadPolyline).color(Color.parseColor("#E040FB")));
    }

    private void westHempsteadBranchMarkers(MapboxMap map){
        map.addMarker(new MarkerOptions().title("West Hempstead Train Station").position(new LatLng(40.701944, -73.641667)));
        map.addMarker(new MarkerOptions().title("Hempstead Gardens Train Station").position(new LatLng(40.694722, -73.646111)));
        map.addMarker(new MarkerOptions().title("Lakeview Train Station").position(new LatLng(40.685556, -73.652222)));
        map.addMarker(new MarkerOptions().title("Malverne Train Station").position(new LatLng(40.675556, -73.668611)));
        map.addMarker(new MarkerOptions().title("Westwood Train Station").position(new LatLng(40.668278, -73.681417)));

    }
    private void hempsteadBranchPolyline(MapboxMap map){
        List<LatLng> hempsteadBranchPoly= new ArrayList<>();

        hempsteadBranchPoly.add(new LatLng(40.713102, -73.625307));//Hempstead
        hempsteadBranchPoly.add(new LatLng(40.721234, -73.629405));//Country Life Press
        hempsteadBranchPoly.add(new LatLng(40.723136, -73.64007));//Garden City
        hempsteadBranchPoly.add(new LatLng(40.722933, -73.662751));//Nassau Blvd
        hempsteadBranchPoly.add(new LatLng(40.723006, -73.68114));//Stewart Manor
        hempsteadBranchPoly.add(new LatLng(40.724622, -73.706398));//Floral Park

        map.addPolyline(new PolylineOptions().addAll(hempsteadBranchPoly).color(Color.parseColor("#01579B")));
    }

    private void hempsteadBranchMarkers(MapboxMap map){
        map.addMarker(new MarkerOptions().title("Hempstead Train Station").position(new LatLng(40.713102, -73.625307)));
        map.addMarker(new MarkerOptions().title("Country Life Press Train Station").position(new LatLng(40.721234, -73.629405)));
        map.addMarker(new MarkerOptions().title("Garden City Train Station").position(new LatLng(40.723136, -73.64007)));
        map.addMarker(new MarkerOptions().title("Nassau Boulevard Train Station").position(new LatLng(40.722933, -73.662751)));
        map.addMarker(new MarkerOptions().title("Stewart Manor Train Station").position(new LatLng(40.723006, -73.68114)));

    }


    private void ronkonkomaTrainPolyline(MapboxMap map){
        List<LatLng> ronkonkomaLine= new ArrayList<>();

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

        map.addPolyline(new PolylineOptions().addAll(ronkonkomaLine).color(Color.parseColor("#9C27B0")));
    }

    private void ronkonkomaMarkers(MapboxMap map){
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
    }

    private void drawPolyLinesBabylon(MapboxMap map){
        List<LatLng> babylonTrainLine= new ArrayList<>();

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

    private void babylonMarkers (MapboxMap map){
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
    private void longBeachPolyLine(MapboxMap map){
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
    private void longbeachBranchMarkers(MapboxMap map){

        map.addMarker(new MarkerOptions().title("Lynbrook Train Station").position(new LatLng(40.65603, -73.6758)));
        map.addMarker(new MarkerOptions().title("Centre Avenue Train Station").position(new LatLng(40.648272, -73.663915)));
        map.addMarker(new MarkerOptions().title("East Rockaway Train Station").position(new LatLng(40.640938, -73.657067)));
        map.addMarker(new MarkerOptions().title("Oceanside Train Station").position(new LatLng(40.634986, -73.654613)));
        map.addMarker(new MarkerOptions().title("Island Park Train Station").position(new LatLng(40.600433, -73.655388)));
        map.addMarker(new MarkerOptions().title("Long Beach Train Station").position(new LatLng(40.589368, -73.664854)));
    }

    private void atlanticBranchPolyline(MapboxMap map){
        List<LatLng> atlanticPolylines= new ArrayList<>();

        atlanticPolylines.add(new LatLng(40.699511, -73.808727)); //Jamaica
        atlanticPolylines.add(new LatLng(40.675022, -73.764897));//Locust Manor
        atlanticPolylines.add(new LatLng(40.66853, -73.7518));//Laurelton
        atlanticPolylines.add(new LatLng(40.6659, -73.7356));//Rosedale
        atlanticPolylines.add(new LatLng(40.661483, -73.704679));//Valley Stream

        map.addPolyline(new PolylineOptions().addAll(atlanticPolylines).color(Color.parseColor("#FFA000")));
    }

    private void atlanticBranchMarkers(MapboxMap map){

        map.addMarker(new MarkerOptions().title("Jamaica Train Station").position(new LatLng(40.699511, -73.808727)));
        map.addMarker(new MarkerOptions().title("Locust Manor Train Station").position(new LatLng(40.675022, -73.764897)));
        map.addMarker(new MarkerOptions().title("Laurelton Train Station").position(new LatLng(40.66853, -73.7518)));
        map.addMarker(new MarkerOptions().title("Rosedale Train Station").position(new LatLng(40.6659, -73.7356)));
        map.addMarker(new MarkerOptions().title("Valley Stream Train Station").position(new LatLng(40.661483, -73.704679)));
    }
    private void oysterBayBranchPolyLine(MapboxMap map){
        List<LatLng> oysterBayLine= new ArrayList<>();

        oysterBayLine.add(new LatLng(40.874992,-73.531603));//Oyster Bay
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
    private void oysterBayBranchMarkers(MapboxMap map){
        map.addMarker(new MarkerOptions().title("Oyster Bay Train Station").position(new LatLng(40.874992,-73.531603)));
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

    private void portJeffersonBranchPolyLine(MapboxMap map){
        List<LatLng> portJeffLine= new ArrayList<>();

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
    private void portJeffersonBranchMarkers(MapboxMap map){
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
    private void farRockawayBranchPolyline(MapboxMap map){
        List<LatLng> farRockawayLine= new ArrayList<>();

        farRockawayLine.add(new LatLng(40.608610, -73.750792));//Far Rockaway
        farRockawayLine.add(new LatLng(40.612291, -73.74431));//Inwood
        farRockawayLine.add(new LatLng(40.615638, -73.736050));//Lawrence
        farRockawayLine.add(new LatLng(40.622214,-73.727121));//Cedarhurst
        farRockawayLine.add(new LatLng(40.631298, -73.713740));//Woodmere
        farRockawayLine.add(new LatLng(40.636737, -73.705151));//Hewlett
        farRockawayLine.add(new LatLng(40.649927, -73.701694));//Gibson
        farRockawayLine.add(new LatLng(40.661483, -73.704679)); //Valley Stream

        map.addPolyline(new PolylineOptions().addAll(farRockawayLine).color(Color.parseColor("#64FFDA")));
    }

    private void farRockawayBranchMarkers(MapboxMap map){
        map.addMarker(new MarkerOptions().title("Far Rockaway Train Station").position(new LatLng(40.608610, -73.750792)));
        map.addMarker(new MarkerOptions().title("Inwood Train Station").position(new LatLng(40.612291, -73.74431)));
        map.addMarker(new MarkerOptions().title("Lawrence Train Station").position(new LatLng(40.615638, -73.736050)));
        map.addMarker(new MarkerOptions().title("Cedarhurst Train Station").position(new LatLng(40.622214,-73.727121)));
        map.addMarker(new MarkerOptions().title("Woodmere Train Station").position(new LatLng(40.631298, -73.713740)));
        map.addMarker(new MarkerOptions().title("Hewlett Train Station").position(new LatLng(40.636737, -73.705151)));
        map.addMarker(new MarkerOptions().title("Gibson Train Station").position(new LatLng(40.649927, -73.701694)));

    }

            // Add the mapView lifecycle to the activity's lifecycle methods


            @Override
            public void onLowMemory() {
                super.onLowMemory();
                mapView.onLowMemory();
            }

            @Override
            protected void onDestroy() {
                super.onDestroy();
                mapView.onDestroy();
            }

            @Override
            protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                mapView.onSaveInstanceState(outState);
            }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSIONS_LOCATION: {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    map1.setMyLocationEnabled(true);
//                }
//            }
//        }
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if(mRequestLocationUpdates){
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocationCoordinates= location;
        Toast.makeText(LIRRMap.this, "Location Changed", Toast.LENGTH_SHORT).show();
        displayLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG,"Connection failed" + connectionResult.getErrorMessage());
    }
}


