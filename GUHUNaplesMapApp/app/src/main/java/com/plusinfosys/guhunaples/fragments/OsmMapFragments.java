package com.plusinfosys.guhunaples.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plusinfosys.guhunaples.MainActivity;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;

import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


import com.plusinfosys.guhunaples.R;
import com.plusinfosys.guhunaples.maputils.BoundedMapView;
import com.plusinfosys.guhunaples.maputils.CustomInfoWindow;
import com.plusinfosys.guhunaples.maputils.MBTileProvider;
import com.plusinfosys.guhunaples.maputils.MarkerOnClick;
import com.plusinfosys.guhunaples.model.Place;
import com.plusinfosys.guhunaples.widgets.CustomMapView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public class OsmMapFragments extends Fragment implements IRegisterReceiver, CompoundButton.OnCheckedChangeListener, MarkerOnClick, View.OnTouchListener {
    private MapView mapView;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private ImageView toolbar;
    MyLocationNewOverlay myLocationOverlay = null;
    private CheckBox food_tab, shop_tab, views_tab, fun_tab, people_tab;
    FolderOverlay foodOverlay, shopOverlay, viewsOverlay, funOverlay, peopleOverlay;
    private ArrayList<Marker> foodMarkers, shopMarkers, viewsMarkers, FunMarkers, peopleMarkers;
    double north = 40.922;
    double east = 14.17;
    double south = 40.789;
    double west = 14.319;
    BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);
    DefaultResourceProxyImpl resProxy;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    public static OsmMapFragments newInstance() {
        OsmMapFragments fragment = new OsmMapFragments();

        return fragment;
    }

    public OsmMapFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        toolbar = (ImageView) rootView.findViewById(R.id.activity_my_toolbar);


        food_tab = (CheckBox) rootView.findViewById(R.id.food_tab);
        shop_tab = (CheckBox) rootView.findViewById(R.id.shop_tab);
        views_tab = (CheckBox) rootView.findViewById(R.id.views_tab);
        fun_tab = (CheckBox) rootView.findViewById(R.id.fun_tab);
        people_tab = (CheckBox) rootView.findViewById(R.id.people_tab);

        food_tab.setOnCheckedChangeListener(this);

        shop_tab.setOnCheckedChangeListener(this);
        views_tab.setOnCheckedChangeListener(this);
        fun_tab.setOnCheckedChangeListener(this);
        people_tab.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        resProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());

        toolbar.setImageResource(R.drawable.map_icon_tab);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getActivity().getPackageName();
        File file = new File(path, "Naples.mbtiles");

        MBTileProvider provider = new MBTileProvider(this, file);
        // mapView = new CustomMapView(getActivity(),256, resProxy, provider);
       /* mapView = new BoundedMapView(getActivity(),
                provider.getTileSource().getTileSizePixels(),
                resProxy,
                provider);*/
        mapView = new MapView(getActivity(),  provider.getTileSource().getTileSizePixels(),

                resProxy,
                provider);
        mapView.setMultiTouchControls(true);


        // Zoom in and go to Amsterdam
        mapView.setScrollableAreaLimit(bBox);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMinZoomLevel(13);
        mapView.setMaxZoomLevel(16);
        mapView.setScrollableAreaLimit(bBox);
        IMapController controller = mapView.getController();
        controller.setZoom(15);
        controller.animateTo(new GeoPoint(40.83344, 14.24080));
        ((FrameLayout) getView().findViewById(R.id.mapviewLayout)).addView(mapView);

        foodOverlay = new FolderOverlay(getActivity());
        shopOverlay = new FolderOverlay(getActivity());
        viewsOverlay = new FolderOverlay(getActivity());
        funOverlay = new FolderOverlay(getActivity());
        peopleOverlay = new FolderOverlay(getActivity());
        setFoodOverlay();
        setFunOverlay();
        setPeopleOverlay();
        setViewsOverlay();
        setshopOverlay();
        myLocationOverlay = new MyLocationNewOverlay(getActivity(), mapView);
        mapView.getOverlays().add(myLocationOverlay);
       /* myLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {

               // Toast.makeText(getActivity(),myLocationOverlay.getMyLocation().toString(),Toast.LENGTH_LONG).show();
               // mapView.getController().setCenter(new GeoPoint(40.85572, 14.23908));
            }
        });*/

        mapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().setCenter(new GeoPoint(40.85572, 14.23908));
            }
        }, 6000);
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    if (peopleMarkers != null) {
                        for (int i = 0; i < peopleMarkers.size(); i++) {
                            if (peopleMarkers.get(i).isInfoWindowShown())
                                peopleMarkers.get(i).hideInfoWindow();
                        }
                    }
                    if (FunMarkers != null) {
                        for (int i = 0; i < peopleMarkers.size(); i++) {
                            if (peopleMarkers.get(i).isInfoWindowShown())
                                peopleMarkers.get(i).hideInfoWindow();
                        }
                    }
                    if (viewsMarkers != null) {
                        for (int i = 0; i < viewsMarkers.size(); i++) {
                            if (viewsMarkers.get(i).isInfoWindowShown())
                                viewsMarkers.get(i).hideInfoWindow();
                        }
                    }
                    if (foodMarkers != null) {
                        for (int i = 0; i < foodMarkers.size(); i++) {
                            if (foodMarkers.get(i).isInfoWindowShown())
                                foodMarkers.get(i).hideInfoWindow();
                        }
                    }
                    if (shopMarkers != null) {
                        for (int i = 0; i < shopMarkers.size(); i++) {
                            if (shopMarkers.get(i).isInfoWindowShown())
                                shopMarkers.get(i).hideInfoWindow();
                        }
                    }

                }
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.food_tab:
                if (isChecked)
                    mapView.getOverlays().add(foodOverlay);
                else
                    mapView.getOverlays().remove(foodOverlay);
                mapView.invalidate();
                break;
            case R.id.shop_tab:
                if (isChecked)
                    mapView.getOverlays().add(shopOverlay);
                else
                    mapView.getOverlays().remove(shopOverlay);
                mapView.invalidate();
                break;
            case R.id.views_tab:
                if (isChecked)
                    mapView.getOverlays().add(viewsOverlay);
                else
                    mapView.getOverlays().remove(viewsOverlay);
                mapView.invalidate();
                break;
            case R.id.fun_tab:
                if (isChecked)
                    mapView.getOverlays().add(funOverlay);
                else
                    mapView.getOverlays().remove(funOverlay);
                mapView.invalidate();
                break;
            case R.id.people_tab:
                if (isChecked)
                    mapView.getOverlays().add(peopleOverlay);
                else
                    mapView.getOverlays().remove(peopleOverlay);
                mapView.invalidate();
                break;

        }

    }

    public void setFoodOverlay() {


        mapView.getOverlays().add(foodOverlay);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.foodmappin);
        List<Place> places = ((MainActivity) getActivity()).getDataFromAsstes(MainActivity.Food);
        foodMarkers = new ArrayList<Marker>();
        for (int i = 0; i < places.size(); i++) {

            Marker nodeMarker = new Marker(mapView);
            try {
                nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
                nodeMarker.setIcon(nodeIcon);

                //4. Filling the bubbles
                nodeMarker.setTitle(places.get(i).Title);
                Log.i(places.get(i).Category_Name, places.get(i).Title);
                nodeMarker.setInfoWindow(new CustomInfoWindow(mapView, OsmMapFragments.this));
                nodeMarker.setRelatedObject(places.get(i));
                foodMarkers.add(nodeMarker);
                mapView.getController().animateTo(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
                foodOverlay.add(nodeMarker);
            } catch (Exception e) {

            }

        }

    }

    public void setshopOverlay() {

        mapView.getOverlays().add(shopOverlay);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.shopsmappin);
        List<Place> places = ((MainActivity) getActivity()).getDataFromAsstes(MainActivity.Shop);
        shopMarkers = new ArrayList<Marker>();
        for (int i = 0; i < places.size(); i++) {

            Marker nodeMarker = new Marker(mapView);
            Log.i(places.get(i).Category_Name, places.get(i).Title);
            nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setInfoWindow(new CustomInfoWindow(mapView, OsmMapFragments.this));
            nodeMarker.setRelatedObject(places.get(i));
            shopMarkers.add(nodeMarker);
            //4. Filling the bubbles
            nodeMarker.setTitle(places.get(i).Title);
            mapView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapView.getController().setCenter(new GeoPoint(40.85572, 14.23908));
                }
            }, 2000);

            shopOverlay.add(nodeMarker);
        }

    }

    public void setFunOverlay() {

        mapView.getOverlays().add(funOverlay);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.funmappin);
        List<Place> places = ((MainActivity) getActivity()).getDataFromAsstes(MainActivity.Fun);
        FunMarkers = new ArrayList<Marker>();
        for (int i = 0; i < places.size(); i++) {

            Marker nodeMarker = new Marker(mapView);
            Log.i(places.get(i).Category_Name, places.get(i).Title);
            nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setInfoWindow(new CustomInfoWindow(mapView, OsmMapFragments.this));
            nodeMarker.setRelatedObject(places.get(i));
            mapView.getController().animateTo(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            //4. Filling the bubbles
            nodeMarker.setTitle(places.get(i).Title);

            FunMarkers.add(nodeMarker);
            funOverlay.add(nodeMarker);
            mapView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapView.getController().animateTo(new GeoPoint(40.85572, 14.23908));
                }
            }, 2000);
            mapView.invalidate();
        }

    }

    public void setPeopleOverlay() {

        mapView.getOverlays().add(peopleOverlay);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.peoplemappin);
        List<Place> places = ((MainActivity) getActivity()).getDataFromAsstes(MainActivity.People);
        peopleMarkers = new ArrayList<Marker>();
        for (int i = 0; i < places.size(); i++) {

            Marker nodeMarker = new Marker(mapView);
            Log.i(places.get(i).Category_Name, places.get(i).Title);
            nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setInfoWindow(new CustomInfoWindow(mapView, OsmMapFragments.this));
            peopleMarkers.add(nodeMarker);
            nodeMarker.setRelatedObject(places.get(i));
            mapView.getController().animateTo(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            //4. Filling the bubbles
            nodeMarker.setTitle(places.get(i).Title);

            mapView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapView.getController().animateTo(new GeoPoint(40.85572, 14.23908));
                }
            }, 2000);
            peopleOverlay.add(nodeMarker);
        }

    }

    public void setViewsOverlay() {

        mapView.getOverlays().add(viewsOverlay);
        Drawable nodeIcon = getResources().getDrawable(R.drawable.viewsmappin);
        List<Place> places = ((MainActivity) getActivity()).getDataFromAsstes(MainActivity.Views);
        viewsMarkers = new ArrayList<Marker>();
        for (int i = 0; i < places.size(); i++) {

            Marker nodeMarker = new Marker(mapView);
            Log.i(places.get(i).Category_Name, places.get(i).Title);
            nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            nodeMarker.setIcon(nodeIcon);
            mapView.getController().animateTo(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
            //4. Filling the bubbles
            nodeMarker.setTitle(places.get(i).Title);
            viewsMarkers.add(nodeMarker);
            nodeMarker.setInfoWindow(new CustomInfoWindow(mapView, OsmMapFragments.this));
            nodeMarker.setRelatedObject(places.get(i));
            mapView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapView.getController().animateTo(new GeoPoint(40.85572, 14.23908));
                }
            }, 2000);
            viewsOverlay.add(nodeMarker);
        }

    }

    @Override
    public void onMarketClick(Object item) {
        Marker marker = (Marker) item;
        Place palce = (Place) marker.getRelatedObject();
        String cat = palce.Category_Name;
        int plusindex = 0;
        if (cat.equalsIgnoreCase(MainActivity.Food))
            plusindex = 1;
        else if (cat.equalsIgnoreCase(MainActivity.Shop))
            plusindex = 2;
        else if (cat.equalsIgnoreCase(MainActivity.Views))
            plusindex = 3;
        else if (cat.equalsIgnoreCase(MainActivity.Fun))
            plusindex = 4;
        else if (cat.equalsIgnoreCase(MainActivity.People))
            plusindex = 5;

        int curPos = ((MainActivity) getActivity()).mViewPager.getCurrentItem();
        MainActivity.curplace = palce;
        Log.i("On Marker click", curPos + plusindex + "");

        ((MainActivity) getActivity()).mViewPager.setCurrentItem(curPos + plusindex);


    }


    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the

        resumeLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.

        pauseLocation();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void resumeLocation() {
         myLocationOverlay.enableMyLocation();

       // myLocationOverlay.enableFollowLocation();
    }

    public void pauseLocation() {
            myLocationOverlay.disableMyLocation();

          myLocationOverlay.disableFollowLocation();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

