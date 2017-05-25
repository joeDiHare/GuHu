package com.plusinfosys.guhunaples.model;

import java.io.Serializable;

/**
 * Created by pitwin002 on 1/22/2015.
 */
public class Place implements Serializable, Comparable {
    public String Category_Name, Display_Order, Title, Description_Text, Photo, Wikipedia_Link, Google1_Link, Google2_Link, latitude, longitude;

    @Override
    public int compareTo(Object another) {
        return Integer.valueOf(this.Display_Order)- Integer.valueOf(((Place) another).Display_Order);
    }
}
