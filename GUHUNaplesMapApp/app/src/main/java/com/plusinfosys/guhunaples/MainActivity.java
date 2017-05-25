package com.plusinfosys.guhunaples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.plusinfosys.guhunaples.fragments.AboutFragments;
import com.plusinfosys.guhunaples.fragments.OsmMapFragments;
import com.plusinfosys.guhunaples.fragments.Post_fragment;
import com.plusinfosys.guhunaples.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class MainActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //SectionsPagerAdapter mSectionsPagerAdapter;
    ScreenSlidePagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;
    public static final String Food = "Food";
    public static final String Shop = "Shop";
    public static final String Views = "Views";
    public static final String Fun = "Fun";
    public static final String People = "People";
    public static Place curplace = null;
    public final int[] ToastImage = {R.drawable.icon_tab_map, R.drawable.icon_tab_food, R.drawable.icon_tab_shops, R.drawable.icon_tab_landmarks, R.drawable.icon_tab_entertainment, R.drawable.icon_tab_people, R.drawable.icon_tab_about};
    ImageView imageView;
    public static ArrayList<Place> placeArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        imageView = new ImageView(this);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        setDataFromAsstes();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                Fragment f = mSectionsPagerAdapter.getFragment(position);
                if (f instanceof Post_fragment) {
                    if (curplace != null) {
                        ((Post_fragment) f).scrollToItem(curplace.Title);

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public ArrayList<Place> getDataFromAsstes(String Catname) {


        ArrayList<Place> PlaceLayout = new ArrayList<Place>();


        for (Place place1 : placeArrayList) {

            if (place1.Category_Name.equalsIgnoreCase(Catname)) {


                PlaceLayout.add(place1);
            }

        }
        Collections.sort(PlaceLayout);
        return PlaceLayout;

    }

    public void setDataFromAsstes() {
        StringBuilder buf = new StringBuilder();
        InputStream json1;
        try {
            json1 = getAssets().open("DataGuhuApp.xml");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json1, "UTF-8"));
            String str = "";

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
            try {
                JSONObject josn = XML.toJSONObject(buf.toString());

                placeArrayList = new ArrayList<Place>();

                JSONArray jarry = josn.getJSONObject("GuHuDataInfo").getJSONArray("GuHuData");
                for (int i = 0; i < jarry.length(); i++) {
                    JSONObject j = jarry.getJSONObject(i);

                    Place place = new Place();
                    place.Category_Name = j.getString("Category_Name").replace("&#xD;", "").trim();
                    place.Display_Order = j.getString("Display_Order").replace("&#xD;", "").trim();
                    place.Title = j.getString("Title").replace("&#xD;", "").trim();
                    if(j.getString("Description_Text")!=null)
                    {
                    place.Description_Text = Html.fromHtml(j.getString("Description_Text")).toString();
                    }else       place.Description_Text="";

                    place.Photo = j.getString("Photo").replace("&#xD;", "").trim();
                    place.Wikipedia_Link = j.getString("Wikipedia_Link").replace("&#xD;", "").trim();
                    place.Google1_Link = j.getString("Google1_Link").replace("&#xD;", "").trim();
                    place.Google2_Link = j.getString("Google2_Link").replace("&#xD;", "").trim();
                    place.latitude = j.getJSONObject("Geo_Map_Coordinates").getString("latitude").replace("&#xD;", "").trim();
                    place.longitude = j.getJSONObject("Geo_Map_Coordinates").getString("longitude").replace("&#xD;", "").trim();
                    placeArrayList.add(place);


                }


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> slideList;

        public ScreenSlidePagerAdapter(FragmentManager fm) {

            super(fm);
            slideList = new ArrayList<Fragment>();
            slideList.add(OsmMapFragments.newInstance());
            slideList.add(Post_fragment.newInstance(Food));
            slideList.add(Post_fragment.newInstance(Shop));
            slideList.add(Post_fragment.newInstance(Views));
            slideList.add(Post_fragment.newInstance(Fun));
            slideList.add(Post_fragment.newInstance(People));
            slideList.add(AboutFragments.newInstance());
        }

        @Override
        public Fragment getItem(int position) {

            int virtualPosition = position % slideList.size();
            Log.i("virtualPosition:", String.format("Current Position = %d,Real position = %d", virtualPosition, position));
            return slideList.get(virtualPosition);
        }

        @Override
        public int getCount() {
            return slideList.size();
            //return Integer.MAX_VALUE;
        }

        public Fragment getFragment(int position) {
            int virtualPosition = position % slideList.size();
            return slideList.get(virtualPosition);
        }
    }

}
