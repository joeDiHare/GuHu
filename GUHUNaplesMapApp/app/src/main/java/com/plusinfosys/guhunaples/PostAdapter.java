package com.plusinfosys.guhunaples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.plusinfosys.guhunaples.model.Place;
import com.plusinfosys.guhunaples.widgets.LocaitonButtonListner;
import com.plusinfosys.guhunaples.widgets.SquareImageView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public class PostAdapter extends BaseAdapter {
    public Activity mContext;
    private List<Place> mList;
    public int count = 0;
    private LayoutInflater mLayoutInflater = null;
    public LocaitonButtonListner locaitonButtonListner;
    DisplayImageOptions options;
    private int lastPosition = -1;

    public PostAdapter(Activity context, List<Place> list, LocaitonButtonListner locaitonButtonListner) {
        mContext = context;
        mList = list;
        this.locaitonButtonListner = locaitonButtonListner;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getCount() {
        return mList.size();
      /*  if (count > mList.size() || count < 0)
            return mList.size();
        else
            return count;*/
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolderPlace viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_layout2, null);

            viewHolder = new ViewHolderPlace(v, mContext, locaitonButtonListner);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderPlace) v.getTag();
        }
        Place place = mList.get(position);
        viewHolder.mTitle.setText(Html.fromHtml(place.Title));

        viewHolder.mDesc.setText(Html.fromHtml((place.Description_Text)));//, "text/html", "utf-8");

        viewHolder.googleBtn.setTag(place.Google1_Link);
        viewHolder.ReviewBtn.setTag(place.Google2_Link);
        viewHolder.WikiBtn.setTag(place.Wikipedia_Link);
        viewHolder.mapBtn.setTag(position);
        final ViewHolderPlace vs= viewHolder;
        //  int d=mContext.getResources().getIdentifier("t1p1", "drawable", mContext.getPackageName());
        // viewHolder.imagePhoto.setImageResource(d);
        ImageLoader.getInstance().loadImage("assets:/" + place.Photo.replace("shop/", ""), options, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                vs.imagePhoto.setImageBitmap(loadedImage);
                super.onLoadingComplete(imageUri, view, loadedImage);
            }
        });
       // ImageLoader.getInstance().displayImage("assets:/" + place.Photo.replace("shop/", ""), viewHolder.imagePhoto, options);
   /*     Animation animation = AnimationUtils.loadAnimation(v.getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        v.startAnimation(animation);
        lastPosition = position;*/
        return v;
    }
}

class ViewHolderPlace {
    public TextView mTitle;
    public TextView mDesc;
    public ImageView mapBtn, googleBtn, WikiBtn, ReviewBtn;
    public ImageView imagePhoto;

    public ViewHolderPlace(View base, final Context mContext, final LocaitonButtonListner locaitonButtonListner) {
        mTitle = (TextView) base.findViewById(R.id.txtTitle2);
        mTitle.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"Helvetica.otf"),Typeface.BOLD);

        mDesc = (TextView) base.findViewById(R.id.txtDesc);
        mDesc.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"Helvetica.otf"),Typeface.BOLD);
        imagePhoto = (ImageView) base.findViewById(R.id.imageView);
        mapBtn = (ImageView) base.findViewById(R.id.btnMapIcon);
        googleBtn = (ImageView) base.findViewById(R.id.btnGoogleIcon);
        WikiBtn = (ImageView) base.findViewById(R.id.btnWikiIcon);
        ReviewBtn = (ImageView) base.findViewById(R.id.btnReviewIcon);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locaitonButtonListner.onClick((int) v.getTag());
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) googleBtn.getTag()));
                mContext.startActivity(browserIntent);

            }
        });
        WikiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) WikiBtn.getTag()));
                mContext.startActivity(browserIntent);

            }
        });
        ReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) ReviewBtn.getTag()));
                mContext.startActivity(browserIntent);

            }
        });

    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
