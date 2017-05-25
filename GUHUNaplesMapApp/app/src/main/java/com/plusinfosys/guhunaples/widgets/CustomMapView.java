package com.plusinfosys.guhunaples.widgets;

/**
 * Created by pitwin002 on 1/24/2015.
 */

import microsoft.mappoint.TileSystem;

import org.osmdroid.ResourceProxy;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;



public class CustomMapView extends MapView {

    private static final String TAG = CustomMapView.class.getSimpleName();

    private int draggingCounter = 0;

    protected Rect mScrollableAreaLimit;
    private boolean isTapOnOverlay = false;


    /**
     * Time in ms before the OnLongpressListener is triggered.
     */
    static final int LONGPRESS_THRESHOLD = 500;

    public CustomMapView(Context context, int tileSizePixels, ResourceProxy myResourceProxy,MapTileProviderBase provider) {
        super(context, tileSizePixels, myResourceProxy,provider);

        double north = 40.922;
        double east  =14.134 ;
        double south = 40.789;
        double west  =  14.359;
        BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);
        setScrollableAreaLimit(bBox);
        setBuiltInZoomControls(false);
    }

    // @Override
    // public int getMaxZoomLevel() {
    // Log.e(TAG, "Zoom level has been changed----" + mapPreference.getMaxZoom());
    // return super.getMaxZoomLevel();
    //
    // }
    //
    // @Override
    // public int getMinZoomLevel() {
    // return mapPreference.getMinZoom();
    // }

    /**
     * Set the map to limit it's scrollable view to the specified BoundingBoxE6. Note that, like
     * North/South bounds limiting, this allows an overscroll of half the screen size. This means
     * each border can be scrolled to the center of the screen.
     *
     * @param
     * @param box  A lat/long bounding box to limit scrolling to, or null to remove any scrolling
     *             limitations
     */
    public void setScrollableAreaLimit(BoundingBoxE6 box) {

        final int worldSize_2 = TileSystem.MapSize(getMaxZoomLevel()) / 2;

        // Clear scrollable area limit if null passed.
        if (box == null) {
            mScrollableAreaLimit = null;
            return;
        }

        // Get NW/upper-left
        final Point upperLeft = TileSystem
                .LatLongToPixelXY(box.getLatNorthE6() / 1E6, box.getLonWestE6() / 1E6, getMaxZoomLevel(), null);
        upperLeft.offset(-worldSize_2, -worldSize_2);

        // Get SE/lower-right
        final Point lowerRight = TileSystem.LatLongToPixelXY(box.getLatSouthE6() / 1E6, box.getLonEastE6() / 1E6, getMaxZoomLevel(),
                null);
        lowerRight.offset(-worldSize_2, -worldSize_2);
        mScrollableAreaLimit = new Rect(upperLeft.x, upperLeft.y, lowerRight.x, lowerRight.y);
    }

    @Override
    public void scrollTo(int x, int y) {
        final int worldSize_2 = TileSystem.MapSize(this.getZoomLevel(true)) / 2;
        while (x < -worldSize_2) {
            x += worldSize_2 * 2;
        }
        while (x > worldSize_2) {
            x -= worldSize_2 * 2;
        }
        if (y < -worldSize_2) {
            y = -worldSize_2;
        }
        if (y > worldSize_2) {
            y = worldSize_2;
        }

        if (mScrollableAreaLimit != null) {
            final int zoomDiff = getMaxZoomLevel() - getZoomLevel();
            final int minX = mScrollableAreaLimit.left >> zoomDiff;
            final int minY = mScrollableAreaLimit.top >> zoomDiff;
            final int maxX = mScrollableAreaLimit.right >> zoomDiff;
            final int maxY = mScrollableAreaLimit.bottom >> zoomDiff;
            if (x < minX)
                x = minX;
            else if (x > maxX)
                x = maxX;
            if (y < minY)
                y = minY;
            else if (y > maxY)
                y = maxY;
        }
        super.scrollTo(x, y);

        // do callback on listener
        if (mListener != null) {
            final ScrollEvent event = new ScrollEvent(this, x, y);
            mListener.onScroll(event);
        }
    }

    @Override
    public void computeScroll() {
        final Scroller mScroller = getScroller();
        final int mZoomLevel = getZoomLevel(false);

        if (mScroller.computeScrollOffset()) {
            if (mScroller.isFinished()) {
                /**
                 * Need to jump through some accessibility hoops here Silly enough the only thing
                 * MapController.setZoom does is call MapView.setZoomLevel(zoomlevel). But noooo .. if I
                 * try that directly setZoomLevel needs to be set to "protected". Explanation can be
                 * found at http://docs.oracle.com/javase/tutorial /java/javaOO/accesscontrol.html
                 *
                 * This also suggests that if the subclass is made to be part of the package, this can
                 * be replaced by a simple call to setZoomLevel(mZoomLevel)
                 */
                // This will facilitate snapping-to any Snappable points.
                getController().setZoom(mZoomLevel);
            } else {
            /* correction for double tap */
                int targetZoomLevel = getZoomLevel();
                if (targetZoomLevel == mZoomLevel)
                    scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            postInvalidate(); // Keep on drawing until the animation has
            // finished.
        }
    }

    public void animateToGeopoint(double latitude, double longitude) {
        getController().animateTo(new GeoPoint(latitude, longitude));
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            draggingCounter = 0;
            Log.e(TAG, "Action Down..............." + draggingCounter);

        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            Log.e(TAG, "Touching..............." + draggingCounter);
            if (draggingCounter < 5) {
                Log.e(TAG, "Perform--------------" + draggingCounter + "--" + isTapOnOverlay);
                if (isTapOnOverlay) {

                } else {
                  /*  if (poiMarkers != null) {
                        poiMarkers.hideBubble();
                    } else {
                        Log.e(TAG, "Poi marker is null------------------------------");
                    }*/
                }
                // close();
            } else {

            }

        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            draggingCounter++;
            Log.e(TAG, "Dragging..............." + draggingCounter);

        }
        return super.onTouchEvent(e);
    }

    public void dontHidetheBubble(boolean isTapOnOverlay) {
        this.isTapOnOverlay = isTapOnOverlay;
    }
/*
    public void setPoiMArkerRef(ItemizedOverlayWithBubble<ExtendedOverlayItem> poiMarkers) {
        this.poiMarkers = poiMarkers;
    }*/

}