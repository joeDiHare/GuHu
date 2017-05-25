package com.plusinfosys.guhunaples.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plusinfosys.guhunaples.MainActivity;
import com.plusinfosys.guhunaples.R;
import com.plusinfosys.guhunaples.maputils.CustomInfoWindow;
import com.plusinfosys.guhunaples.maputils.MBTileProvider;
import com.plusinfosys.guhunaples.maputils.MarkerOnClick;
import com.plusinfosys.guhunaples.model.Place;
import com.plusinfosys.guhunaples.widgets.LocaitonButtonListner;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.FolderOverlay;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public class OsmMapDialogFragments extends Fragment implements IRegisterReceiver, MarkerOnClick {
    private MapView mapView;


    FolderOverlay catOverlay;
    FolderOverlay SelectedOverlay;
    DefaultResourceProxyImpl resProxy;
    List<Place> places = null;
    List<Marker> markers = null;
    MarkerOnClick markerOnClick = null;
    double north = 40.922;
    double east = 14.17;
    double south = 40.789;
    double west = 14.319;
    BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);
    public GeoPoint geoPoint =new GeoPoint(40.85572, 14.23908);
    public static OsmMapDialogFragments newInstance(String cat, MarkerOnClick markerOnClick) {
        OsmMapDialogFragments fragment = new OsmMapDialogFragments();
        Bundle bundle = new Bundle();
        bundle.putString("cat", cat);

        fragment.setArguments(bundle);
        fragment.markerOnClick = markerOnClick;
        return fragment;
    }

    public OsmMapDialogFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        resProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());


        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getActivity().getApplicationContext().getPackageName();

        File file = new File(path, "Naples.mbtiles");
        if (file.exists()) {
            MBTileProvider provider = new MBTileProvider(this, file);
            mapView = new MapView(getActivity(), 256, resProxy, provider);
            return mapView;
        }
        mapView.setBackgroundResource(R.drawable.mapbg);
        return mapView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
       /* MapView mapView = new MapView(getActivity(),
                provider.getTileSource().getTileSizePixels(),
                resProxy,
                provider);*/

        mapView.setMultiTouchControls(true);
        mapView.setScrollableAreaLimit(bBox);

        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        IMapController controller = mapView.getController();


        controller.setZoom(14);
        catOverlay = new FolderOverlay(getActivity());

        mapView.getOverlays().add(catOverlay);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.getController().setCenter(geoPoint);

        setCatOverlay();
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    if (markers != null) {
                        for (int i = 0; i < markers.size(); i++) {
                            if (markers.get(i).isInfoWindowShown())
                                markers.get(i).hideInfoWindow();
                        }
                    }

                }
                return false;
            }
        });
    }
public void hideAllInfo()
{
    if (markers != null) {
        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).isInfoWindowShown())
                markers.get(i).hideInfoWindow();
        }
    }
}
    public void setupSelectedOverLay(int pos) {
        mapView.getOverlays().clear();
        // SelectedOverlay = new FolderOverlay(getActivity());
        if (markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                if (markers.get(i).isInfoWindowShown())
                    markers.get(i).hideInfoWindow();
                if (pos == i)
                    markers.get(i).showInfoWindow();
            }
        }
        geoPoint=markers.get(pos).getPosition();
        mapView.getController().setCenter(markers.get(pos).getPosition());
        mapView.getOverlays().add(catOverlay);
      /*  Marker nodeMarker = new Marker(mapView);
        try {
            nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(pos).latitude), Double.valueOf(places.get(pos).longitude)));

            Drawable nodeIcon;
            String cat = places.get(pos).Category_Name;
            if (cat.equalsIgnoreCase(MainActivity.Food))
                nodeIcon = getResources().getDrawable(R.drawable.foodmappin);
            else if (cat.equalsIgnoreCase(MainActivity.Shop))
                nodeIcon = getResources().getDrawable(R.drawable.shopsmappin);
            else if (cat.equalsIgnoreCase(MainActivity.Fun))
                nodeIcon = getResources().getDrawable(R.drawable.funmappin);
            else if (cat.equalsIgnoreCase(MainActivity.Views))
                nodeIcon = getResources().getDrawable(R.drawable.viewsmappin);
            else
                nodeIcon = getResources().getDrawable(R.drawable.peoplemappin);

            //4. Filling the bubbles
            nodeMarker.setIcon(nodeIcon);

            nodeMarker.setTitle(places.get(pos).Title);
            mapView.getController().setCenter(new GeoPoint(Double.valueOf(places.get(pos).latitude), Double.valueOf(places.get(pos).longitude)));
            mapView.getController().setZoom(15);
            SelectedOverlay.add(nodeMarker);
        } catch (Exception e) {

        }*/


    }

    @Override
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

    }


    public void setCatOverlay() {
        catOverlay = new FolderOverlay(getActivity());

        mapView.getOverlays().add(catOverlay);
        Drawable nodeIcon;
        markers = new ArrayList<Marker>();
        String cat = getArguments().getString("cat");
        if (cat.equalsIgnoreCase(MainActivity.Food))
            nodeIcon = getResources().getDrawable(R.drawable.foodmappin);
        else if (cat.equalsIgnoreCase(MainActivity.Shop))
            nodeIcon = getResources().getDrawable(R.drawable.shopsmappin);
        else if (cat.equalsIgnoreCase(MainActivity.Fun))
            nodeIcon = getResources().getDrawable(R.drawable.funmappin);
        else if (cat.equalsIgnoreCase(MainActivity.Views))
            nodeIcon = getResources().getDrawable(R.drawable.viewsmappin);
        else
            nodeIcon = getResources().getDrawable(R.drawable.peoplemappin);
        if (places == null)
            places = ((MainActivity) getActivity()).getDataFromAsstes(cat);
        for (int i = 0; i < places.size(); i++) {

            Marker nodeMarker = new Marker(mapView);
            try {
                nodeMarker.setPosition(new GeoPoint(Double.valueOf(places.get(i).latitude), Double.valueOf(places.get(i).longitude)));
                nodeMarker.setIcon(nodeIcon);

                //4. Filling the bubbles
                nodeMarker.setTitle(places.get(i).Title);
                Log.i(places.get(i).Category_Name, places.get(i).Title);
                markers.add(nodeMarker);
                nodeMarker.setInfoWindow(new CustomInfoWindow(mapView, OsmMapDialogFragments.this));
                catOverlay.add(nodeMarker);
            } catch (Exception e) {

            }

        }

    }


    @Override
    public void onMarketClick(Object marker) {
        if (markerOnClick != null)
            markerOnClick.onMarketClick(marker);
    }
}

