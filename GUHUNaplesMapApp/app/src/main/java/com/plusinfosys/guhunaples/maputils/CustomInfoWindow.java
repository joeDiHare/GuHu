package com.plusinfosys.guhunaples.maputils;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.plusinfosys.guhunaples.R;

import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

public class CustomInfoWindow extends MarkerInfoWindow {
    MarkerOnClick markerOnClick;

    public CustomInfoWindow(MapView mapView, final MarkerOnClick markerOnClick1) {

        super(R.layout.bonuspack_bubble, mapView);
        CustomInfoWindow.this.markerOnClick = markerOnClick1;

    /*  Button btn = (Button) (mView.findViewById(R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Button clicked", Toast.LENGTH_LONG).show();
              CustomInfoWindow.this.markerOnClick.onMarketClick(view.getTag());
            }
        });*/
        mView.findViewById(R.id.mainview).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                CustomInfoWindow.this.markerOnClick.onMarketClick(view.getTag());
            }
        });
    }

    @Override
    public void onOpen(Object item) {
        super.onOpen(item);
     //mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.mainview).setTag(item);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}