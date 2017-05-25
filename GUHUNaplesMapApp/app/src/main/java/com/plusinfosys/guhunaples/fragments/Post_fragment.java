package com.plusinfosys.guhunaples.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.plusinfosys.guhunaples.MainActivity;
import com.plusinfosys.guhunaples.PostAdapter;
import com.plusinfosys.guhunaples.R;
import com.plusinfosys.guhunaples.maputils.MarkerOnClick;
import com.plusinfosys.guhunaples.model.Place;
import com.plusinfosys.guhunaples.widgets.LocaitonButtonListner;
import com.plusinfosys.guhunaples.widgets.MyCustomAnimation;
import com.plusinfosys.guhunaples.widgets.OnDrawerCloseListener;
import com.plusinfosys.guhunaples.widgets.SlidingDrawer;

import org.osmdroid.bonuspack.overlays.Marker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public class Post_fragment extends Fragment implements LocaitonButtonListner,OnDrawerCloseListener,MarkerOnClick {
    private ListView listView;
    private PostAdapter postAdapter;
    private static final String ARG_CATEGORY_NUMBER = "category_number";
    private ArrayList<Place> places;
    private ImageView toolbar;
    private FrameLayout mapLayout;
    private ImageView ropeImage;
    int height;
    OsmMapDialogFragments osmMapDialogFragments;
    SlidingDrawer sldingD;
    public static Post_fragment newInstance(String cat_id) {
        Post_fragment fragment = new Post_fragment();
        Bundle b = new Bundle();
        b.putString(ARG_CATEGORY_NUMBER, cat_id);
        fragment.setArguments(b);

        return fragment;
    }

    public Post_fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.post_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        toolbar = (ImageView) rootView.findViewById(R.id.activity_my_toolbar);




      //  mapLayout = (FrameLayout) rootView.findViewById(R.id.mapviewLayout);
       // mapLayout.setVisibility(View.GONE);

       // ropeImage = (ImageView) rootView.findViewById(R.id.rope);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (places == null) {

            places = ((MainActivity) getActivity()).getDataFromAsstes(getArguments().getString(ARG_CATEGORY_NUMBER));
        }
        osmMapDialogFragments = OsmMapDialogFragments.newInstance(getArguments().getString(ARG_CATEGORY_NUMBER),this);
        if (places != null) {
            getView().findViewById(R.id.EmptyView).setVisibility(View.GONE);
            listView.setAdapter(new PostAdapter(getActivity(), places, this));
        } else {
            getView().findViewById(R.id.EmptyView).setVisibility(View.VISIBLE);
        }
        if (getArguments().getString(ARG_CATEGORY_NUMBER).equals(MainActivity.Food)) {
            toolbar.setImageResource(R.drawable.food_icon_tab);
        } else if (getArguments().getString(ARG_CATEGORY_NUMBER).equals(MainActivity.Fun)) {
            toolbar.setImageResource(R.drawable.fun_icon_tab);
        } else if (getArguments().getString(ARG_CATEGORY_NUMBER).equals(MainActivity.People)) {
            toolbar.setImageResource(R.drawable.people_icon_tab);
        } else if (getArguments().getString(ARG_CATEGORY_NUMBER).equals(MainActivity.Shop)) {

            toolbar.setImageResource(R.drawable.shops_icon_tab);

         //   toolbar.setLogo(R.drawable.shops_icon_tab);
        } else if (getArguments().getString(ARG_CATEGORY_NUMBER).equals(MainActivity.Views)) {
            toolbar.setImageResource(R.drawable.views_icon_tab);
        }
        sldingD = (SlidingDrawer) getView().findViewById(R.id.slide);
        getChildFragmentManager().beginTransaction().replace(R.id.card_view, osmMapDialogFragments).commit();
        View view =new View(getActivity());
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackgroundResource(R.drawable.mapbg);
        ((FrameLayout)getView().findViewById(R.id.card_view)).addView(view);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



                for (int i=0; i < visibleItemCount; i++) {
                    View listItem = listView.getChildAt(i);
                    if (listItem == null)
                        break;

                    TextView title = (TextView) listItem.findViewById(R.id.txtTitle2);

                    int topMargin = 0;
                    if (i == 0) {
                        int top = listItem.getTop();
                        int height = listItem.getHeight();

                        // if top is negative, the list item has scrolled up.
                        // if the title view falls within the container's visible portion,
                        //     set the top margin to be the (inverse) scrolled amount of the container.
                        // else
                        //     set the top margin to be the difference between the heights.
                        if (top < 0)
                            topMargin = title.getHeight() < (top + height) ? -top : (height - title.getHeight());
                    }

                    // set the margin.
                    ((ViewGroup.MarginLayoutParams) title.getLayoutParams()).topMargin = topMargin;

                    // request Android to layout again.
                    listItem.requestLayout();



                }
            }

        });
        sldingD.setOnDrawerCloseListener(this);
        super.onActivityCreated(savedInstanceState);
    }



    public void scrollToItem(String title) {

        for (int i = 0; i < places.size(); i++) {
            if (title.equals(places.get(i).Title)) {
                listView.setSelection(i);
                Log.i("scroll to", places.get(i).Title);
                MainActivity.curplace = null;
                break;
            }
        }

    }

    @Override
    public void onClick(int pos) {


            sldingD.toggle();
            osmMapDialogFragments.setupSelectedOverLay(pos);


    }

    @Override
    public void onDrawerClosed() {
      osmMapDialogFragments.hideAllInfo();
    }

    @Override
    public void onMarketClick(Object marker) {
        Marker marker1 = (Marker) marker;
        scrollToItem(marker1.getTitle());
        sldingD.toggle();
    }
}
