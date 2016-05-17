package com.example.mom.lirrapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class Basic extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                drawPolyLinesBabylon(mapboxMap);
                ronkonkomaTrainPolyline(mapboxMap);
                babylonMarkers(mapboxMap);
                ronkonkomaMarkers(mapboxMap);
                longBeachPolyLine(mapboxMap);
                longbeachBranchMarkers(mapboxMap);
                atlanticBranchMarkers(mapboxMap);
                atlanticBranchPolyline(mapboxMap);

//                // Customize map with markers, polylines, etc.
//                mapboxMap.addMarker(new MarkerOptions()
//                .position(new LatLng(40.680263, -73.420472)).title("Amityville Train Station"));


            }
        });
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

            // Add the mapView lifecycle to the activity's lifecycle methods
            @Override
            public void onResume() {
                super.onResume();
                mapView.onResume();
            }

            @Override
            public void onPause() {
                super.onPause();
                mapView.onPause();
            }

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
}


