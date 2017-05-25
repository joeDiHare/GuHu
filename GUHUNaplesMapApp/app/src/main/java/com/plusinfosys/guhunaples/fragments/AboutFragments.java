package com.plusinfosys.guhunaples.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.plusinfosys.guhunaples.R;
import com.plusinfosys.guhunaples.maputils.BoundedMapView;
import com.plusinfosys.guhunaples.maputils.MBTileProvider;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import java.io.File;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public  class AboutFragments extends Fragment  {
    private ImageView toolbar;
    //private String aboutText = "Guiding Humans - GuHu - was started by Stefano Cosentino and his two long-time freinds, Lorenzo \"Low\" Battista and Eric \"Pomodorino\" Greco in 2014. For more information about the project, visit guidinghumans.com.";
    private String aboutText="Guiding Humans - GuHu - is a project started by Stefano Cosentino. But it'd be very little, probably the size of a little ball of nothing, without the help of many friends. \n" +
            "For more information about the project, visit guidinghumans.com.";
    public static AboutFragments newInstance() {
        AboutFragments fragment = new AboutFragments();

        return fragment;
    }

    public AboutFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_layout, container, false);
        toolbar = (ImageView) rootView.findViewById(R.id.activity_my_toolbar);



        toolbar.setImageResource(R.drawable.about_icon_tab);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((TextView)getView().findViewById(R.id.aboutText)).setText(aboutText);
        super.onActivityCreated(savedInstanceState);
    }



}

